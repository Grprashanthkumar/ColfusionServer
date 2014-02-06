package edu.pitt.sis.exp.colfusion.tests;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import junit.framework.TestCase;

public class ConfigManagerTest extends TestCase {
	
	public void testGetParameterValueExisting() {
		ConfigManager configManager = ConfigManager.getInstance();
		
		String testValue = configManager.getPropertyByName("test");
		
		 assertEquals("test", testValue);
	}	
}
