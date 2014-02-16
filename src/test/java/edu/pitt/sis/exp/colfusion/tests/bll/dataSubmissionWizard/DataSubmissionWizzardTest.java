package edu.pitt.sis.exp.colfusion.tests.bll.dataSubmissionWizard;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzard;
import edu.pitt.sis.exp.colfusion.models.AcceptedFilesResponseModel;
import edu.pitt.sis.exp.colfusion.models.OneUploadedItem;
import edu.pitt.sis.exp.colfusion.tests.PropertyKeysTest;

public class DataSubmissionWizzardTest extends TestCase {
	
	Logger logger = LogManager.getLogger(DataSubmissionWizzardTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	/**
	 * Test Store Uploaded Files
	 */
	public void testStoreUploadedFiles() {
		
		
		String testFileName = configManager.getPropertyByName(PropertyKeysTest.testExcelFileNameInResourceFolder);
		String testArchiveFileName = configManager.getPropertyByName(PropertyKeysTest.testZipArchive);
		
		InputStream testFileNameIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(testFileName);
		InputStream testArchiveFileIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(testArchiveFileName);
		
		Map<String, InputStream> inputStreams = new HashMap<String, InputStream>();
							
		inputStreams.put(testFileName, testFileNameIS);
		inputStreams.put(testArchiveFileName, testArchiveFileIS);
		
		
		DataSubmissionWizzard wizardBLL = new DataSubmissionWizzard();
		AcceptedFilesResponseModel result = wizardBLL.storeUploadedFiles("test", "123", "fileType", "dbType", inputStreams);
		
    	assertEquals(true, result.IsSuccessful);
    	assertEquals("no errors", result.Message);
    	assertEquals(2, result.Payload.size());
    	
    	OneUploadedItem filesFromArray = result.Payload.get(0);
    	
    	assertEquals(2, filesFromArray.files.size());
	}
}
