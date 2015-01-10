/**
 * 
 */
package edu.pitt.sis.exp.colfusion.utils.docker.containers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.StringUtils;
import edu.pitt.sis.exp.colfusion.utils.docker.ColfusionDockerClient;
import edu.pitt.sis.exp.colfusion.utils.docker.containerProviders.AbstractDockerContainerProvider;
import edu.pitt.sis.exp.colfusion.utils.docker.containerProviders.MySQLContainerProvider;


/**
 * @author Evgeny
 *
 */
public class MySQLDockerContainer extends AbstractDockerContainer {

	MySQLDockerContainerConnectionInfo connectionInfo;
	
	public MySQLDockerContainer(final String containerId, final ColfusionDockerClient dockerClient,
			final AbstractDockerContainerProvider<MySQLDockerContainer> containerProvider) {
		super(containerId, dockerClient, containerProvider);
	}

	@Override
	protected boolean isContainerStarted() {
		
		try {
			InputStream io = dockerClient.logContainer(containerId);
		
			BufferedReader bf = new BufferedReader(new InputStreamReader(io));
			String line = null;
			StringBuilder logBuilder = new StringBuilder();
			//TODO: potential "deadlock" if there never a line that contains that string. Check for mysql shutdown
			while ((line = bf.readLine()) != null) {
				logBuilder.append(line).append(StringUtils.NEWLINE);
//				System.out.println(line);
				if (line.toLowerCase().contains("mysqld: ready for connections.".toLowerCase())) {
					break;
				}
				if (line.toLowerCase().contains("/usr/sbin/mysqld: Shutdown complete".toLowerCase())) {
					String message = String.format("Couldn't start mysql image for container '%s' because mysqld could not start."
							+ "The log from container is: %s", containerId, logBuilder.toString());
					logger.error(message);
					throw new RuntimeException(message);
				}
			}
			
			bf.close();
			io.close();
		}
		catch (Exception e) {
			//TODO: add logger statements
			return false;
		}
		
		return true;
	}

	@Override
	protected void postStartContainer() throws Exception {
		
		String host = new URI(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_DOCKER_URI)).getHost();
		int port = getHostPort(MySQLContainerProvider.MYSQL_PORT_DEFAULT);
		
		connectionInfo = new MySQLDockerContainerConnectionInfo(host,  port,  
				MySQLContainerProvider.DOCKER_MYSQL_ROOT_USER, MySQLContainerProvider.DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE);
	}

	public MySQLDockerContainerConnectionInfo getMySQLConnectionInfo() {
		return connectionInfo;
	}
	
	public static class MySQLDockerContainerConnectionInfo {
		private final String host;
		private final int port;
		private final String userName;
		private final String password;
		
		public MySQLDockerContainerConnectionInfo(final String host, final int port, final String userName, final String password) {
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
				throw new RuntimeException(message);
			}
			
			return String.format("jdbc:mysql://%s:%d/%s", host, port, databaseName);
		}
	}
}
