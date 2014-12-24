/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.importers.ktr;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTargetDBViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

/**
 * @author Evgeny
 *
 */
public class KTRManagerTest extends DatabaseUnitTestBase {
	Logger logger = LogManager.getLogger(KTRManagerTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	@Test
	public void testCreateTemplate() throws Exception {
		ColfusionSourceinfo story = setUpTestStory(TEST_TARGET_TABLE_NAME, TEST_TARGET_COLUMNS_NAMES);
		
		String folderForKtr = tempFolder.newFolder("testCreateTemplateKTRFolder").toString();

		redefineSystemPropertyForMethod(PropertyKeys.COLFUSION_KTR_FOLDER.getKey(), folderForKtr);
		
		String folderForUpload = tempFolder.newFolder("testCreateTemplateUploadFolder").toString();
		
		FileContentInfoViewModel fileContentInfoViewModel = prepareDileContentInfoViewModel(
				story, folderForUpload);
		
		KTRManager ktrManager = new KTRManager(story.getSid());
		ktrManager.createTemplate(fileContentInfoViewModel);
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(story.getSid());
		
		assertEquals(1, ktrLocations.size());
		
		ktrManager = new KTRManager(story.getSid());
		ktrManager.loadKTR(ktrLocations.get(0));
		
		assertEquals("Sheet1", ktrManager.getTableName());
		
		StoryTargetDBViewModel dbInfo = ktrManager.readTargetDatabaseInfo();
		
		String dbNameExpected = configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_DATABASE_NAME_PREFIX) + story.getSid();
		String dbNameActual =  dbInfo.getDatabaseName();
		
		assertEquals(dbNameExpected, dbNameActual);
		assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_VENDOR), dbInfo.getDriver());
		assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PASSWORD), dbInfo.getPassword());
		assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PORT), String.valueOf(dbInfo.getPort()));
		assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_HOST), dbInfo.getServerAddress());
		assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_USERNAME), dbInfo.getUserName());			
	}

	/**
	 * @param story
	 * @param folderForUpload
	 * @return
	 */
	private FileContentInfoViewModel prepareDileContentInfoViewModel(
			final ColfusionSourceinfo story, final String folderForUpload) {
		FileContentInfoViewModel fileContentInfoViewModel = new FileContentInfoViewModel();
		
		String testExcelFileName = edu.pitt.sis.exp.colfusion.tests.TestResourcesNames.TEST_EXCEL_FILE_XLSX;
		
		String uploadFileAbsolutePath = folderForUpload + File.separator + story.getSid(); 
		
		String textExcelFileNameLocation =  uploadFileAbsolutePath + File.separator + testExcelFileName;
		
		fileContentInfoViewModel.setFileAbsoluteName(textExcelFileNameLocation);
		fileContentInfoViewModel.setExtension("xlsx");
		fileContentInfoViewModel.setFileName(testExcelFileName);
		
		ArrayList<WorksheetViewModel> worksheets = new ArrayList<>();
		WorksheetViewModel testWorksheet = new WorksheetViewModel();
		testWorksheet.setHeaderRow(1);
		testWorksheet.setIndexInTheFile(0);
		testWorksheet.setSheetName("Sheet1");
		testWorksheet.setStartColumn("A");
		
		ArrayList<DatasetVariableViewModel> variables = new ArrayList<>();
		
		DatasetVariableViewModel variable = new DatasetVariableViewModel();
		variable.setChecked(true);
		variable.setChosenName("A");
		variable.setOriginalName("A");
		
		variables.add(variable);
		
		variable = new DatasetVariableViewModel();
		variable.setChecked(true);
		variable.setChosenName("B");
		variable.setOriginalName("B");
		
		variables.add(variable);
		
		testWorksheet.setVariables(variables);
		
		worksheets.add(testWorksheet);
		
		fileContentInfoViewModel.setWorksheets(worksheets);
		return fileContentInfoViewModel;
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
