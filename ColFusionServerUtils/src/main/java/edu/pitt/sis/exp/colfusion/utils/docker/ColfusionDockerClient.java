package edu.pitt.sis.exp.colfusion.utils.docker;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PairOf;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.docker.containerProviders.AbstractDockerContainerProvider;

public final class ColfusionDockerClient {
	
	private static final Logger logger = LogManager.getLogger(ColfusionDockerClient.class.getName());
	
	static ColfusionDockerClient instance = new ColfusionDockerClient();
	
	final DockerClient dockerClient;
	
	ConfigManager configMng = ConfigManager.getInstance();
	
	final Map<String, AbstractDockerContainerProvider<?>> registeredContainerProviders = new HashMap<String, AbstractDockerContainerProvider<?>>();
	
	private ColfusionDockerClient() {
		dockerClient = initDockerClient();
	}
	
	public static ColfusionDockerClient getInstance() {
		return instance;
	}
	
	/**
	 * 
	 */
	private DockerClient initDockerClient() {
		logger.info(String.format("Initializing docker client with version '%s', uri '%s', "
				+ "server address '%s', docker cert path '%s'", 
				configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_VERSION), configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_URI),
				configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_SERVER_ADDRESS), configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_CERT_PATH)));
		
		DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
			    .withVersion(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_VERSION))
			    .withUri(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_URI))
			    .withServerAddress(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_SERVER_ADDRESS))
			    .withDockerCertPath(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_CERT_PATH))
			    .build();
				
		DockerClient dockerClient = DockerClientBuilder.getInstance(config).build();
		
		logger.info("Done initializing docker client");
		
		return dockerClient;
	}

	public String createContainer(final String imageName,
			final PairOf<String, String>[] envVariables) {
		
		CreateContainerCmd createContainerCmd = dockerClient.createContainerCmd(imageName);
		
		if (envVariables != null && envVariables.length > 0) {
			String[] envVariablesParams = new String[envVariables.length];
			for (int i = 0; i < envVariables.length; i++) {
				envVariablesParams[i] = String.format("%s=%s", envVariables[i].getValue1(), envVariables[i].getValue2());
			}
			
			createContainerCmd = createContainerCmd.withEnv(envVariablesParams); // might not need to reassign
		}
		
		CreateContainerResponse container = createContainerCmd.exec();
		
		return container.getId();
	}

	public void startContainer(final String containerId) {
		dockerClient.startContainerCmd(containerId)
		   .withPublishAllPorts(true)
		   .exec();
	}

	public InputStream logContainer(final String containerId) {
		InputStream io = dockerClient.logContainerCmd(containerId)
				.withStdOut(true)
				.withStdErr(true)
				.withTailAll()
				.withFollowStream(true)
				.exec();
		
		return io;
	}

	public InspectContainerResponse inspectContainer(final String containerId) {
		InspectContainerResponse inspectResponse = dockerClient.inspectContainerCmd(containerId).exec();
		
		return inspectResponse;
	}
}
