/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.importers.ktr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.tests.PropertyKeysTest;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import edu.pitt.sis.exp.colfusion.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.WorksheetViewModel;
import junit.framework.TestCase;

/**
 * @author Evgeny
 *
 */
public class KTRManagerTest extends TestCase {
	Logger logger = LogManager.getLogger(KTRManagerTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	public void testCreateKTR() {
		
		KTRManager ktrManager = new KTRManager();
		
		int sid = 1163;
		
		FileContentInfoViewModel fileContentInfoViewModel = new FileContentInfoViewModel();
		
		String textExcelFileName = configManager.getPropertyByName(PropertyKeysTest.testExcelFileNameInResourceFolder);
		String textExcelFileNameLocation = Thread.currentThread().getContextClassLoader().getResource(textExcelFileName).getFile();
		
		File testFile = new File(textExcelFileNameLocation);
		
		fileContentInfoViewModel.setFileAbsoluteName(testFile.getAbsolutePath());
		fileContentInfoViewModel.setExtension("xlsx");
		fileContentInfoViewModel.setFileName(testFile.getName());
		
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
		
		
		try {
			ktrManager.createTemplate(fileContentInfoViewModel, sid);
		} catch (IOException e) {
			logger.error("testCreateKTR failed!", e);
		}
	}
}
