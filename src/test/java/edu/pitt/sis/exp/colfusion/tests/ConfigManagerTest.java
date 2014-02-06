package edu.pitt.sis.exp.colfusion.tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import junit.framework.TestCase;

public class ConfigManagerTest extends TestCase {
	
	Logger logger = LogManager.getLogger(ConfigManagerTest.class.getName());
	
	public void testGetParameterValueExisting() {
		ConfigManager configManager = ConfigManager.getInstance();
		
		logger.warn("Testing log.");
		
		String testValue = configManager.getPropertyByName("test");
		
		 assertEquals("test", testValue);
	}	
}
