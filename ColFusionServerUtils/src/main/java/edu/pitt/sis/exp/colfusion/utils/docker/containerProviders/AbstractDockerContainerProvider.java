package edu.pitt.sis.exp.colfusion.utils.docker.containerProviders;

import edu.pitt.sis.exp.colfusion.utils.PairOf;
import edu.pitt.sis.exp.colfusion.utils.docker.ColfusionDockerClient;
import edu.pitt.sis.exp.colfusion.utils.docker.containers.AbstractDockerContainer;


public abstract class AbstractDockerContainerProvider<DockerContainer extends AbstractDockerContainer> {
	protected final ColfusionDockerClient dockerClient;
	
	public AbstractDockerContainerProvider(final ColfusionDockerClient dockerClient) {
		this.dockerClient = dockerClient;
	}

	public DockerContainer createContainer() {
		
		String containerId = dockerClient.createContainer(getImageName(), getEnvVariables());
		
		return createContainerInternal(containerId);
	}

	public abstract PairOf<String, String>[] getEnvVariables();

	public abstract String getImageName();

	protected abstract DockerContainer createContainerInternal(String containerId);
}
