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

public class MySQLDockerContainer extends AbstractDockerContainer {

	MySQLDockerContainerConnectionInfo connectionInfo;
	private static int LOG_RETRY_NUMBER = 25;
	private static int LOG_RETRY_SLEEP_MS = 3000;
	
	public MySQLDockerContainer(final String containerId, final ColfusionDockerClient dockerClient,
			final AbstractDockerContainerProvider<MySQLDockerContainer> containerProvider) {
		super(containerId, dockerClient, containerProvider);
	}

	@Override
	protected boolean isContainerStarted() {
		
		try {
			int time = 0;
			String log = "";
			while (time++ < LOG_RETRY_NUMBER) {			
				if (!isRunning()) {
					logger.warn(String.format("Container %s is not running", containerId));
					return false;
				}
				
				log = dockerClient.logContainer(containerId);
				
				if (log.toLowerCase().contains("mysqld: ready for connections.")) {
					logger.info(String.format("Log for container %s contains the message that mysql is ready, "
							+ "thus container is ready, but still going to sleep just a little bit before returning", containerId));
					Thread.sleep(LOG_RETRY_SLEEP_MS);
					return true;
				}
				
				if (log.toLowerCase().contains("/usr/sbin/mysqld: Shutdown complete".toLowerCase())) {
					String message = String.format("Couldn't start mysql image for container '%s' because mysqld could not start."
							+ "The log from container is: %s", containerId, log);
					logger.error(message);
					throw new RuntimeException(message);
				}
				
				logger.info(String.format("Going to sleep for %d before checking log for container %s again. There are still %d more tries", 
						LOG_RETRY_SLEEP_MS, containerId, LOG_RETRY_NUMBER - time));
				Thread.sleep(LOG_RETRY_SLEEP_MS);
			}
			
			logger.info(String.format("Exiting isContainerStarted for container %s with false because used up "
					+ "all try attempts to read the log and see that mysql is ready", 
					containerId));
			
			logger.info(String.format("Here is the log from container %s: %s", containerId, log));
			
			return false;
		}
		catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void postStartContainer() throws Exception {
		URI dockerUri = new URI(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_DOCKER_URI));
		String scheme = dockerUri.getScheme();
		String host;
		if (scheme != null && scheme.equals("unix")) {
			// if we're communicating through unix socket, getHost won't be defined
			// This could possibly be a generic fallback for when getHost() is null
			host = "127.0.0.1";
		} else {
			host = dockerUri.getHost();
		}
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
		
		public String getPassword() {
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
