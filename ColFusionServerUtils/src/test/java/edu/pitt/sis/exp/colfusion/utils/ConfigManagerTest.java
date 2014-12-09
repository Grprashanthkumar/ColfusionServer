package edu.pitt.sis.exp.colfusion.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * Test functionality of {@link ConfigManager}
 * @author Evgeny
 *
 */
public class ConfigManagerTest extends TestCase {
	
	private static final String COLFUSION_PROPERTIES_SOURCE_VALUE_DEFAULT = "default";
	private static final String COLFUSION_PROPERTIES_SOURCE_VALUE_CUSTOM = "custom";
	private static final String COLFUSION_PROPERTIES_SOURCE_VALUE_CUSOM_VIA_SYSTEM_PROPERTY = "custom via system properties";
	
	Logger logger = LogManager.getLogger(ConfigManagerTest.class.getName());
	
	public ConfigManagerTest(final String name) {
		super(name);
	}
	
	/**
	 * Tests {@link ConfigManager#loadDefaultPropertiesFoundInResource(Properties)}
	 */
	@Test
	public void testLoadDefaultPropertiesFoundInResource() {
		Properties properties = new Properties();
		
		ConfigManager configManager = ConfigManager.getInstance();
		configManager.loadProperties(); // Because other tests might have messed up with the properties.
		configManager.loadDefaultPropertiesFoundInResource(properties);
		
		assertEquals(COLFUSION_PROPERTIES_SOURCE_VALUE_DEFAULT, properties.getProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE));
	}
	
	/**
	 * Tests {@link ConfigManager#loadCustomPropertiesFoundInResource(Properties)}
	 */
	@Test
	public void testLoadCustomPropertiesFoundInResource() {
		Properties properties = new Properties();
		
		ConfigManager configManager = ConfigManager.getInstance();
		configManager.loadProperties(); // Because other tests might have messed up with the properties.
		configManager.loadCustomPropertiesFoundInResource(properties);
		
		assertEquals(COLFUSION_PROPERTIES_SOURCE_VALUE_CUSTOM, properties.getProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE));
	}
	
	/**
	 * Tests {@link ConfigManager#loadCustomPropertiesProvidedViaSystemProperty(Properties)}
	 * @throws IOException
	 */
	@Test
	public void testLoadCustomPropertiesProvidedViaSystemProperty() throws IOException {
		
		String testValue = String.format("%s = %s", PropertyKeys.COLFUSION_PROPERTIES_SOURCE, COLFUSION_PROPERTIES_SOURCE_VALUE_CUSOM_VIA_SYSTEM_PROPERTY);
		File tempFile = createTestPropertyFile(testValue);
		
		System.setProperty(ConfigManager.CONFIG_FILE_NAME_SYSTEM_PROPERTY, tempFile.toString());
		
		Properties properties = new Properties();
		
		ConfigManager configManager = ConfigManager.getInstance(); // Load properties automatically.
		configManager.loadProperties(); // Because other tests might have messed up with the properties.
		configManager.loadCustomPropertiesProvidedViaSystemProperty(properties);
		
		assertEquals(COLFUSION_PROPERTIES_SOURCE_VALUE_CUSOM_VIA_SYSTEM_PROPERTY, properties.getProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE));
		
		System.clearProperty(ConfigManager.CONFIG_FILE_NAME_SYSTEM_PROPERTY); // cleaning up
		
		tempFile.delete();
	}
	
	/**
	 * Tests {@link ConfigManager#loadProperties()} and indirectly {@link ConfigManager#getProperty(String)}
	 * @throws IOException
	 */
	@Test
	public void testLoadProperties() throws IOException {
		ConfigManager configManager = ConfigManager.getInstance(); // this will also call loadProperties in the ConfigManager
		configManager.loadProperties(); // Because other tests might have messed up with the properties.
		
		// Since the custom config.properties file is present in the src/test/resources then
		// the default PropertyKeys.COLFUSION_PROPERTIES_SORUCE property should be overridden
		
		assertEquals(COLFUSION_PROPERTIES_SOURCE_VALUE_CUSTOM, configManager.getProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE));
		
		// Now set system property to the temp config file and call loadProperties manually to reload the properties.
		
		String testValue = String.format("%s = %s", PropertyKeys.COLFUSION_PROPERTIES_SOURCE, COLFUSION_PROPERTIES_SOURCE_VALUE_CUSOM_VIA_SYSTEM_PROPERTY);
		File tempFile = createTestPropertyFile(testValue);
		System.setProperty(ConfigManager.CONFIG_FILE_NAME_SYSTEM_PROPERTY, tempFile.toString());
		
		configManager.loadProperties();
		
		assertEquals(COLFUSION_PROPERTIES_SOURCE_VALUE_CUSOM_VIA_SYSTEM_PROPERTY, configManager.getProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE));
		System.clearProperty(ConfigManager.CONFIG_FILE_NAME_SYSTEM_PROPERTY); //cleaning up
		
		tempFile.delete();
	}

	@Test
	public void testGetProperty() {
		ConfigManager configManager = ConfigManager.getInstance(); // this will also call loadProperties in the ConfigManager
		configManager.loadProperties(); // Because other tests might have messed up with the properties.
		
		// Since the custom config.properties file is present in the src/test/resources then
		// the default PropertyKeys.COLFUSION_PROPERTIES_SORUCE property should be overridden
				
		assertEquals(COLFUSION_PROPERTIES_SOURCE_VALUE_CUSTOM, configManager.getProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE));
		
		// Not existing key
		assertTrue(null == configManager.getProperty(PropertyKeys.COLFUSION_PROPERTIES_TEST_NEVER_EXISTING_PROPERTY));
		
		// Not existing key, but a default value is provided.
		String defalutValue = "default value for not existing property";
		assertEquals(defalutValue, configManager.getProperty(PropertyKeys.COLFUSION_PROPERTIES_TEST_NEVER_EXISTING_PROPERTY, defalutValue));
		
		String propertyValueViaSystemProperties = "this value should be returned"; 
		
		// A system property provided to JSV outside of any property file
		// and that property was never read from any other properties file
		System.setProperty(PropertyKeys.COLFUSION_PROPERTIES_TEST_NEVER_EXISTING_PROPERTY, propertyValueViaSystemProperties);
		
		assertEquals(propertyValueViaSystemProperties, configManager.getProperty(PropertyKeys.COLFUSION_PROPERTIES_TEST_NEVER_EXISTING_PROPERTY));
		System.clearProperty(PropertyKeys.COLFUSION_PROPERTIES_TEST_NEVER_EXISTING_PROPERTY); // cleaning up
		
		// A system property provided to JVM outside of any property file
		// The property should override property with the same name but loaded form any file.
		System.setProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE, propertyValueViaSystemProperties);
		
		assertEquals(propertyValueViaSystemProperties, configManager.getProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE));
		System.clearProperty(PropertyKeys.COLFUSION_PROPERTIES_SOURCE); // cleaning up
	}
	
	/**
	 * Creates temporary file and writes a given string into it.
	 * 
	 * @param value
	 * 		The String value to write to the file.
	 * @return 
	 * 		The temporary file.
	 * @throws IOException
	 */
	private File createTestPropertyFile(final String value) throws IOException {
		File tempFile = File.createTempFile("colfusion_ConfigManagerTest", ".properties");
		
		Writer writer = new FileWriter(tempFile);
		writer.write(value);
		writer.flush();
		writer.close();
		return tempFile;
	}
}
