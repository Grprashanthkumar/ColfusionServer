/**
 * 
 */
package edu.pitt.sis.exp.colfusion.utils.docker.containers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports.Binding;

import edu.pitt.sis.exp.colfusion.utils.docker.ColfusionDockerClient;
import edu.pitt.sis.exp.colfusion.utils.docker.containerProviders.AbstractDockerContainerProvider;

/**
 * @author Evgeny
 *
 */
public abstract class AbstractDockerContainer {
	
	protected final Logger logger = LogManager.getLogger(getClass().getName());
	
	protected final String containerId;
	protected final ColfusionDockerClient dockerClient;
	protected final AbstractDockerContainerProvider<? extends AbstractDockerContainer> containerProvider;
	protected InspectContainerResponse inspectResponse;
	
	public AbstractDockerContainer(final String containerId, final ColfusionDockerClient dockerClient, 
			final AbstractDockerContainerProvider<? extends AbstractDockerContainer> containerProvider) {
		this.containerId = containerId;
		this.dockerClient = dockerClient;
		this.containerProvider = containerProvider;
	}
	
	public void start() throws Exception {
		dockerClient.startContainer(containerId);
		
		if (isContainerStarted()) {
			inspectResponse = dockerClient.inspectContainer(containerId);
			postStartContainer();
		}
		else {
			throw new RuntimeException("Faile to start container " + containerId);
		}
	}

	public boolean isRunning() {
		InspectContainerResponse inspectResponse = dockerClient.inspectContainer(containerId);
		
		if (inspectResponse == null) {
			return false;
		}
		
		return inspectResponse.getState().isRunning(); 
	}
	
	public void stop() {
		dockerClient.stopContainer(containerId);
	}
	
	public void delete() {
		dockerClient.deleteContainer(containerId);
	}
	
	protected int getHostPort(final int port) {
		if (inspectResponse == null) {
			String message = String.format("Could not get host port for container port %d. "
					+ "Seems like container with id '%s' is not started. Call startContainer first.", 
					port, containerId);
			
			logger.error(message);
			throw new RuntimeException(message);
		}
		
		Binding[] bindings = inspectResponse.getNetworkSettings().getPorts().getBindings().get(ExposedPort.tcp(port));
		
		if (bindings == null || bindings.length == 0) {
			String message = String.format("Could not get host port for container port %d and container id '%s'."
					+ "getNetworkSettings().getPorts().getBindings().get(containerPort) returned null or empty array from this"
					+ "inspect json: %s", 
					port, containerId, inspectResponse.toString());
			
			logger.error(message);
			throw new RuntimeException(message);
		}
		
		//TODO not should why bindings is an array, just get the first one		
		return bindings[0].getHostPort();
	}
	
	protected abstract void postStartContainer() throws Exception;

	protected abstract boolean isContainerStarted();
}
