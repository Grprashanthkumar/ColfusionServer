/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.persistence.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.importers.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.tests.Utils;
import junit.framework.TestCase;

/**
 * @author Evgeny
 *
 */
public class SourceInfoManagerTest extends TestCase {
	
	Logger logger = LogManager.getLogger(SourceInfoManagerTest.class.getName());
	
	public SourceInfoManagerTest(String name) {
		super(name);
	}
	
	
	/**
	 * Test if the Excel file from the test/resources is written to specified in the properties location.
	 */
	public void testFindDatasetInfoBySid() {
			
		int sid = 0;
		
		try {
			sid = Utils.getTestSid();
		} catch (Exception e) {
			logger.error("testFindDatasetInfoBySid failed", e);
			
			fail("testFindDatasetInfoBySid failed");
		}
		
		SourceInfoManager sourceInfoManager = new SourceInfoManagerImpl();
		 
		ColfusionSourceinfo si = null;
		try {
			si = sourceInfoManager.findByID(sid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			fail();
		}
		
		if (si == null) {
			logger.error("testFindDatasetInfoBySid failed. Could not find sotry by sid " + sid);
			
			fail("testFindDatasetInfoBySid failed. Could not find sotry by sid " + sid);
		}
		 	
		assertEquals(String.valueOf(sid), si.getSid().toString());
		assertEquals("UnitTestStory", si.getTitle());
		
		assertEquals(DataSourceTypes.DATA_FILE.getValue(), si.getSourceType());
	}	
}