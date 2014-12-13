/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.persistence.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;

/**
 * @author Evgeny
 *
 */
//TODO: move to dal project
public class SourceInfoManagerTest extends DatabaseUnitTestBase {
	
	Logger logger = LogManager.getLogger(SourceInfoManagerTest.class.getName());
	
	/**
	 * Test if the Excel file from the test/resources is written to specified in the properties location.
	 */
	@Ignore
	@Test
	public void testFindDatasetInfoBySid() {
			
//		int sid = 0;
//		
//		try {
//			sid = Utils.getTestSid();
//		} catch (Exception e) {
//			logger.error("testFindDatasetInfoBySid failed", e);
//			
//			fail("testFindDatasetInfoBySid failed");
//		}
//		
//		SourceInfoManager sourceInfoManager = new SourceInfoManagerImpl();
//		 
//		ColfusionSourceinfo si = null;
//		try {
//			si = sourceInfoManager.findByID(sid);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			fail();
//		}
//		
//		if (si == null) {
//			logger.error("testFindDatasetInfoBySid failed. Could not find sotry by sid " + sid);
//			
//			fail("testFindDatasetInfoBySid failed. Could not find sotry by sid " + sid);
//		}
//		 	
//		assertEquals(String.valueOf(sid), si.getSid().toString());
//		assertEquals("UnitTestStory", si.getTitle());
//		
//		assertEquals(DataSourceTypes.DATA_FILE.getValue(), si.getSourceType());
	}	
}
