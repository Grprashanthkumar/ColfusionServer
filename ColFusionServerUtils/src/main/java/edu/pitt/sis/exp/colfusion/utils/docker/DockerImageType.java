package edu.pitt.sis.exp.colfusion.utils.docker;

import edu.pitt.sis.exp.colfusion.utils.docker.containerProviders.AbstractDockerContainerProvider;
import edu.pitt.sis.exp.colfusion.utils.docker.containerProviders.MySQLContainerProvider;

public enum DockerImageType {
	MYSQL_IMAGE(MySQLContainerProvider.class);
	
	Class<? extends AbstractDockerContainerProvider> containerProviderClazz;
	
	DockerImageType(final Class<? extends AbstractDockerContainerProvider> containerProviderClazz) {
		this.containerProviderClazz = containerProviderClazz;
	}

	public Class<? extends AbstractDockerContainerProvider> getContainerProviderClass() {
		return containerProviderClazz;
	}
}
