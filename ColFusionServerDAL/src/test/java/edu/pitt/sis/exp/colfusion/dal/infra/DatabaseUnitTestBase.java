package edu.pitt.sis.exp.colfusion.dal.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import edu.pitt.sis.exp.colfusion.dal.TestResourcesNames;
import edu.pitt.sis.exp.colfusion.dal.managers.ColumnTableInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ColumnTableInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.LicenseInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.LicenseInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoDbManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoDbManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManager;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionColumnTableInfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.ResourceUtils;
import edu.pitt.sis.exp.colfusion.utils.StreamUtils;
import edu.pitt.sis.exp.colfusion.utils.UnitTestBase;

public abstract class DatabaseUnitTestBase extends UnitTestBase {
	
	protected static final int DEFAULT_NUM_GENERATE_TUPLES = 5;

	private static final String TEST_TARGET_DATABASE_VENDOR = "mysql";

	private static final String TEST_TARGET_DATABASE_PREFIX = "test_target_database_";

	private static final Logger logger = LogManager.getLogger(DatabaseUnitTestBase.class.getName());
	
	static DockerClient dockerClient; 
	
	static ConfigManager configMng = ConfigManager.getInstance();
	
	static final Map<String, InspectContainerResponse> containerIdToContainer = new HashMap<String, InspectContainerResponse>();
	
	private static final int MYSQL_PORT_DEFAULT = 3306;
	private static final String DOCKER_MYSQL_IMAGE_NAME = TEST_TARGET_DATABASE_VENDOR;
	private static final String DOCKER_MYSQL_ROOT_USER = "root";
	private static final String DOCKER_ENV_MYSQL_ROOT_PASSWORD = "MYSQL_ROOT_PASSWORD";
	private static final String DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE = "root";
	
	private static final Set<String> systemPropertiesToClean = new HashSet<String>();
	
	/**
	 * Set during the {@link #setUp()}
	 */
	protected static DockerMySQLConnectionInfo dockerMySQLConnectionInfo;
	
	/**
	 * Set's up database for test.
	 * @throws URISyntaxException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@BeforeClass
	public static void setUp() throws Exception {
	
		initDockerClient();
		String containerId = startMySQLContainer();
		
		Class.forName("com.mysql.jdbc.Driver");
		
		setDbConnectionUrlAndDockerMySQLConnectionInfo(containerId);
		
		setSystemProperties(dockerMySQLConnectionInfo);
	}
	
	@Before
	public void createDatabase() throws ClassNotFoundException, SQLException, IOException {
		//Creating database
		String colfusionDatabaseName = configMng.getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG);
		String sql = makeCreateDatabaseSqlStatement(colfusionDatabaseName);
		executeMySQLUpdateQuery(dockerMySQLConnectionInfo.getConnectionString(), sql);
		
		String dbConectionUrl = dockerMySQLConnectionInfo.getConnectionString(colfusionDatabaseName);
		
		// Creating tables
		createTables(dbConectionUrl);
		insertTestData(dbConectionUrl);
	}
	
	@After
	public void dropDatabase() throws ClassNotFoundException, SQLException {
		String sql = String.format("DROP DATABASE IF EXISTS `%s`", 
				configMng.getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG));
		
		executeMySQLUpdateQuery(dockerMySQLConnectionInfo.getConnectionString(), sql);
	}
	
	/**
	 * Removes the database after test
	 * @throws IOException 
	 */
	@AfterClass
	public static void tearDown() throws IOException {
		for (Entry<String, InspectContainerResponse> entry : containerIdToContainer.entrySet()) {
			//TODO: check if container is running
			dockerClient.stopContainerCmd(entry.getKey()).exec();
			//TODO: check if container exists
			dockerClient.removeContainerCmd(entry.getKey()).exec();
		}
		
		for (String sysProperty : systemPropertiesToClean) {
			System.clearProperty(sysProperty);
		};
		
		dockerClient.close();
	}
	
	/**
	 * 
	 * @param containerId
	 * @return connection string lack schema name
	 * @throws URISyntaxException
	 */
	private static void setDbConnectionUrlAndDockerMySQLConnectionInfo(final String containerId) throws URISyntaxException {
		String host = new URI(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_URI)).getHost();
		int port = getHostPort(containerId, ExposedPort.tcp(MYSQL_PORT_DEFAULT));
		
