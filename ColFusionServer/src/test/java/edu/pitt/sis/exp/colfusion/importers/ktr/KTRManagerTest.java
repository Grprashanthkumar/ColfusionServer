/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers.ktr;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import edu.pitt.sis.exp.colfusion.utils.StringUtils;

public class KTRManagerTest extends DatabaseUnitTestBase {
	Logger logger = LogManager.getLogger(KTRManagerTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	@Test
	public void testCreateTemplate() throws Exception {
		ColfusionSourceinfo story = setUpTestStory(TEST_TARGET_TABLE_NAME, TEST_TARGET_COLUMNS_NAMES);
		
		String folderForKtr = tempFolder.newFolder("testCreateTemplateKTRFolder").toString();

		redefineSystemPropertyForMethod(PropertyKeys.COLFUSION_KTR_FOLDER.getKey(), folderForKtr);
		
		String folderForUpload = tempFolder.newFolder("testCreateTemplateUploadFolder").toString();
		
		FileContentInfoViewModel fileContentInfoViewModel = prepareFileContentInfoViewModel(
				story, folderForUpload);
		
		KTRManager ktrManager = new KTRManager(story.getSid());
		ktrManager.createTemplate(fileContentInfoViewModel);
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(story.getSid());
		
		assertEquals(1, ktrLocations.size());
		
		ktrManager = new KTRManager(story.getSid());
		ktrManager.loadKTR(ktrLocations.get(0));
		
		assertEquals(StringUtils.makeShortUnique(StringUtils.replaceSpaces("Sheet1")), ktrManager.getTableName());
		
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
	private FileContentInfoViewModel prepareFileContentInfoViewModel(
			final ColfusionSourceinfo story, final String folderForUpload) {
		FileContentInfoViewModel fileContentInfoViewModel = new FileContentInfoViewModel();
		
		String testExcelFileName = edu.pitt.sis.exp.colfusion.infra.TestResourcesNames.TEST_EXCEL_FILE_XLSX;
		
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
}
