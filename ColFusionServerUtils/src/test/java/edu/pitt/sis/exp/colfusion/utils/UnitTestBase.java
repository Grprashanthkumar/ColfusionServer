package edu.pitt.sis.exp.colfusion.utils;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 * Some functionality that is commonly used by many unit tests.
 * 
 * @author Evgeny
 *
 */
public abstract class UnitTestBase  {
	
	@Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
	
	private static final Set<String> systemPropertiesToCleanAfterClass = new HashSet<String>();
	private final Set<String> systemPropertiesToCleanAfterMethod = new HashSet<String>();
	
	/**
	 * Get resource by given name and returns its URI as string.
	 * 
	 * @param resourceName
	 * 		the name of the resource.
	 * @return the absolute location of the file.
	 * Note: the file might be inside of a jar archive, thus not suitable as input to new File().
	 */
	protected String getResourceAsAbsoluteURI(final String resourceName) {
		return ResourceUtils.getResourceAsFileLocation(this.getClass(), resourceName);
	}
	
	protected InputStream getResourceAsStream(final String resourceName) {
		return ResourceUtils.getResourceAsStream(this.getClass(), resourceName);
	}

	@BeforeClass
	public static void prepareUp() throws Exception {
		//TODO find a better way to do that because currently the same properties
		// are reloaded foreach test class.
		ConfigManager.getInstance().loadTestProperties();
	}
	
	@After
	public void afterMethod() throws Exception {
		for (String sysProperty : systemPropertiesToCleanAfterMethod) {
			System.clearProperty(sysProperty);
		};
	}
	
	@AfterClass
	public static void afterClass() throws Exception {
		for (String sysProperty : systemPropertiesToCleanAfterClass) {
			System.clearProperty(sysProperty);
		};
	}
	
	/**
	 * Sets provided system property to provided value. The property will be cleaned after the test method.
	 * 
	 * @param propertyName
	 * 		the name of the property to set.
	 * @param value
	 * 		the value to set.
	 */
	protected void redefineSystemPropertyForMethod(final String propertyName, final String value) {
		System.setProperty(propertyName, value);
		systemPropertiesToCleanAfterMethod.add(propertyName);
	}
	
	/**
	 * Sets provided system property to provided value. The property will be cleaned after all tests in the class run.
	 * 
	 * @param propertyName
	 * 		the name of the property to set.
	 * @param value
	 * 		the value to set.
	 */
	protected static void redefineSystemPropertyForClass(final String propertyName, final String value) {
		System.setProperty(propertyName, value);
		systemPropertiesToCleanAfterClass.add(propertyName);
	}
	
	/**
	 * Check if OS is windows or not.
	 * @return true if OS is Windows, false otherwise.
	 */
	protected boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		
		if (os.indexOf("win") >= 0) {
			return true;
		}
		
		return false;
	}
	
	protected void assertEqualsIgnoreWhiteSpaces(final String message, 
			final String expected, final String actual) {
		assertEquals(message, expected.replaceAll("\\s+",""), actual.replaceAll("\\s+",""));	
	}
}
