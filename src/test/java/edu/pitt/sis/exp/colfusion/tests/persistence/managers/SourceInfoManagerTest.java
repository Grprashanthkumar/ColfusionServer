/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.persistence.managers;

import java.text.Format;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import junit.framework.TestCase;

/**
 * @author Evgeny
 *
 */
public class SourceInfoManagerTest extends TestCase {
	
	Logger logger = LogManager.getLogger(SourceInfoManagerTest.class.getName());
	
	/**
	 * Test if the Excel file from the test/resources is written to specified in the properties location.
	 */
	public void testFindDatasetInfoBySid() {
			
		SourceInfoManager sourceInfoManager = new SourceInfoManagerImpl();
		 
		ColfusionSourceinfo si = sourceInfoManager.findBySid(1085, true);
		 	
		assertEquals("1085", si.getSid().toString());
		assertEquals("test", si.getTitle());
		assertEquals("1", si.getColfusionUsers().getUserId().toString());
		assertEquals("register-wrapper/1085.ktr", si.getPath());
		
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(si.getEntryDate());
		assertEquals("2013-09-23 11:40:30", dateString);
		assertEquals("queued", si.getStatus());
		assertEquals("database", si.getSourceType());
	}	
}
