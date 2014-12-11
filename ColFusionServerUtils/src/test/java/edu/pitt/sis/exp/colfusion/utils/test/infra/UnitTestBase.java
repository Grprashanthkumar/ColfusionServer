package edu.pitt.sis.exp.colfusion.utils.test.infra;

import java.io.InputStream;

import junit.framework.TestCase;
import edu.pitt.sis.exp.colfusion.utils.ResourceUtils;

/**
 * Some functionality that is commonly used by many unit tests.
 * 
 * @author Evgeny
 *
 */
public abstract class UnitTestBase extends TestCase {
	
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
}
