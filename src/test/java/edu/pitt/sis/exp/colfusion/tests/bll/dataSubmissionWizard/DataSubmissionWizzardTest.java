package edu.pitt.sis.exp.colfusion.tests.bll.dataSubmissionWizard;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzardBL;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.tests.PropertyKeysTest;
import edu.pitt.sis.exp.colfusion.tests.Utils;
import edu.pitt.sis.exp.colfusion.viewmodels.OneUploadedItemViewModel;

public class DataSubmissionWizzardTest extends TestCase {
	
	Logger logger = LogManager.getLogger(DataSubmissionWizzardTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	public DataSubmissionWizzardTest(String name) {
		super(name);
	}
	
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
		
		
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		int sid = 0;
		
		try {
			sid = Utils.getTestSid();
		} catch (Exception e) {
			logger.error("getTestSid failed", e);
			
			fail("prepareDatabase failed");
		}
		
		AcceptedFilesResponse result = wizardBLL.storeUploadedFiles(String.valueOf(sid), "123", "fileType", "dbType", inputStreams);
		
    	assertEquals(true, result.isSuccessful);
    	assertEquals("Files are uploaded successfully", result.message);
    	assertEquals(2, result.getPayload().size());
    	
    	OneUploadedItemViewModel filesFromArray = result.getPayload().get(0);
    	
    	assertEquals(2, filesFromArray.getFiles().size());
	}
}
