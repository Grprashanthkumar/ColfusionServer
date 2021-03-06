package edu.pitt.sis.exp.colfusion.dal.infra;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

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
import edu.pitt.sis.exp.colfusion.utils.DockerTestBase;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.ResourceUtils;
import edu.pitt.sis.exp.colfusion.utils.StreamUtils;
import edu.pitt.sis.exp.colfusion.utils.docker.DockerImageType;
import edu.pitt.sis.exp.colfusion.utils.docker.containers.MySQLDockerContainer;
import edu.pitt.sis.exp.colfusion.utils.docker.containers.MySQLDockerContainer.MySQLDockerContainerConnectionInfo;

public abstract class DatabaseUnitTestBase extends DockerTestBase {
	
	protected static final int DEFAULT_NUM_GENERATE_TUPLES = 5;

	private static final String TEST_TARGET_DATABASE_VENDOR = "mysql";

	private static final String TEST_TARGET_DATABASE_PREFIX = 
			ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_DATABASE_NAME_PREFIX);

	protected String TEST_TARGET_TABLE_NAME = "test table name";
	protected String[] TEST_TARGET_COLUMNS_NAMES = new String[] {"Column A", "Column B", "Column C"};
	
	private static final Logger logger = LogManager.getLogger(DatabaseUnitTestBase.class.getName());

	static ConfigManager configMng = ConfigManager.getInstance();

	/**
	 * Set during {@link #setUp()}
	 */
	protected static MySQLDockerContainer mysqlContainer;
	
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
	
		mysqlContainer = (MySQLDockerContainer) createAndStartContainer(DockerImageType.MYSQL_IMAGE);
		
		Class.forName("com.mysql.jdbc.Driver");
		
		setSystemProperties(mysqlContainer.getMySQLConnectionInfo());
	}
	
	@Before
	public void createDatabase() throws ClassNotFoundException, SQLException, IOException {
		//Creating database
		String colfusionDatabaseName = configMng.getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG);
		String sql = makeCreateDatabaseSqlStatement(colfusionDatabaseName);
		executeMySQLUpdateQuery(mysqlContainer.getMySQLConnectionInfo().getConnectionString(), sql);
		
		String dbConectionUrl = mysqlContainer.getMySQLConnectionInfo().getConnectionString(colfusionDatabaseName);
		
		// Creating tables
		createTables(dbConectionUrl);
		insertTestData(dbConectionUrl);
	}
	
	@After
	public void dropDatabase() throws ClassNotFoundException, SQLException {
		//TODO: drop all databases, not just colfusion, but also those create to hold data. Don't drop mysql system databases.
		String sql = String.format("DROP DATABASE IF EXISTS `%s`", 
				configMng.getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG));
		
		executeMySQLUpdateQuery(mysqlContainer.getMySQLConnectionInfo().getConnectionString(), sql);
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
		HibernateUtil.initiSessionFactory();
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
		//TODO FIXME Hibernate cannot create SOME pentaho logging tables, so we create them manually from sql script
		
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
		
		logger.info(String.format("About to execute MySQL Update Query at connection %s query: %s", connectionUrl, query));
		
		try (Connection connection = getConnection(connectionUrl)) {
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
		
		try (Connection connection = getConnection(connectionUrl)) {
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

	protected static Connection getConnection(final String connectionUrl) throws SQLException {
		logger.info(String.format("Getting connection for url '%s', username '%s'", connectionUrl, mysqlContainer.getMySQLConnectionInfo().getUserName()));
		
		return DriverManager.getConnection(connectionUrl, 
				mysqlContainer.getMySQLConnectionInfo().getUserName(), mysqlContainer.getMySQLConnectionInfo().getPassword());
	}
	
	/**
	 * @param dockerMySQLConnectionInfo2
	 */
	private static void setSystemProperties(final MySQLDockerContainerConnectionInfo mySQLDockerContainerConnectionInfo) {
		String connectionStringWithSchema = mySQLDockerContainerConnectionInfo.getConnectionString(
				configMng.getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG));
		
		redefineSystemPropertyForClass(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_URL.getKey(), connectionStringWithSchema);
		redefineSystemPropertyForClass(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_USERNAME.getKey(), mySQLDockerContainerConnectionInfo.getUserName());
		redefineSystemPropertyForClass(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_PASSWORD.getKey(), mySQLDockerContainerConnectionInfo.getPassword());
	}
		
	protected ColfusionSourceinfo setUpTestStory(final String tableName, final String... columnNames) throws Exception {
		ColfusionSourceinfo story = createAndInsertNewStory();
		
		createAndInsertTableAndColumnMetadata(story, tableName, columnNames);
		
		String targetDatabaseName = TEST_TARGET_DATABASE_PREFIX + story.getSid();
		createAndInsertSourceInfoDB(story, mysqlContainer.getMySQLConnectionInfo(), targetDatabaseName, TEST_TARGET_DATABASE_VENDOR);
		
		generateAndInsertSampleData(tableName, columnNames, targetDatabaseName, mysqlContainer.getMySQLConnectionInfo());
		
		return story;
	}
	
	private static void generateAndInsertSampleData(final String tabelName,
			final String[] columnNames, final String targetDatabaseName,
			final MySQLDockerContainerConnectionInfo mySQLDockerContainerConnectionInfo) throws ClassNotFoundException, SQLException {
		
		{
			String sql = makeCreateDatabaseSqlStatement(targetDatabaseName);
			String dbConnectionUrl = mySQLDockerContainerConnectionInfo.getConnectionString();
			executeMySQLUpdateQuery(dbConnectionUrl, sql);
		}
		
		String dbConnectionUrl = mySQLDockerContainerConnectionInfo.getConnectionString(targetDatabaseName);
		
		{
			String sql = makeCreateTableSqlStatement(tabelName, columnNames);
			executeMySQLUpdateQuery(dbConnectionUrl, sql);
		}
		
		{
			String sql = makeRandomInsertSampleData(tabelName, columnNames, DEFAULT_NUM_GENERATE_TUPLES);
			executeMySQLUpdateQuery(dbConnectionUrl, sql);
		}		
		
		{
			
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
		
		ColfusionLicense license = licenseMng.findAll().get(0);
		ColfusionUsers user = getTestUser();
		
		ColfusionSourceinfo story = new ColfusionSourceinfo(license, user, "testDoReplication", "", "", new Date(), new Date(), "queued", 
				"", "data type", "", null, null, null, null, null, null, null, null, null, null, null, null, null);
		sourceInfoMng.save(story);
		
//		Does sourveInfoMng.save(story) save some random attachment data or relationship data in the database?
//		String sql = makeRelatedStoryData();
//		executeMySQLUpdateQuery(dbConnectionUrl, sql);
		
		return story;
	}
	
	protected ColfusionUsers getTestUser() throws Exception {
		UserManager userMng =  new UserManagerImpl();
		ColfusionUsers user = userMng.findAll().get(0);
		
		return user;
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
	
	private static String makeRelatedStoryData() {
		StringBuilder sqlBuilder = new StringBuilder();
		return sqlBuilder.toString();
	}
	
	private void createAndInsertSourceInfoDB(final ColfusionSourceinfo story,
			final MySQLDockerContainerConnectionInfo mySQLDockerContainerConnectionInfo,
			final String targetDatabaseName, final String driver) throws Exception {
		ColfusionSourceinfoDb sourceInfoDb = new ColfusionSourceinfoDb(story, mySQLDockerContainerConnectionInfo.getHost(), 
				mySQLDockerContainerConnectionInfo.getPort(),
				mySQLDockerContainerConnectionInfo.getUserName(), mySQLDockerContainerConnectionInfo.getPassword(), targetDatabaseName, driver);
		
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
