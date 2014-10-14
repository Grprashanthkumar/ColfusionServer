/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.importers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.importers.Importer;
import edu.pitt.sis.exp.colfusion.importers.ImporterFactory;
import edu.pitt.sis.exp.colfusion.importers.ImporterType;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeysTest;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 * @author Evgeny
 *
 */
public class ImporterFactoryTest extends TestCase {
	
	Logger logger = LogManager.getLogger(ImporterFactoryTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	public ImporterFactoryTest(final String name) {
		super(name);
	}
	
	private Importer getImporter(final ImporterType importerType) throws Exception {
		try {
			return ImporterFactory.getImporter(importerType);
		} catch (Exception e1) {
			
			logger.error("getImporter failed " + importerType.toString(), e1);
			throw e1;
		}
	}
	
	private Collection<WorksheetViewModel> getCSVWorksheets() throws Exception {
		Importer importer = getImporter(ImporterType.CSVImporter);
		
		String testFileName = configManager.getPropertyByName(PropertyKeysTest.testCSVFileNameInResourceFolder);
				
		String testFileNameAbsolute = this.getClass().getResource(testFileName).getFile();
		
		IOUtilsStoredFileInfoModel fileModel = new IOUtilsStoredFileInfoModel();
		fileModel.setAbsoluteFileName(testFileNameAbsolute);
		fileModel.setFileExtension("csv");
		fileModel.setFileName(testFileName);
	
		try {
			Collection<WorksheetViewModel> tables = importer.getTables(fileModel);
			
			return tables;
		} catch (Exception e) {
			
			logger.error("getTablesTest failed on importer.getTables(fileModel)", e);
			throw e;
		}
	}
	
	private Collection<WorksheetViewModel> getExcelWorksheets() throws Exception {
		Importer importer = getImporter(ImporterType.ExcelImporter);
		
		String testFileName = configManager.getPropertyByName(PropertyKeysTest.testExcelFileNameInResourceFolder);
				
		String testFileNameAbsolute = this.getClass().getResource(testFileName).getFile();
		
		IOUtilsStoredFileInfoModel fileModel = new IOUtilsStoredFileInfoModel();
		fileModel.setAbsoluteFileName(testFileNameAbsolute);
		fileModel.setFileExtension("xlsx");
		fileModel.setFileName(testFileName);
	
		try {
			Collection<WorksheetViewModel> tables = importer.getTables(fileModel);
			
			return tables;
		} catch (Exception e) {
			
			logger.error("getTablesTest failed on importer.getTables(fileModel)", e);
			throw e;
		}
	}
	
	public void testGetTablesCSVFile() {
		
		try {
			Collection<WorksheetViewModel> tables = getCSVWorksheets();
			
			assertEquals(1, tables.size());
			
			WorksheetViewModel worksheet = tables.iterator().next();
			
			String testFileName = configManager.getPropertyByName(PropertyKeysTest.testCSVFileNameInResourceFolder);
			assertEquals(testFileName, worksheet.getSheetName());
			
			assertEquals(1, worksheet.getHeaderRow());
			assertEquals("A", worksheet.getStartColumn());
			
		} catch (Exception e) {
			
			logger.error("testGetTablesCSVFile failed", e);
			fail("getTablesTest failed on importer.getTables(fileModel)");
		}
	}
	
	public void testGetTablesExcelFile() {

		try {
			Collection<WorksheetViewModel> tables = getExcelWorksheets();
			
			assertEquals(2, tables.size());
			
			Iterator<WorksheetViewModel> iterator = tables.iterator();
			
			WorksheetViewModel worksheet = iterator.next();
			
			assertEquals("Sheet1", worksheet.getSheetName());
			assertEquals(1, worksheet.getHeaderRow());
			assertEquals("A", worksheet.getStartColumn());
			assertEquals(4, worksheet.getNumberOfRows());
			assertEquals(0, worksheet.getIndexInTheFile());
			
			worksheet = iterator.next();
			
			assertEquals("Sheet2", worksheet.getSheetName());
			assertEquals(1, worksheet.getHeaderRow());
			assertEquals("A", worksheet.getStartColumn());
			assertEquals(4, worksheet.getNumberOfRows());
			assertEquals(1, worksheet.getIndexInTheFile());
			
		} catch (Exception e) {
			
			logger.error("getTablesTest failed on importer.getTables(fileModel)", e);
			fail("getTablesTest failed on importer.getTables(fileModel)");
		}
	}
		
	private HashMap<String, ArrayList<DatasetVariableViewModel>> getVariablesCSV() throws Exception {
		Importer importer = getImporter(ImporterType.CSVImporter);
		
		String testFileName = configManager.getPropertyByName(PropertyKeysTest.testCSVFileNameInResourceFolder);
				
		String testFileNameAbsolute = this.getClass().getResource(testFileName).getFile();
		
		FileContentInfoViewModel fileAndSheetsInfo = new FileContentInfoViewModel();
		fileAndSheetsInfo.setExtension("csv");
		fileAndSheetsInfo.setFileAbsoluteName(testFileNameAbsolute);
		fileAndSheetsInfo.setFileName(testFileName);
		fileAndSheetsInfo.setWorksheets((ArrayList<WorksheetViewModel>)getCSVWorksheets());
		
		try {
			return importer.readVariables(fileAndSheetsInfo);
			
		} catch (Exception e) {
			
			logger.error("getVariablesCSV failed", e);
			throw e;
		}
	}
	
	private HashMap<String, ArrayList<DatasetVariableViewModel>> getVariablesExcel() throws Exception {
		Importer importer = getImporter(ImporterType.ExcelImporter);
		
		String testFileName = configManager.getPropertyByName(PropertyKeysTest.testExcelFileNameInResourceFolder);
				
		String testFileNameAbsolute = this.getClass().getResource(testFileName).getFile();
		
		FileContentInfoViewModel fileAndSheetsInfo = new FileContentInfoViewModel();
		fileAndSheetsInfo.setExtension("xlsx");
		fileAndSheetsInfo.setFileAbsoluteName(testFileNameAbsolute);
		fileAndSheetsInfo.setFileName(testFileName);
		fileAndSheetsInfo.setWorksheets((ArrayList<WorksheetViewModel>)getExcelWorksheets());
		
		try {
			return importer.readVariables(fileAndSheetsInfo);
			
		} catch (Exception e) {
			
			logger.error("getVariablesCSV failed", e);
			throw e;
		}
	}
	
	public void testReadVariablesCSV() {
			
		String testFileName = configManager.getPropertyByName(PropertyKeysTest.testCSVFileNameInResourceFolder);
		
		try {
			HashMap<String, ArrayList<DatasetVariableViewModel>> variables = getVariablesCSV();
			
			assertEquals(1, variables.size());
			
			assertTrue(variables.containsKey(testFileName));
			
			ArrayList<DatasetVariableViewModel> vars = variables.get(testFileName);
			
			assertEquals(2, vars.size());
			
			DatasetVariableViewModel variable = vars.get(0);
			
			assertEquals("cl1", variable.getOriginalName());
			assertEquals("cl1", variable.getChosenName());
			
			variable = vars.get(1);
			
			assertEquals("cl2", variable.getOriginalName());
			assertEquals("cl2", variable.getChosenName());
			
		} catch (Exception e) {
			
			logger.error("testReadVariablesCSV failed on importer.getTables(fileModel)", e);
			fail("testReadVariablesCSV failed on importer.getTables(fileModel)");
		}
	}

	public void testReadVariablesExcel() {
		
		try {
			HashMap<String, ArrayList<DatasetVariableViewModel>> variables = getVariablesExcel();
			
			assertEquals(2, variables.size());
			
			assertTrue(variables.containsKey("Sheet1"));
			
			ArrayList<DatasetVariableViewModel> vars = variables.get("Sheet1");
			
			assertEquals(3, vars.size());
			
			DatasetVariableViewModel variable = vars.get(0);
			
			assertEquals("A", variable.getOriginalName());
			assertEquals("A", variable.getChosenName());
			
			variable = vars.get(1);
			
			assertEquals("B", variable.getOriginalName());
			assertEquals("B", variable.getChosenName());
			
			variable = vars.get(2);
			
			assertEquals("C", variable.getOriginalName());
			assertEquals("C", variable.getChosenName());
			
			vars = variables.get("Sheet2");
			
			assertEquals(2, vars.size());
			
			variable = vars.get(0);
			
			assertEquals("D", variable.getOriginalName());
			assertEquals("D", variable.getChosenName());
			
			variable = vars.get(1);
			
			assertEquals("E", variable.getOriginalName());
			assertEquals("E", variable.getChosenName());
			
		} catch (Exception e) {
			
			logger.error("testReadVariablesExcel failed", e);
			fail("testReadVariablesExcel failed");
		}
	}

	public void testReadWorksheetDataCSV() {
		
		try {
			Importer importer = getImporter(ImporterType.CSVImporter);
			
			String testFileName = configManager.getPropertyByName(PropertyKeysTest.testCSVFileNameInResourceFolder);
			
			String testFileNameAbsolute = this.getClass().getResource(testFileName).getFile();
			
			PreviewFileViewModel previewFileViewModel = new PreviewFileViewModel();
			previewFileViewModel.setFileAbsoluteName(testFileNameAbsolute);
			previewFileViewModel.setFileName(testFileName);
			previewFileViewModel.setPreviewPage(1);
			previewFileViewModel.setPreviewRowsPerPage(20);
			
			
			ArrayList<WorksheetDataViewModel> data = importer.readWorksheetData(previewFileViewModel);
			
			assertEquals(1, data.size());
			
			WorksheetDataViewModel worksheetData = data.get(0);
			
			assertEquals(testFileName, worksheetData.getWorksheetName());
			
			assertEquals(4, worksheetData.getWorksheetData().size());
			
			
			previewFileViewModel.setPreviewPage(2);
			previewFileViewModel.setPreviewRowsPerPage(2);
						
			data = importer.readWorksheetData(previewFileViewModel);
			
			assertEquals(1, data.size());
			
			worksheetData = data.get(0);
			
			assertEquals(testFileName, worksheetData.getWorksheetName());
			
			assertEquals(2, worksheetData.getWorksheetData().size());
			
			
			
			previewFileViewModel.setPreviewPage(20);
			previewFileViewModel.setPreviewRowsPerPage(2);
						
			data = importer.readWorksheetData(previewFileViewModel);
			
			assertEquals(1, data.size());
			
			worksheetData = data.get(0);
			
			assertEquals(testFileName, worksheetData.getWorksheetName());
			
			assertEquals(0, worksheetData.getWorksheetData().size());
			
		} catch (Exception e) {
			logger.error("testReadWorksheetDataCSV failed", e);
			fail("testReadWorksheetDataCSV failed");
		}
	}
	
	public void testReadWorksheetDataExcel() {
		
		try {
			Importer importer = getImporter(ImporterType.ExcelImporter);
			
			String testFileName = configManager.getPropertyByName(PropertyKeysTest.testExcelFileNameInResourceFolder);
			
			String testFileNameAbsolute = this.getClass().getResource(testFileName).getFile();
			
			PreviewFileViewModel previewFileViewModel = new PreviewFileViewModel();
			previewFileViewModel.setFileAbsoluteName(testFileNameAbsolute);
			previewFileViewModel.setFileName(testFileName);
			previewFileViewModel.setPreviewPage(1);
			previewFileViewModel.setPreviewRowsPerPage(20);
			
			
			ArrayList<WorksheetDataViewModel> data = importer.readWorksheetData(previewFileViewModel);
			
			assertEquals(2, data.size());
			
			WorksheetDataViewModel worksheetData = data.get(0);
			
			assertEquals("Sheet1", worksheetData.getWorksheetName());
			
			assertEquals(4, worksheetData.getWorksheetData().size());
			
			worksheetData = data.get(1);
			
			assertEquals("Sheet2", worksheetData.getWorksheetName());
			
			assertEquals(4, worksheetData.getWorksheetData().size());
			
			
			previewFileViewModel.setPreviewPage(2);
			previewFileViewModel.setPreviewRowsPerPage(2);
						
			data = importer.readWorksheetData(previewFileViewModel);
			
			assertEquals(2, data.size());
			
			worksheetData = data.get(0);
			
			assertEquals("Sheet1", worksheetData.getWorksheetName());
			
			assertEquals(2, worksheetData.getWorksheetData().size());
			
			worksheetData = data.get(1);
			
			assertEquals("Sheet2", worksheetData.getWorksheetName());
			
			assertEquals(2, worksheetData.getWorksheetData().size());
			
			
			
			previewFileViewModel.setPreviewPage(20);
			previewFileViewModel.setPreviewRowsPerPage(2);
						
			data = importer.readWorksheetData(previewFileViewModel);
			
			assertEquals(2, data.size());
			
			worksheetData = data.get(0);
			
			assertEquals("Sheet1", worksheetData.getWorksheetName());
			
			assertEquals(0, worksheetData.getWorksheetData().size());
			
			worksheetData = data.get(1);
			
			assertEquals("Sheet2", worksheetData.getWorksheetName());
			
			assertEquals(0, worksheetData.getWorksheetData().size());
			
		} catch (Exception e) {
			logger.error("testReadWorksheetDataExcel failed", e);
			fail("testReadWorksheetDataExcel failed");
		}
		
		
	}
}
