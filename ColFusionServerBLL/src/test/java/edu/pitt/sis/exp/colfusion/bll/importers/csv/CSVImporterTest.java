package edu.pitt.sis.exp.colfusion.bll.importers.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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

public class CSVImporterTest extends UnitTestBase {
	Logger logger = LogManager.getLogger(CSVImporterTest.class.getName());

	private Importer getImporter() throws Exception {
		return new CSVImporter();
	}

	private Collection<WorksheetViewModel> getCSVWorksheets(final String testFileName, final String testFileNameAbsolute) throws Exception {
		final Importer importer = getImporter();

		final IOUtilsStoredFileInfoModel fileModel = new IOUtilsStoredFileInfoModel();
		fileModel.setAbsoluteFileName(testFileNameAbsolute);
		fileModel.setFileExtension("csv");
		fileModel.setFileName(testFileName);

		try {
			final Collection<WorksheetViewModel> tables = importer.getTables(fileModel);

			return tables;
		} catch (final Exception e) {

			this.logger.error("getTablesTest failed on importer.getTables(fileModel)", e);
			throw e;
		}
	}

	@Test
	public void testGetTablesCSVFile() {
		try {
			final String testFileName = TestResourcesNames.TEST_CSV_FILE;
			final String testFileNameAbsolute = this.getResourceAsAbsoluteURI(testFileName);

			final Collection<WorksheetViewModel> tables = getCSVWorksheets(testFileName, testFileNameAbsolute);

			assertEquals(1, tables.size());

			final WorksheetViewModel worksheet = tables.iterator().next();

			assertEquals(testFileName, worksheet.getSheetName());

			assertEquals(1, worksheet.getHeaderRow());
			assertEquals("A", worksheet.getStartColumn());

		} catch (final Exception e) {

			this.logger.error("testGetTablesCSVFile failed", e);
			fail("getTablesTest failed on importer.getTables(fileModel)");
		}
	}



	private HashMap<String, ArrayList<DatasetVariableViewModel>> getVariablesCSV(final String testFileName, final String testFileNameAbsolute) throws Exception {
		final Importer importer = getImporter();

		final FileContentInfoViewModel fileAndSheetsInfo = new FileContentInfoViewModel();
		fileAndSheetsInfo.setExtension("csv");
		fileAndSheetsInfo.setFileAbsoluteName(testFileNameAbsolute);
		fileAndSheetsInfo.setFileName(testFileName);
		fileAndSheetsInfo.setWorksheets((ArrayList<WorksheetViewModel>)getCSVWorksheets(testFileName, testFileNameAbsolute));

		try {
			return importer.readVariables(fileAndSheetsInfo);

		} catch (final Exception e) {

			this.logger.error("getVariablesCSV failed", e);
			throw e;
		}
	}

	@Test
	public void testReadVariablesCSV() {

		final String testFileName = TestResourcesNames.TEST_CSV_FILE;
		final String testFileNameAbsolute = this.getResourceAsAbsoluteURI(testFileName);

		try {
			final HashMap<String, ArrayList<DatasetVariableViewModel>> variables = getVariablesCSV(testFileName, testFileNameAbsolute);

			assertEquals(1, variables.size());

			assertTrue(variables.containsKey(testFileName));

			final ArrayList<DatasetVariableViewModel> vars = variables.get(testFileName);

			assertEquals(2, vars.size());

			DatasetVariableViewModel variable = vars.get(0);

			assertEquals("cl1", variable.getOriginalName());
			assertEquals("cl1", variable.getChosenName());

			variable = vars.get(1);

			assertEquals("cl2", variable.getOriginalName());
			assertEquals("cl2", variable.getChosenName());

		} catch (final Exception e) {

			this.logger.error("testReadVariablesCSV failed on importer.getTables(fileModel)", e);
			fail("testReadVariablesCSV failed on importer.getTables(fileModel)");
		}
	}



	@Test
	public void testReadWorksheetDataCSV() {

		try {
			final Importer importer = getImporter();

			final String testFileName = TestResourcesNames.TEST_CSV_FILE;
			final String testFileNameAbsolute = this.getResourceAsAbsoluteURI(testFileName);

			final PreviewFileViewModel previewFileViewModel = new PreviewFileViewModel();
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

		} catch (final Exception e) {
			this.logger.error("testReadWorksheetDataCSV failed", e);
			fail("testReadWorksheetDataCSV failed");
		}
	}
}
