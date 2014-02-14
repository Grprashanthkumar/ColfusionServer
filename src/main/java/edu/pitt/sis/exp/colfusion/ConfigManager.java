package edu.pitt.sis.exp.colfusion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
	private static ConfigManager instance = null;
	private Properties properties = null;
	
	final static String configFileLocation = "config.properties";
	
	protected ConfigManager() {
		
	}
	
	public static ConfigManager getInstance() {
		if(instance == null) {
	         instance = new ConfigManager();
	         instance.getProperties();
	      }
	      return instance;
	}
	
	/**
	 * Loads properties from the properties file.
	 */
	private void getProperties() {
		
		if (instance.properties == null) {
			Properties prop = new Properties();
			InputStream input = null;
		 
			try {
		 
				prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileLocation));
		 
				instance.properties = prop;
		 
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Get the configuration property value by the property name
	 * 
	 * @param propertyName for which to get the value
	 * @return the value of the property
	 */
	public String getPropertyByName(String propertyName) {
		return properties.getProperty(propertyName);
	}
}
