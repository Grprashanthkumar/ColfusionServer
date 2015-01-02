package edu.pitt.sis.exp.colfusion.tests.bll.dataSubmissionWizard;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;

public class DataSubmissionWizzardTest extends TestCase {
	
	Logger logger = LogManager.getLogger(DataSubmissionWizzardTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	public DataSubmissionWizzardTest(final String name) {
		super(name);
	}
	
	//TODO: redo DataSubmissionWizzardTest.testStoreUploadedFiles
	/**
	 * Test Store Uploaded Files
	 */
	@Ignore
	@Test
	public void testStoreUploadedFiles() {
//		String testFileName = configManager.getProperty(PropertyKeysTest.testExcelFileNameInResourceFolder);
//		String testArchiveFileName = configManager.getProperty(PropertyKeysTest.testZipArchive);
//		
//		InputStream testFileNameIS = ResourceUtils.getResourceAsStream(this.getClass(), testFileName);
//		InputStream testArchiveFileIS = ResourceUtils.getResourceAsStream(this.getClass(), testArchiveFileName);
//		
//		Map<String, InputStream> inputStreams = new HashMap<String, InputStream>();
//							
//		inputStreams.put(testFileName, testFileNameIS);
//		inputStreams.put(testArchiveFileName, testArchiveFileIS);
//		
//		
//		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
//		
//		int sid = 0;
//		
//		try {
//			sid = Utils.getTestSid();
//		} catch (Exception e) {
//			logger.error("getTestSid failed", e);
//			
//			try {
//				testFileNameIS.close();
//				testArchiveFileIS.close();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block		
//			}
//			
//			fail("prepareDatabase failed");
//		}
//		
//		AcceptedFilesResponse result = wizardBLL.storeUploadedFiles(String.valueOf(sid), "123", "fileType", "dbType", inputStreams);
//		
//    	assertEquals(true, result.isSuccessful);
//    	assertEquals("Files are uploaded successfully", result.message);
//    	assertEquals(2, result.getPayload().size());
//    	
//    	OneUploadedItemViewModel filesFromArray = result.getPayload().get(0);
//    	
//    	assertEquals(2, filesFromArray.getFiles().size());
//    	
//    	try {
//			testFileNameIS.close();
//			testArchiveFileIS.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block	
//		}
	}
}
