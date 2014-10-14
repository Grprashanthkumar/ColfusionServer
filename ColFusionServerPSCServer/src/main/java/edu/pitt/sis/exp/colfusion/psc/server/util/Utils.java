package edu.pitt.sis.exp.colfusion.psc.server.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.psc.server.ColfusionPSCServer;
import edu.pitt.sis.exp.colfusion.utils.ResourceUtils;

public class Utils {
	
	private static final Logger logger = LogManager.getLogger(Utils.class.getName());
	
	/**
	 * This class is intended to use be used as static only.
	 */
	private Utils() {}
	
	private final static String CONFIG_FILE_NAME = "config.properties";
	private static Properties properties;
	
	static {
		loadProperties();
	}
	
	private static void loadProperties() {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = ResourceUtils.getResourceAsStream(ColfusionPSCServer.class, CONFIG_FILE_NAME);
			
			prop.load(input);
	 
			properties = prop;
	 
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}
	
	public static String getProperty(final String propertyKey) {
		return properties.getProperty(propertyKey);
	}
	
	public static String getBaseRestURL(final ServerType serverType) {
		String host = Utils.getHost(serverType);
		String port = Utils.getPort(serverType);
		
		return String.format("http://%s:%s/rest", host, port);
	}
	
	public static String getHost(final ServerType serverType) {
		switch (serverType) {
			case FETCHER:
				return Utils.getProperty("colfusion.tablefetcher.host");
				
			case JOINER:
				return Utils.getProperty("colfusion.tablejoiner.host");
				
			default:
				String message = String.format("Unsupported server type '%s'", serverType);
				logger.error(message);
				throw new IllegalArgumentException(message);
		}
	}

	public static String getPort(final ServerType serverType) {
		
		switch (serverType) {
			case FETCHER:
				return Utils.getProperty("colfusion.tablefetcher.port");
				
			case JOINER:
				return Utils.getProperty("colfusion.tablejoiner.port");
				
			default:
				String message = String.format("Unsupported server type '%s'", serverType);
				logger.error(message);
				throw new IllegalArgumentException(message);
		}
	}
}
