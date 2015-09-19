package edu.pitt.sis.exp.colfusion.bll.importers.excel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.importers.Importer;
import edu.pitt.sis.exp.colfusion.bll.infra.TestResourcesNames;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.utils.UnitTestBase;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

public class ExcelImporterTest extends UnitTestBase {
	Logger logger = LogManager.getLogger(ExcelImporterTest.class.getName());

	private Importer getExcelImporter() {
		return new ExcelImporter(new ExcelFileHandlerImpl());
	}

	/**
	 * Tests {@link ExcelImporter#readVariables(FileContentInfoViewModel) in {@link #testReadVariables()}
	 *
	 * @param testFileName
	 * @param testFileNameAbsolute
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, ArrayList<DatasetVariableViewModel>> getVariablesExcel(final String testFileName, final String testFileNameAbsolute) throws Exception {
		final Importer importer = getExcelImporter();

		final FileContentInfoViewModel fileAndSheetsInfo = new FileContentInfoViewModel();
		fileAndSheetsInfo.setExtension("xlsx");
		fileAndSheetsInfo.setFileAbsoluteName(testFileNameAbsolute);
		fileAndSheetsInfo.setFileName(testFileName);
		fileAndSheetsInfo.setWorksheets((ArrayList<WorksheetViewModel>)getExcelWorksheets(testFileName, testFileNameAbsolute));

		try {
			return importer.readVariables(fileAndSheetsInfo);

		} catch (final Exception e) {

			this.logger.error("getVariablesExcel failed", e);
			throw e;
		}
	}

	/**
	 *
	 * Tests {@link ExcelImporter#getTables(IOUtilsStoredFileInfoModel) in {@link #testGetTables()}
	 *
	 * @param testFileName
	 * @param testFileNameAbsolute
	 * @return
	 * @throws Exception
	 */
	private Collection<WorksheetViewModel> getExcelWorksheets(final String testFileName, final String testFileNameAbsolute) throws Exception {
		final Importer importer = getExcelImporter();

		final IOUtilsStoredFileInfoModel fileModel = new IOUtilsStoredFileInfoModel();
		fileModel.setAbsoluteFileName(testFileNameAbsolute);
		fileModel.setFileExtension("xlsx");
		fileModel.setFileName(testFileName);

		try {
			final Collection<WorksheetViewModel> tables = importer.getTables(fileModel);

			return tables;
		} catch (final Exception e) {

			this.logger.error("getTablesTest failed on importer.getTables(fileModel)", e);
			throw e;
		}
	}

	/**
	 * Tests {@link ExcelImporter#getTables(IOUtilsStoredFileInfoModel) via {@link #getExcelWorksheets(String, String)}
	 */
	@Test
	public void testGetTables() {

		try {
			final String testFileName = TestResourcesNames.TEST_EXCEL_FILE_XLSX;
			final String testFileNameAbsolute = this.getResourceAsAbsoluteURI(testFileName);

			final Collection<WorksheetViewModel> tables = getExcelWorksheets(testFileName, testFileNameAbsolute);

			assertEquals(2, tables.size());

			final Iterator<WorksheetViewModel> iterator = tables.iterator();

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

		} catch (final Exception e) {

			this.logger.error("getTablesTest failed on importer.getTables(fileModel)", e);
			fail("getTablesTest failed on importer.getTables(fileModel)");
		}
	}

	/**
	 * Tests {@link ExcelImporter#readVariables(FileContentInfoViewModel) via {@link #getVariablesExcel(String, String)}
	 */
	@Test
	public void testReadVariables() {

		final String testFileName = TestResourcesNames.TEST_EXCEL_FILE_XLSX;
		final String testFileNameAbsolute = this.getResourceAsAbsoluteURI(testFileName);

		try {
			final HashMap<String, ArrayList<DatasetVariableViewModel>> variables = getVariablesExcel(testFileName, testFileNameAbsolute);

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

		} catch (final Exception e) {

			this.logger.error("testReadVariablesExcel failed", e);
			fail("testReadVariablesExcel failed");
		}
	}

	/**
	 * Test read variables functionality on the example test excel file that is formated as CHIA template. Only need to read variables from
	 * the second sheet.
	 *
	 * Tests {@link ExcelImporter#readVariables(FileContentInfoViewModel)
	 */
	@Test
	public void testReadVariablesChiaTestExcelTwoSheets() {

		final String testFileName = TestResourcesNames.CHIA_TEST_EXCEL_TWO_SHEETS_XLSX;
		final String testFileNameAbsolute = this.getResourceAsAbsoluteURI(testFileName);

		try {

			final FileContentInfoViewModel fileAndSheetsInfo = new FileContentInfoViewModel();
			fileAndSheetsInfo.setExtension("xlsx");
			fileAndSheetsInfo.setFileAbsoluteName(testFileNameAbsolute);
			fileAndSheetsInfo.setFileName(testFileName);

			final ArrayList<WorksheetViewModel> worksheets = new ArrayList<>();
			final WorksheetViewModel worksheet = new WorksheetViewModel();
			worksheet.setHeaderRow(1);
			worksheet.setNumberOfRows(0);
			worksheet.setStartColumn("A");
			worksheet.setSheetName("Test_Test,Test_1916-21");
			worksheet.setIndexInTheFile(1);
			worksheets.add(worksheet);

			fileAndSheetsInfo.setWorksheets(worksheets);

			final Importer importer = getExcelImporter();
			final HashMap<String, ArrayList<DatasetVariableViewModel>> variables = importer.readVariables(fileAndSheetsInfo);

			assertEquals(1, variables.size());

			assertTrue(variables.containsKey("Test_Test,Test_1916-21"));

			final ArrayList<DatasetVariableViewModel> vars = variables.get("Test_Test,Test_1916-21");

			assertEquals(7, vars.size());

			DatasetVariableViewModel variable = vars.get(0);
			assertEquals("Country", variable.getOriginalName());
			assertEquals("Country", variable.getChosenName());

			variable = vars.get(1);
			assertEquals("Province", variable.getOriginalName());
			assertEquals("Province", variable.getChosenName());

			variable = vars.get(2);
			assertEquals("District", variable.getOriginalName());
			assertEquals("District", variable.getChosenName());

			variable = vars.get(3);
			assertEquals("Date", variable.getOriginalName());
			assertEquals("Date", variable.getChosenName());

			variable = vars.get(4);
			assertEquals("Mortality", variable.getOriginalName());
			assertEquals("Mortality", variable.getChosenName());

			variable = vars.get(5);
			assertEquals("Cause", variable.getOriginalName());
			assertEquals("Cause", variable.getChosenName());

			variable = vars.get(6);
			assertEquals("Source", variable.getOriginalName());
			assertEquals("Source", variable.getChosenName());

		} catch (final Exception e) {

			this.logger.error("testReadVariablesExcel failed", e);
			fail("testReadVariablesExcel failed");
		}
	}

	@Test
	public void testReadWorksheetData() {

		try {
			final Importer importer = getExcelImporter();

			final String testFileName = TestResourcesNames.TEST_EXCEL_FILE_XLSX;
			final String testFileNameAbsolute = this.getResourceAsAbsoluteURI(testFileName);

			final PreviewFileViewModel previewFileViewModel = new PreviewFileViewModel();
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

		} catch (final Exception e) {
			this.logger.error("testReadWorksheetDataExcel failed", e);
			fail("testReadWorksheetDataExcel failed");
		}
	}
}
