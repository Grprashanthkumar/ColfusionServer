package edu.pitt.sis.exp.colfusion.utils.test.infra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

public abstract class DatabaseUnitTestBase extends UnitTestBase {

	private static final Logger logger = LogManager.getLogger(DatabaseUnitTestBase.class.getName());
	
	static DockerClient dockerClient; 
	
	static ConfigManager configMng = ConfigManager.getInstance();
	
	static final Map<String, InspectContainerResponse> containerIdToContainer = new HashMap<String, InspectContainerResponse>();
	
	private static final int MYSQL_PORT_DEFAULT = 3306;
	private static final String DOCKER_MYSQL_IMAGE_NAME = "mysql";
	private static final String DOCKER_MYSQL_ROOT_USER = "root";
	private static final String DOCKER_ENV_MYSQL_ROOT_PASSWORD = "MYSQL_ROOT_PASSWORD";
	private static final String DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE = "root";
	
	private static final Set<String> systemPropertiesToClean = new HashSet<String>();
	
	private static String dbConnectionUrl;
	
	/**
	 * Set's up database for test.
	 * @throws URISyntaxException 
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@BeforeClass
	public static void setUp() throws URISyntaxException, ClassNotFoundException, SQLException, IOException, InterruptedException {
		initDockerClient();
		String containerId = startMySQLContainer();
		dbConnectionUrl = constructDbConnectionUrl(containerId);
		
		setSystemProperties(dbConnectionUrl);
	}
	
	@Before
	public void createDatabase() throws ClassNotFoundException, SQLException {
		String sql = String.format("CREATE DATABASE IF NOT EXISTS `%s`", 
				configMng.getProperty(PropertyKeys.HIBERNATE_DEFAULT_CATALOG));
		
		executeMySQLUpdateQuery(sql);
	}
	
	@After
	public void dropDatabase() throws ClassNotFoundException, SQLException {
		String sql = String.format("DROP DATABASE IF EXISTS `%s`", 
				configMng.getProperty(PropertyKeys.HIBERNATE_DEFAULT_CATALOG));
		
		executeMySQLUpdateQuery(sql);
	}
	
	/**
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private void executeMySQLUpdateQuery(final String query) throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		
		try (Connection connection = DriverManager.getConnection(dbConnectionUrl, 
				DOCKER_MYSQL_ROOT_USER, DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE)) {
			try (Statement statement = connection.createStatement()){
				
				statement.executeUpdate(query);
			} 
		}
	}

	/**
	 * @param dbConnectionUrl
	 */
	private static void setSystemProperties(final String dbConnectionUrl) {
		String connectionStringWithSchema = String.format("%s/%s", dbConnectionUrl, 
				configMng.getProperty(PropertyKeys.HIBERNATE_DEFAULT_CATALOG));
		
		System.setProperty(PropertyKeys.HIBERNATE_CONNECTION_URL.getKey(), connectionStringWithSchema);
		systemPropertiesToClean.add(PropertyKeys.HIBERNATE_CONNECTION_URL.getKey());
		System.setProperty(PropertyKeys.HIBERNATE_CONNECTION_USERNAME.getKey(), DOCKER_MYSQL_ROOT_USER);
		systemPropertiesToClean.add(PropertyKeys.HIBERNATE_CONNECTION_USERNAME.getKey());
		System.setProperty(PropertyKeys.HIBERNATE_CONNECTION_PASSWORD.getKey(), DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE);
		systemPropertiesToClean.add(PropertyKeys.HIBERNATE_CONNECTION_PASSWORD.getKey());
	}

	/**
	 * 
	 * @param containerId
	 * @return connection string lack schema name
	 * @throws URISyntaxException
	 */
	private static String constructDbConnectionUrl(final String containerId) throws URISyntaxException {
		String host = new URI(configMng.getProperty(PropertyKeysTest.COLFUSION_DOCKER_URI)).getHost();
		int port = getHostPort(containerId, ExposedPort.tcp(MYSQL_PORT_DEFAULT));
		
		return String.format("jdbc:mysql://%s:%d", host, port);
	}

	/**
	 * 
	 */
	private static void initDockerClient() {
		DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
			    .withVersion(configMng.getProperty(PropertyKeysTest.COLFUSION_DOCKER_VERSION))
			    .withUri(configMng.getProperty(PropertyKeysTest.COLFUSION_DOCKER_URI))
			    .withServerAddress(configMng.getProperty(PropertyKeysTest.COLFUSION_DOCKER_SERVER_ADDRESS))
			    .withDockerCertPath(configMng.getProperty(PropertyKeysTest.COLFUSION_DOCKER_CERT_PATH))
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
}
