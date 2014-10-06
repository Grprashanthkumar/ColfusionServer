package edu.pitt.sis.exp.colfusion.utils;

import java.io.InputStream;

public class ResourceUtils {
	public static InputStream getResourceAsStream(final String resourceName) {
		return ResourceUtils.class.getClassLoader().getResourceAsStream(resourceName);
	}
}
