package edu.pitt.sis.exp.colfusion;

import java.io.FileInputStream;
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
	
	private void getProperties() {
		
		if (instance.properties == null) {
			Properties prop = new Properties();
			InputStream input = null;
		 
			try {
		 
				input = new FileInputStream(configFileLocation);
		 
				// load a properties file
				prop.load(input);
		 
				instance.properties = prop;
		 
			} catch (IOException ex) {
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
}
