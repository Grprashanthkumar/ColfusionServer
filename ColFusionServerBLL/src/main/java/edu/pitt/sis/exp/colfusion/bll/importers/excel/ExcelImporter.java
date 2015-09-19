/**
 *
 */
package edu.pitt.sis.exp.colfusion.bll.importers.excel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.importers.Importer;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataRowViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

public class ExcelImporter implements Importer {
	final Logger logger = LogManager.getLogger(ExcelImporter.class.getName());

	ExcelFileHandler excelFileHandler;

	public ExcelImporter(final ExcelFileHandler excelFileHandler) {
		this.excelFileHandler = excelFileHandler;
	}

	@Override
	public Collection<WorksheetViewModel> getTables(final IOUtilsStoredFileInfoModel fileModel) throws FileNotFoundException, IOException  {

		try (ExcelFile excelFile = this.excelFileHandler.openFile(fileModel.getAbsoluteFileName())) {
			final ArrayList<WorksheetViewModel> result = new ArrayList<>();

			final int sheetCount = excelFile.getNumberOfSheets();

			for (int i = 0; i < sheetCount; i++) {
				final ExcelSheet sheet = excelFile.getSheet(i);
				final int rows = sheet.getRowCount();

				final WorksheetViewModel worksheet = new WorksheetViewModel();
				worksheet.setSheetName(sheet.getSheetName());
				worksheet.setNumberOfRows(rows);
				worksheet.setHeaderRow(sheet.getHeaderRow());
				worksheet.setStartColumn("A");
				worksheet.setIndexInTheFile(i);
				result.add(worksheet);
			}

			return result;
		}
	}

	@Override
	public HashMap<String, ArrayList<DatasetVariableViewModel>> readVariables(final FileContentInfoViewModel fileAndSheetsInfo) throws Exception {

		try (ExcelFile excelFile = this.excelFileHandler.openFile(fileAndSheetsInfo.getFileAbsoluteName())) {
			final HashMap<String, ArrayList<DatasetVariableViewModel>> result = new HashMap<>();

			for (final WorksheetViewModel worksheet : fileAndSheetsInfo.getWorksheets()) {
				final ExcelSheet sheet = excelFile.getSheet(worksheet.getIndexInTheFile());
				//TODO: change ArrayList to List
				final ArrayList<DatasetVariableViewModel> oneSheetVariables = (ArrayList<DatasetVariableViewModel>) readVariablesOneSheet(worksheet, sheet);
				result.put(worksheet.getSheetName(), oneSheetVariables);
			}

			return result;
		}
	}

	private List<DatasetVariableViewModel> readVariablesOneSheet(final WorksheetViewModel worksheet,
			final ExcelSheet sheet) {
		final Stream<ExcelCell> row = sheet.readRow(worksheet.getHeaderRow() - 1);
		return row.map(c -> new DatasetVariableViewModel(c.getValue().toString(), c.getValue().toString(), c.getDataType())).collect(Collectors.toList());
	}

	@Override
	public ArrayList<WorksheetDataViewModel> readWorksheetData(final PreviewFileViewModel previewFileViewModel) throws IOException {

		try (final ExcelFile excelFile = this.excelFileHandler.openFile(previewFileViewModel.getFileAbsoluteName())) {
			final ArrayList<WorksheetDataViewModel> result = new ArrayList<WorksheetDataViewModel>();

			final int startRow = previewFileViewModel.getPreviewRowsPerPage() * (previewFileViewModel.getPreviewPage() - 1);
			final int endRow = startRow + previewFileViewModel.getPreviewRowsPerPage();

			IntStream.range(0, excelFile.getNumberOfSheets()).mapToObj(i -> readDataOneSheet(excelFile.getSheet(i), startRow, endRow)).forEach(result::add);

			return result;
		}
	}

	private WorksheetDataViewModel readDataOneSheet(final ExcelSheet sheet, final int startRow, final int endRow) {

		final WorksheetDataViewModel worksheetDataViewModel = new WorksheetDataViewModel();
		worksheetDataViewModel.setWorksheetName(sheet.getSheetName());

		final ArrayList<WorksheetDataRowViewModel> worksheetData = new ArrayList<>();

		IntStream.range(startRow, Math.min(endRow, sheet.getRowCount())).mapToObj(i -> sheet.readRow(i))
		.map(r -> {
			final WorksheetDataRowViewModel rowCells = new WorksheetDataRowViewModel();
			r.forEach(c -> rowCells.getWorksheetDataRow().add(c.getValue().toString()));
			return rowCells;
		})
		.forEach(worksheetData::add);

		worksheetDataViewModel.setWorksheetData(worksheetData);

		return worksheetDataViewModel;
	}
}
