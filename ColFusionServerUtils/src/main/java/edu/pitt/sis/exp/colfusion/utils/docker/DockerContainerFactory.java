package edu.pitt.sis.exp.colfusion.utils.docker;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import edu.pitt.sis.exp.colfusion.utils.docker.containerProviders.AbstractDockerContainerProvider;
import edu.pitt.sis.exp.colfusion.utils.docker.containers.AbstractDockerContainer;

/**
 * 
 * @author Evgeny
 *
 */
public class DockerContainerFactory {
	
	static ColfusionDockerClient dockerClient = ColfusionDockerClient.getInstance();
	
	static Map<DockerImageType, AbstractDockerContainerProvider<?>> initializedContainerProviders = 
			new HashMap<DockerImageType, AbstractDockerContainerProvider<?>>();
	
	public static AbstractDockerContainer createContainer(final DockerImageType imageType) throws Exception {
		return createOrCreateAndStartInternal(imageType, AbstractDockerContainerProvider.DOCKER_LATEST_TAG, false);
	}
	
	public static AbstractDockerContainer createAndStartContainer(final DockerImageType imageType) throws Exception {
		return createOrCreateAndStartInternal(imageType, AbstractDockerContainerProvider.DOCKER_LATEST_TAG, true);
	}
	
	public static AbstractDockerContainer createContainer(final DockerImageType imageType, final String tag) throws Exception {
		return createOrCreateAndStartInternal(imageType, tag, false);
	}
	
	public static AbstractDockerContainer createAndStartContainer(final DockerImageType imageType, final String tag) throws Exception {
		return createOrCreateAndStartInternal(imageType, tag, true);
	}
	
	private static AbstractDockerContainer createOrCreateAndStartInternal(final DockerImageType imageType, 
			final String tag, final boolean doStart) throws Exception {
		AbstractDockerContainerProvider<?> containerProvider = initializedContainerProviders.get(imageType);
		
		if (containerProvider == null) {
			containerProvider = initializeContainerProvider(imageType);
		}
		
		if (doStart) {
			return containerProvider.createAndStartContainer(tag);
		}
		else {
			return containerProvider.createContainer(tag);
		}
	}

	private static AbstractDockerContainerProvider<?> initializeContainerProvider(
			final DockerImageType imageType) {
		try {
			return imageType.getContainerProviderClass().getConstructor(ColfusionDockerClient.class).newInstance(new Object[] {dockerClient});
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			String message = String.format("Cound not initialize Docker Container Provider from class '%s'", imageType.getContainerProviderClass().getName());
			throw new RuntimeException(message, e);
		}
	}
}
