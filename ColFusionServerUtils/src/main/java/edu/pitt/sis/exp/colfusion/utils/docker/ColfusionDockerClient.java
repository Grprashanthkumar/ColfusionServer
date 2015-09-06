package edu.pitt.sis.exp.colfusion.utils.docker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.jaxrs.DockerCmdExecFactoryImpl;

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
//			    .withDockerCertPath(configMng.getProperty(PropertyKeys.COLFUSION_DOCKER_CERT_PATH))
			    .build();
				
		DockerCmdExecFactoryImpl dockerCmdExecFactory = new DockerCmdExecFactoryImpl()
				  .withReadTimeout(1000)
				  .withConnectTimeout(1000);
		
		DockerClient dockerClient = DockerClientBuilder
				.getInstance(config)
				.withDockerCmdExecFactory(dockerCmdExecFactory)
				.build();
		
		logger.info("Done initializing docker client");
		
		return dockerClient;
	}

	public String createContainer(final String imageName,
			final String tag, final PairOf<String, String>[] envVariables) throws IOException {
		
		pullImage(imageName, tag);
		
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

	public void pullImage(final String imageName, final String tag) throws IOException {
		dockerClient.pullImageCmd(imageName)
				.withTag(tag)
				.exec(new PullImageResultCallback()).awaitSuccess();
	}

	public void startContainer(final String containerId) {
		dockerClient.startContainerCmd(containerId)
		   .exec();
	}

	public void stopContainer(final String containerId) {
		dockerClient.stopContainerCmd(containerId).exec();
	}
	
	public void deleteContainer(final String containerId) {
		dockerClient.removeContainerCmd(containerId).exec();
	}
	
	public String logContainer(final String containerId) throws InterruptedException {
		LogContainerResultCallback logContainerResult = new LogContainerResultCallback();
		
		LogContainerResultCallback io = dockerClient.logContainerCmd(containerId)
				.withStdOut(true)
				.withStdErr(true)
				.withTailAll()
				.withFollowStream(true)
				.exec(logContainerResult).awaitCompletion();
		
		return io.toString();
	}

	public InspectContainerResponse inspectContainer(final String containerId) {
		InspectContainerResponse inspectResponse = dockerClient.inspectContainerCmd(containerId).exec();
		
		return inspectResponse;
	}
}
