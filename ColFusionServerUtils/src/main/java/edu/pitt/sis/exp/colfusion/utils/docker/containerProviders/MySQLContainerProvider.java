/**
 * 
 */
package edu.pitt.sis.exp.colfusion.utils.docker.containerProviders;

import edu.pitt.sis.exp.colfusion.utils.PairOf;
import edu.pitt.sis.exp.colfusion.utils.docker.ColfusionDockerClient;
import edu.pitt.sis.exp.colfusion.utils.docker.containers.MySQLDockerContainer;

/**
 * @author Evgeny
 *
 */
public class MySQLContainerProvider extends AbstractDockerContainerProvider<MySQLDockerContainer> {

	public static final int MYSQL_PORT_DEFAULT = 3306;
	public static final String DOCKER_MYSQL_IMAGE_NAME = "mysql";
	public static final String DOCKER_MYSQL_ROOT_USER = "root";
	public static final String DOCKER_ENV_MYSQL_ROOT_PASSWORD = "MYSQL_ROOT_PASSWORD";
	public static final String DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE = "root";
	
	public MySQLContainerProvider(final ColfusionDockerClient dockerClient) {
		super(dockerClient);
	}

	@Override
	protected MySQLDockerContainer createContainerInternal(final String containerId, final ColfusionDockerClient dockerClient) {
		return new MySQLDockerContainer(containerId, dockerClient, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PairOf<String, String>[] getEnvVariables() {		
		return new PairOf[] {
				new PairOf<String, String>(DOCKER_ENV_MYSQL_ROOT_PASSWORD, DOCKER_ENV_MYSQL_ROOT_PASSWORD_VALUE)
		};
	}

	@Override
	public String getImageName() {
		return DOCKER_MYSQL_IMAGE_NAME;
	}
}
