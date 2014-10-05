package edu.pitt.sis.exp.colfusion.utils;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigManagerTest extends TestCase {
	
	Logger logger = LogManager.getLogger(ConfigManagerTest.class.getName());
	
	public ConfigManagerTest(final String name) {
		super(name);
	}
	
	public void testGetParameterValueExisting() {
		ConfigManager configManager = ConfigManager.getInstance();
			
		String testValue = configManager.getPropertyByName(PropertyKeysTest.test);
		
		assertEquals("test", testValue);
	}		
}
