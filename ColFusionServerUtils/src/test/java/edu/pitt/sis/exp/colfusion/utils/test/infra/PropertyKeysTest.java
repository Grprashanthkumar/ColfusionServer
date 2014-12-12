package edu.pitt.sis.exp.colfusion.utils.test.infra;

import edu.pitt.sis.exp.colfusion.utils.IPropertyKeys;

public enum PropertyKeysTest implements IPropertyKeys {
	
	COLFUSION_DOCKER_VERSION ("colfusion.docker.version"),
	
	COLFUSION_DOCKER_URI ("colfusion.docker.uri"),
	
	COLFUSION_DOCKER_SERVER_ADDRESS ("colfusion.docker.server_address"),
	
	COLFUSION_DOCKER_CERT_PATH ("colfusion.docker.cert_path");

	private final String propertyKey;
	
	PropertyKeysTest(final String propertyKey) {
		this.propertyKey = propertyKey;
	}
	
	@Override
	public String getKey() {
		return propertyKey;
	}

	
}
