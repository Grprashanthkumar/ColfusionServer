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
	
	static Map<DockerImageType, AbstractDockerContainerProvider> initializedContainerProviders = new HashMap<DockerImageType, AbstractDockerContainerProvider>();
	
	public static AbstractDockerContainer createContainer(final DockerImageType imageType) {
		AbstractDockerContainerProvider containerProvider = initializedContainerProviders.get(imageType);
		
		if (containerProvider == null) {
			containerProvider = initializeContainerProvider(imageType);
		}
		
		return containerProvider.createContainer();
	}

	private static AbstractDockerContainerProvider initializeContainerProvider(
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
