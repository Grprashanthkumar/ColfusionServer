/**
 *
 */
package edu.pitt.sis.exp.colfusion.bll.importers.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import au.com.bytecode.opencsv.CSVParser;
import edu.pitt.sis.exp.colfusion.bll.importers.Importer;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataRowViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 *
 */
public class CSVImporter implements Importer {

	final Logger logger = LogManager.getLogger(CSVImporter.class.getName());

	@Override
	public Collection<WorksheetViewModel> getTables(final IOUtilsStoredFileInfoModel fileModel) throws Exception {
		final WorksheetViewModel worksheet = new WorksheetViewModel();
		worksheet.setSheetName(fileModel.getFileName());
		worksheet.setHeaderRow(1);
		worksheet.setStartColumn("A");

		final ArrayList<WorksheetViewModel> result = new ArrayList<>();
		result.add(worksheet);

		return result;
	}

	@Override
	public HashMap<String, ArrayList<DatasetVariableViewModel>> readVariables(final FileContentInfoViewModel fileAndSheetsInfo) throws Exception {

		if (fileAndSheetsInfo.getWorksheets().size() != 1) {
			this.logger.error("readVariables in CSV importer failed: CSV file must have exacly one sheet specified in the model");

			throw new Exception("CSV file must have exacly one sheet specified in the model");
		}

		final String[] headerRowAr = readOneLine(fileAndSheetsInfo.getFileAbsoluteName(), 0);

		if (headerRowAr != null) {

			final ArrayList<DatasetVariableViewModel> oneSheetResult = new ArrayList<>();

			for(final String variable : headerRowAr) {
				final DatasetVariableViewModel datasetVariable = new DatasetVariableViewModel();

				datasetVariable.setOriginalName(variable);
				datasetVariable.setChosenName(variable);
				//TODO: guess type
				datasetVariable.setVariableValueType("text");

				if (datasetVariable != null) {
					oneSheetResult.add(datasetVariable);
				}
			}

			final HashMap<String, ArrayList<DatasetVariableViewModel>> result = new HashMap<>();
			result.put(fileAndSheetsInfo.getWorksheets().get(0).getSheetName(), oneSheetResult);
			return result;
		}
		else {
			this.logger.error("readVariables in CSV importer failed: could not read header row");

			throw new Exception("readVariables in CSV importer failed: could not read header row");
		}
	}

	/**
	 * Read a specified line from given file.
	 * @param fileAbsoluteName the absolute path of the file to read from.
	 * @param lineNumber the line number which to read.
	 * @return array of values from the read line.
	 * @throws Exception
	 */
	private String[] readOneLine(final String fileAbsoluteName, final int lineNumber) throws Exception {

		final ArrayList<WorksheetDataRowViewModel> result = readLinesFromTo(fileAbsoluteName, lineNumber, lineNumber);

		if (result == null) {
			this.logger.error(String.format("readOneLine failed. Got null result from readLinesFromTo. File name: %s. Line number: %d", fileAbsoluteName, lineNumber));

			throw new Exception(String.format("readOneLine failed. Got null result from readLinesFromTo. File name: %s. Line number: %d", fileAbsoluteName, lineNumber));
		}

		if (result.size() == 0) {
			return new String[0];
		}

		return result.get(0).getWorksheetDataRow().toArray(new String[result.get(0).getWorksheetDataRow().size()]);
	}

	/**
	 * Read a range of lines from given file.
	 * @param fileAbsoluteName the absolute path of the file to read from.
	 * @param startLine the line to start reading from.
	 * @param endLine the line number to stop finish at.
	 * @return array of values from the read line.
	 * @throws IOException
	 */
	private ArrayList<WorksheetDataRowViewModel> readLinesFromTo(final String fileAbsoluteName, final int startLine, final int endLine) throws IOException  {
		final Reader reader = new FileReader(new File(fileAbsoluteName));

		final LineNumberReader lnReader = new LineNumberReader(reader);

		//TODO: the separator should be passed from UI as parameter. Issue #28
		final char sep = ',';

		final CSVParser parser = new CSVParser(
				sep,
				CSVParser.DEFAULT_QUOTE_CHARACTER,
				(char) 0, // we don't want escape processing
				false,
				CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);

		final ArrayList<WorksheetDataRowViewModel> result = new ArrayList<>();

		for (int i = 0; i <= endLine; i++) {
			final String line = lnReader.readLine();

			if (line == null) {
				this.logger.info(String.format("readLinesFromTo got null line at possition %d. ", i));
				break;
			}

			if (i < startLine) {
				continue;
			}

			final WorksheetDataRowViewModel lineValues = new WorksheetDataRowViewModel(getCells(line, parser, lnReader));

			//TODO: not sure if this is the best implementation
			result.add(lineValues);
		}

		lnReader.close();

		return result;
	}

	/**
	 * Parses a given line with {@link CSVParser}.
	 *
	 * @param line the line to parse.
	 * @param parser the parser.
	 * @param lnReader reader to read extra data if the line was not finished.
	 * @return array of values read from line.
	 * @throws IOException
	 */
	static protected ArrayList<String> getCells(final String line, final CSVParser parser, final LineNumberReader lnReader)
			throws IOException{

		final ArrayList<String> cells = new ArrayList<>();
		String[] tokens = parser.parseLineMulti(line);
		cells.addAll(Arrays.asList(tokens));
		while (parser.isPending()) {
			tokens = parser.parseLineMulti(lnReader.readLine());
			cells.addAll(Arrays.asList(tokens));
		}
		return cells;
	}

	@Override
	public ArrayList<WorksheetDataViewModel> readWorksheetData(final PreviewFileViewModel previewFileViewModel) throws FileNotFoundException, IOException {

		final ArrayList<WorksheetDataViewModel> result = new ArrayList<WorksheetDataViewModel>();

		final int startRow = previewFileViewModel.getPreviewRowsPerPage() * (previewFileViewModel.getPreviewPage() - 1);
		final int endRow = startRow + previewFileViewModel.getPreviewRowsPerPage();

		final ArrayList<WorksheetDataRowViewModel> data = readLinesFromTo(previewFileViewModel.getFileAbsoluteName(), startRow, endRow);

		final WorksheetDataViewModel worksheetDataViewModel = new WorksheetDataViewModel();
		worksheetDataViewModel.setWorksheetName(previewFileViewModel.getFileName());

		worksheetDataViewModel.setWorksheetData(data);

		result.add(worksheetDataViewModel);

		return result;
	}
}