		dockerMySQLConnectionInfo = new DockerMySQLConnectionInfo(host,  port,  DOCKER_MYSQL_ROOT_USER, DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE);
	}
	
	/**
	 * @param dbConectionUrl 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws HibernateException
	 * @throws IOException
	 */
	private void createTables(final String dbConectionUrl) throws ClassNotFoundException, SQLException,
			HibernateException, IOException {
		
		// This will trigger hibernate to create tables from mappings
		HibernateUtil.beginTransaction();
		HibernateUtil.commitTransaction();
		
		createPentahoLoggingTables(dbConectionUrl);
	}

	/**
	 * @param databaseName 
	 * @return
	 */
	private static String makeCreateDatabaseSqlStatement(final String databaseName) {
		String sql = String.format("CREATE DATABASE IF NOT EXISTS `%s`", databaseName);
		return sql;
	}
	
	private static String makeCreateTableSqlStatement(final String tableName, final String... columnNames) {
		StringBuilder sqlBuilder = new StringBuilder(String.format("CREATE TABLE IF NOT EXISTS `%s` (", tableName));
		
		int index = 0;
		
		for (String columnName : columnNames) {
			
			if (index++ == columnNames.length - 1) { // The difference is in the last character: comma or parenthesis.
				sqlBuilder.append(String.format("`%s` VARCHAR(255))", columnName));
			}
			else {
				sqlBuilder.append(String.format("`%s` VARCHAR(255),", columnName));
			}
		}
				
		return sqlBuilder.toString();
	}
	
	private static String makeRandomInsertSampleData(final String tableName,
			final String[] columnNames, final int numTuples) {
		StringBuilder sqlBuilder = new StringBuilder(String.format("INSERT INTO `%s` ", tableName)).append(" VALUES ");
		
		for (int i = 0; i < numTuples; i++) {
			sqlBuilder.append("(");
			
			for (int j = 0; j < columnNames.length; j++) {
				sqlBuilder.append(String.format("'%s %d %d'", columnNames[j], i, j));
				if (j < columnNames.length - 1) {
					sqlBuilder.append(",");
				}
			}
			
			sqlBuilder.append(")").append((i == numTuples - 1) ? ";" : ",");
		}
		
		return sqlBuilder.toString();
	}
	
	private void createPentahoLoggingTables(final String dbConnectionUrl) throws IOException, ClassNotFoundException, SQLException {
		// Hibernate cannot create pentaho logging tables, so we create them manually from sql script
		
		InputStream fileContentStream = ResourceUtils.getResourceAsStream(this.getClass(), TestResourcesNames.DATABASE_ADDITONAL_SETUP_CREATE_PENTAHO_TABLES);
		
		executeUpdateMutliStatementScript(dbConnectionUrl, fileContentStream);
	}

	private void insertTestData(final String dbConnectionUrl) throws ClassNotFoundException, IOException, SQLException {
		InputStream fileContentStream = ResourceUtils.getResourceAsStream(this.getClass(), TestResourcesNames.DATABASE_ADDITONAL_SETUP_INSERTS);
		
		executeUpdateMutliStatementScript(dbConnectionUrl, fileContentStream);
	}
	
	private void executeUpdateMutliStatementScript(final String dbConnectionUrl, final InputStream fileContentStream) throws IOException, ClassNotFoundException, SQLException {
		String fileContentStrOneLine = StreamUtils.toString(fileContentStream).replaceAll("[\\t\\n\\r]+"," ");

		String[] separateSqls = fileContentStrOneLine.split(";");
		
		for (String sql : separateSqls) {
			executeMySQLUpdateQuery(dbConnectionUrl, sql);
		}
	}
	
	/**
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected static void executeMySQLUpdateQuery(final String connectionUrl, final String query) throws ClassNotFoundException,
			SQLException {
		if ("".equals(query)) {
			return;
		}
		
		try (Connection connection = DriverManager.getConnection(connectionUrl, 
				DOCKER_MYSQL_ROOT_USER, DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE)) {
			try (Statement statement = connection.createStatement()){
				
				statement.executeUpdate(query);
			} 
		}
	}
	
	/**
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	protected List<Object[]> executeMySQLQuery(final String connectionUrl, final String query) throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		
		try (Connection connection = DriverManager.getConnection(connectionUrl, 
				DOCKER_MYSQL_ROOT_USER, DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE)) {
			try (Statement statement = connection.createStatement()){
				
				 ResultSet resultSet = statement.executeQuery(query);
				 
				 List<Object[]> result = new ArrayList<Object[]>();
				 
				 int numColumns = resultSet.getMetaData().getColumnCount();
				 
				 while (resultSet.next()) {
					 
					 Object[] row = new Object[numColumns];
					 
					 for (int i = 1; i <= numColumns; i++) {
						 row[i - 1] = resultSet.getObject(i);
					 }
					 
					 result.add(row);
				 }
				 
				 return result;
			} 
		}
	}

	/**
	 * @param dockerMySQLConnectionInfo2
	 */
	private static void setSystemProperties(final DockerMySQLConnectionInfo dockerMySQLConnectionInfo2) {
		String connectionStringWithSchema = dockerMySQLConnectionInfo2.getConnectionString(
				configMng.getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG));
		
		System.setProperty(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_URL.getKey(), connectionStringWithSchema);
		systemPropertiesToClean.add(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_URL.getKey());
		System.setProperty(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_USERNAME.getKey(), DOCKER_MYSQL_ROOT_USER);
		systemPropertiesToClean.add(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_USERNAME.getKey());
		System.setProperty(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_PASSWORD.getKey(), DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE);
		systemPropertiesToClean.add(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_PASSWORD.getKey());
	}

	/**
	 * 
	 */
	private static void initDockerClient() {
		DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
			    .withVersion(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_VERSION))
			    .withUri(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_URI))
			    .withServerAddress(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_SERVER_ADDRESS))
			    .withDockerCertPath(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_CERT_PATH))
			    .build();
				
		dockerClient = DockerClientBuilder.getInstance(config).build();
	}
	
	protected static String startMySQLContainer() throws IOException, InterruptedException {
		CreateContainerResponse container = dockerClient.createContainerCmd(DOCKER_MYSQL_IMAGE_NAME)
		.withEnv(String.format("%s=%s", DOCKER_ENV_MYSQL_ROOT_PASSWORD, DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE))
		.exec();
		
		dockerClient.startContainerCmd(container.getId())
		   .withPublishAllPorts(true)
		   .exec();
		
		waitUntilMySQLStartedInContainer(container.getId());
		
		InspectContainerResponse inspectResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
		
		containerIdToContainer.put(container.getId(), inspectResponse);
		
		return container.getId();
	}
	
	private static void waitUntilMySQLStartedInContainer(final String containerId) throws IOException, InterruptedException {
		InputStream io = dockerClient.logContainerCmd(containerId)
				.withStdOut(true)
				.withStdErr(true)
				.withTailAll()
				.withFollowStream(true)
				.exec();
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(io));
		String line = null;
		//TODO: potential "deadlock" if there never a line that contains that string.
		while ((line = bf.readLine()) != null) {
			System.out.println(line);
			if (line.toLowerCase().contains("mysqld: ready for connections.".toLowerCase())) {
				break;
			}
		}
		
		bf.close();
		io.close();
	}

	protected static int getHostPort(final String containerId, final ExposedPort containerPort) {
		InspectContainerResponse containerInspect = containerIdToContainer.get(containerId);
		
		if (containerInspect == null) {
			String message = String.format("Could not get host port for container port %s. "
					+ "Seems like container with id '%s' is not started. Call startContainer first.", 
					containerPort.toString() ,containerId);
			
			logger.error(message);
			throw new RuntimeException(message);
		}
		
		Binding[] bindings = containerInspect.getNetworkSettings().getPorts().getBindings().get(containerPort);
		
		if (bindings == null || bindings.length == 0) {
			String message = String.format("Could not get host port for container port %s and container id '%s'."
					+ "getNetworkSettings().getPorts().getBindings().get(containerPort) returned null or empty array from this"
					+ "inspect json: %s", 
					containerPort.toString() ,containerId, containerInspect.toString());
			
			logger.error(message);
			throw new RuntimeException(message);
		}
		
		//TODO not should why bindings is an array, just get the first one		
		return bindings[0].getHostPort();
	}
	
	protected ColfusionSourceinfo setUpTestStory(final String tableName, final String... columnNames) throws Exception {
		ColfusionSourceinfo story = createAndInsertNewStory();
		
		createAndInsertTableAndColumnMetadata(story, tableName, columnNames);
		
		String targetDatabaseName = TEST_TARGET_DATABASE_PREFIX + story.getSid();
		createAndInsertSourceInfoDB(story, dockerMySQLConnectionInfo, targetDatabaseName, TEST_TARGET_DATABASE_VENDOR);
		
		generateAndInsertSampleData(tableName, columnNames, targetDatabaseName, dockerMySQLConnectionInfo);
		
		return story;
	}
	
	private static void generateAndInsertSampleData(final String tabelName,
			final String[] columnNames, final String targetDatabaseName,
			final DockerMySQLConnectionInfo dockerMySQLConnectionInfoP) throws ClassNotFoundException, SQLException {
		
		{
			String sql = makeCreateDatabaseSqlStatement(targetDatabaseName);
			String dbConnectionUrl = dockerMySQLConnectionInfoP.getConnectionString();
			executeMySQLUpdateQuery(dbConnectionUrl, sql);
		}
		
		String dbConnectionUrl = dockerMySQLConnectionInfoP.getConnectionString(targetDatabaseName);
		
		{
			String sql = makeCreateTableSqlStatement(tabelName, columnNames);
			executeMySQLUpdateQuery(dbConnectionUrl, sql);
		}
		
		{
			String sql = makeRandomInsertSampleData(tabelName, columnNames, DEFAULT_NUM_GENERATE_TUPLES);
			executeMySQLUpdateQuery(dbConnectionUrl, sql);
		}		
	}

	/**
	 * Creates and saves to db new sourceinfo tuple. It assumes database already has some licenses and users info.
	 * @return
	 * @throws Exception
	 */
	private ColfusionSourceinfo createAndInsertNewStory() throws Exception {
		SourceInfoManager sourceInfoMng = new SourceInfoManagerImpl();
		LicenseInfoManager licenseMng = new LicenseInfoManagerImpl();
		UserManager userMng =  new UserManagerImpl();
		
		ColfusionLicense license = licenseMng.findAll().get(0);
		ColfusionUsers user = userMng.findAll().get(0);
		
		ColfusionSourceinfo story = new ColfusionSourceinfo(license, user, "testDoReplication", "", new Date(), new Date(), "queued", 
				"", "data type", "", null, null, null, null, null, null, null, null, null, null, null, null, null);
	
		sourceInfoMng.save(story);
		
		return story;
	}
	
	/**
	 * Creates and saves to db tuples in the dnameInfo table and ColunTableInfo table.
	 * @param story
	 * @param tableName
	 * @param columnNames
	 * @throws Exception
	 */
	private void createAndInsertTableAndColumnMetadata(
			final ColfusionSourceinfo story, final String tableName, final String... columnNames) throws Exception {
		DNameInfoManager dNameInfoMng = new DNameInfoManagerImpl();
		ColumnTableInfoManager columnTableMng = new ColumnTableInfoManagerImpl();
		
		for(String columnName : columnNames) {
			ColfusionDnameinfo column = new ColfusionDnameinfo(story, columnName, "dnameValueType", "dnameValueUnit", 
					"dnameValueFormat", "dnameValueDescription", columnName, false, "constantValue", 
					"missingValue", null, null);
			
			dNameInfoMng.save(column);
			
			ColfusionColumnTableInfo columnTable = new ColfusionColumnTableInfo(column, tableName, tableName);
			
			columnTableMng.save(columnTable);
		}		
	}
	
	private void createAndInsertSourceInfoDB(final ColfusionSourceinfo story,
			final DockerMySQLConnectionInfo dockerMySQLConnectionInfo2,
			final String targetDatabaseName, final String driver) throws Exception {
		ColfusionSourceinfoDb sourceInfoDb = new ColfusionSourceinfoDb(story, dockerMySQLConnectionInfo2.getHost(), dockerMySQLConnectionInfo2.getPort(),
				dockerMySQLConnectionInfo2.getUserName(), dockerMySQLConnectionInfo2.getPasswordt(), targetDatabaseName, driver);
		
		SourceInfoDbManager sourceInfoDbMng = new SourceInfoDbManagerImpl();
		
		sourceInfoDbMng.saveOrUpdate(sourceInfoDb);
		
		story.setColfusionSourceinfoDb(sourceInfoDb);
	}
	
	public static class DockerMySQLConnectionInfo {
		private final String host;
		private final int port;
		private final String userName;
		private final String password;
		
		public DockerMySQLConnectionInfo(final String host, final int port, final String userName, final String password) {
			this.host = host;
			this.port = port;
			this.userName = userName;
			this.password = password;
		}
		
		public String getHost() {
			return host;
		}
		
		public int getPort() {
			return port;
		}
		
		public String getUserName() {
			return userName;
		}
		
		public String getPasswordt() {
			return password;
		}
		
		/**
		 * Constructs MySQL JDBC connection string without default catalog.
		 * @return
		 */
		public String getConnectionString() {
			return String.format("jdbc:mysql://%s:%d", host, port);
		}
		
		/**
		 * Constructs MySQL JDBC connection string including the provided database as the default catalog.
		 * 
		 * @param databaseName
		 * 		the name of the database (NOTE: currently names containing spaces are not supported)
		 * @return MySQL JDBC connection string
		 * @throws RuntimeException is database name contains spaces.
		 */
		public String getConnectionString(final String databaseName) {
			//TODO: this connection string will not work for database names with spaces
			if (databaseName.contains(" ")) {
				String message = String.format("Currently database names that contain spaces are not supported. '%s'", databaseName);
				logger.error(message);
				throw new RuntimeException(message);
			}
			
			return String.format("jdbc:mysql://%s:%d/%s", host, port, databaseName);
		}
	}
}
