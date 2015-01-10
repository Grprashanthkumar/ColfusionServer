package edu.pitt.sis.exp.colfusion.utils.docker.containerProviders;

import java.io.IOException;

import edu.pitt.sis.exp.colfusion.utils.PairOf;
import edu.pitt.sis.exp.colfusion.utils.docker.ColfusionDockerClient;
import edu.pitt.sis.exp.colfusion.utils.docker.containers.AbstractDockerContainer;


public abstract class AbstractDockerContainerProvider<DockerContainer extends AbstractDockerContainer> {
	
	public static final String DOCKER_LATEST_TAG = "latest";
	
	protected final ColfusionDockerClient dockerClient;
	
	public AbstractDockerContainerProvider(final ColfusionDockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

	public DockerContainer createContainer() throws IOException {		
		return createContainer(DOCKER_LATEST_TAG);
	}
	
	public DockerContainer createContainer(final String tag) throws IOException {
		String actualTag = "".equals(tag) ? DOCKER_LATEST_TAG : tag;
		String containerId = dockerClient.createContainer(getImageName(), actualTag, getEnvVariables());
		
		return createContainerInternal(containerId, dockerClient);
	}
	
	public DockerContainer createAndStartContainer() throws Exception {
		return createAndStartContainer(DOCKER_LATEST_TAG);
	}
	
	public DockerContainer createAndStartContainer(final String tag) throws Exception {
		DockerContainer container = createContainer(tag);
		container.start();
		
		return container;
	}

	public abstract PairOf<String, String>[] getEnvVariables();

	public abstract String getImageName();

	protected abstract DockerContainer createContainerInternal(String containerId, ColfusionDockerClient dockerClient);
}
