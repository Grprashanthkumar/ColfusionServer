/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.importers.ktr;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;

/**
 * @author Evgeny
 *
 */
public class KTRManagerTest extends DatabaseUnitTestBase {
	Logger logger = LogManager.getLogger(KTRManagerTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	@Test
	public void test() {
		
	}
	
	//TODO: test different parts of KTRManager and most of it should not depend on the database.
	@Ignore
	@Test
	public void testCreateKTR() {
		
//		int sid = 0;
//		
//		try {
//			sid = prepareDatabase();
//		} catch (Exception e1) {
//			logger.error("prepareDatabase failed", e1);
//			
//			fail("prepareDatabase failed");
//		}
//		
//		KTRManager ktrManager = new KTRManager(sid);
//		
//		FileContentInfoViewModel fileContentInfoViewModel = new FileContentInfoViewModel();
//		
//		String textExcelFileName = configManager.getProperty(PropertyKeysTest.testExcelFileNameInResourceFolder);
//		//This depends on the fact that the file is already copied to the uplaod_row_data folder.
//		
//		String uploadFilesLocation = IOUtils.getAbsolutePathInColfutionRoot(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_UPLOAD_FILES_FOLDER));
//		String uploadFileAbsolutePath = uploadFilesLocation + File.separator + sid; 
//		
//		String textExcelFileNameLocation =  uploadFileAbsolutePath + File.separator + textExcelFileName;
//		
//		File testFile = new File(textExcelFileNameLocation);
//		
//		fileContentInfoViewModel.setFileAbsoluteName(testFile.getAbsolutePath());
//		fileContentInfoViewModel.setExtension("xlsx");
//		fileContentInfoViewModel.setFileName(testFile.getName());
//		
//		ArrayList<WorksheetViewModel> worksheets = new ArrayList<>();
//		WorksheetViewModel testWorksheet = new WorksheetViewModel();
//		testWorksheet.setHeaderRow(1);
//		testWorksheet.setIndexInTheFile(0);
//		testWorksheet.setSheetName("Sheet1");
//		testWorksheet.setStartColumn("A");
//		
//		ArrayList<DatasetVariableViewModel> variables = new ArrayList<>();
//		
//		DatasetVariableViewModel variable = new DatasetVariableViewModel();
//		variable.setChecked(true);
//		variable.setChosenName("A");
//		variable.setOriginalName("A");
//		
//		variables.add(variable);
//		
//		variable = new DatasetVariableViewModel();
//		variable.setChecked(true);
//		variable.setChosenName("B");
//		variable.setOriginalName("B");
//		
//		variables.add(variable);
//		
//		testWorksheet.setVariables(variables);
//		
//		worksheets.add(testWorksheet);
//		
//		fileContentInfoViewModel.setWorksheets(worksheets);
//		
//		
//		try {
//			ktrManager.createTemplate(fileContentInfoViewModel);
//			
//			SourceInfoManager storyMgr = new SourceInfoManagerImpl();
//			ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(sid);
//			
//			assertEquals(1, ktrLocations.size());
//			
//			ktrManager = new KTRManager(sid);
//			ktrManager.loadKTR(ktrLocations.get(0));
//			
//			assertEquals("Sheet1", ktrManager.getTableName());
//			
//			StoryTargetDBViewModel dbInfo = ktrManager.readTargetDatabaseInfo();
//			
//			String dbNameExpected = configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_DATABASE_NAME_PREFIX) + sid;
//			String dbNameActual =  dbInfo.getDatabaseName();
//			
//			assertEquals(dbNameExpected, dbNameActual);
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_VENDOR), dbInfo.getDriver());
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PASSWORD), dbInfo.getPassword());
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PORT), String.valueOf(dbInfo.getPort()));
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_HOST), dbInfo.getServerAddress());
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_USERNAME), dbInfo.getUserName());
//			
//		} catch (IOException e) {
//			logger.error("testCreateKTR failed!", e);
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (XPathExpressionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
//	private int prepareDatabase() throws Exception {
//		int sid = Utils.getTestSid();
//		
//		//TODO: again depend on other test, BAD.
//		DataSubmissionWizzardTest dataSubmissionTest = new DataSubmissionWizzardTest("");
//		dataSubmissionTest.testStoreUploadedFiles();
//		
//		return sid;
//	}
}
