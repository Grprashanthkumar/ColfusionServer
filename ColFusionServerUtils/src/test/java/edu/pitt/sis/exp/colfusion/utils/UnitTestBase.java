package edu.pitt.sis.exp.colfusion.utils;

import java.io.InputStream;

import org.junit.BeforeClass;
/**
 * Some functionality that is commonly used by many unit tests.
 * 
 * @author Evgeny
 *
 */
public abstract class UnitTestBase  {
	
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
}
