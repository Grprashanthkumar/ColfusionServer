package edu.pitt.sis.exp.colfusion.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ResourceUtils {
	public static InputStream getResourceAsStream(final Class<?> clazz, final String resourceName) {
		return clazz.getClassLoader().getResourceAsStream(resourceName);
	}
	
	public static File getResourceAsFile(final Class<?> clazz, final String resourceName) {
		URL resourceFile = clazz.getClassLoader().getResource(resourceName);
		
		return resourceFile == null ? null : new File(resourceFile.getFile());
	}
}
