package edu.pitt.sis.exp.colfusion.tests.bll.dataSubmissionWizard;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzard;
import edu.pitt.sis.exp.colfusion.models.GeneralResponseModel;
import edu.pitt.sis.exp.colfusion.tests.PropertyKeysTest;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

public class DataSubmissionWizzardTest extends TestCase {
	
	Logger logger = LogManager.getLogger(DataSubmissionWizzardTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	private Response makeCORS(ResponseBuilder req) {
		   ResponseBuilder rb = req.header("Access-Control-Allow-Origin", "*")
		      .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

		   //if (!"".equals(returnMethod)) {
		      rb.header("Access-Control-Allow-Headers", "Content-Type, Accept"); //returnMethod);
		   //}

		   return rb.build();
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
		
		
		DataSubmissionWizzard wizardBLL = new DataSubmissionWizzard();
    	GeneralResponseModel result = wizardBLL.storeUploadedFiles("test", "123", "fileType", "dbType", inputStreams);
		
    	assertEquals(true, result.IsSuccessful);
    	assertEquals("no errors", result.Message);
    	assertEquals(2, result.PayLoad.size());
    	
    	ArrayList<Object> payload = (ArrayList<Object>)result.PayLoad;
    	@SuppressWarnings("unchecked")
		List<IOUtilsStoredFileInfoModel> filesFromArray = (List<IOUtilsStoredFileInfoModel>)payload.get(0);
    	
    	assertEquals(2, filesFromArray.size());
	}
}
