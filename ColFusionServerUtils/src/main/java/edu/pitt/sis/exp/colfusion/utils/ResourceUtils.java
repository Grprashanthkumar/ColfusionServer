package edu.pitt.sis.exp.colfusion.utils;

import java.io.InputStream;

public class ResourceUtils {
	public static InputStream getResourceAsStream(final Class<?> clazz, final String resourceName) {
		return clazz.getClassLoader().getResourceAsStream(resourceName);
	}
}
