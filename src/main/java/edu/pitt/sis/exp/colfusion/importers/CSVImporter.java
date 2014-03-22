/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers;

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
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import edu.pitt.sis.exp.colfusion.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.WorksheetDataViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.WorksheetViewModel;

/**
 * @author Evgeny
 *
 */
public class CSVImporter implements Importer {

	final Logger logger = LogManager.getLogger(CSVImporter.class.getName());
	
	@Override
	public Collection<WorksheetViewModel> getTables(IOUtilsStoredFileInfoModel fileModel) throws Exception {
		WorksheetViewModel worksheet = new WorksheetViewModel();
		worksheet.setSheetName("Worksheet");
		worksheet.setHeaderRow(1);
        worksheet.setStartColumn("A");
        
        ArrayList<WorksheetViewModel> result = new ArrayList<>();
        result.add(worksheet);
        
        return result;
	}

	@Override
	public HashMap<String, ArrayList<DatasetVariableViewModel>> readVariables(FileContentInfoViewModel fileAndSheetsInfo) throws Exception {
		
		if (fileAndSheetsInfo.getWorksheets().size() != 1) {
    		logger.error("readVariables in CSV importer failed: CSV file must have exacly one sheet specified in the model");
    		
    		throw new Exception("CSV file must have exacly one sheet specified in the model");
    	}
		
		String[] headerRowAr = readOneLine(fileAndSheetsInfo.getFileAbsoluteName(), 0);
	
    	if (headerRowAr != null) {
    		
    		ArrayList<DatasetVariableViewModel> oneSheetResult = new ArrayList<>();
    		
    		for(String variable : headerRowAr) {
    			DatasetVariableViewModel datasetVariable = new DatasetVariableViewModel();
    			
    			datasetVariable.setOriginalName(variable);
    			datasetVariable.setChosenName(variable);
    			//TODO: guess type
    			datasetVariable.setVariableValueType("text");
    			
    			if (datasetVariable != null) {
            		oneSheetResult.add(datasetVariable);
            	}
    		}
            
    		HashMap<String, ArrayList<DatasetVariableViewModel>> result = new HashMap<>();
            result.put(fileAndSheetsInfo.getWorksheets().get(0).getSheetName(), oneSheetResult);
            return result;
        }
    	else {
    		logger.error("readVariables in CSV importer failed: could not read header row");
    		
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
	private String[] readOneLine(String fileAbsoluteName, int lineNumber) throws Exception {
		
		ArrayList<String[]> result = readLinesFromTo(fileAbsoluteName, lineNumber, lineNumber);
		
		if (result == null) {
			logger.error(String.format("readOneLine failed. Got null result from readLinesFromTo. File name: %s. Line number: %d", fileAbsoluteName, lineNumber));
			
			throw new Exception(String.format("readOneLine failed. Got null result from readLinesFromTo. File name: %s. Line number: %d", fileAbsoluteName, lineNumber));
		}
		
		if (result.size() == 0) {
			return new String[0];
		}
		
		return result.get(0);
	}
	
	/**
	 * Read a range of lines from given file.
	 * @param fileAbsoluteName the absolute path of the file to read from.
	 * @param startLine the line to start reading from.
	 * @param endLine the line number to stop finish at.
	 * @return array of values from the read line.
	 * @throws IOException
	 */
	private ArrayList<String[]> readLinesFromTo(String fileAbsoluteName, int startLine, int endLine) throws IOException  {
		Reader reader = new FileReader(new File(fileAbsoluteName));
		
		final LineNumberReader lnReader = new LineNumberReader(reader);
		
		//TODO: the separator should be passed from UI as parameter.
		char sep = ',';
		
		final CSVParser parser = new CSVParser(
	            sep,
	            CSVParser.DEFAULT_QUOTE_CHARACTER,
	            (char) 0, // we don't want escape processing
	            false,
	            CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
		
		lnReader.setLineNumber(startLine);
		
		 ArrayList<String[]> result = new ArrayList<>();
		
		for (int i = startLine; i < endLine; i++) {
			String line = lnReader.readLine();
			
			if (line == null) {
				logger.info(String.format("readLinesFromTo got null line at possition %d. ", i));
				break;
			}
			
			ArrayList<String> lineValues = getCells(line, parser, lnReader);
			
			//TODO: not sure if this is the best implementation
			result.add(lineValues.toArray(new String[lineValues.size()]));
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
	static protected ArrayList<String> getCells(String line, CSVParser parser, LineNumberReader lnReader)
	        throws IOException{
	        
	        ArrayList<String> cells = new ArrayList<>();
	        String[] tokens = parser.parseLineMulti(line);
	        cells.addAll(Arrays.asList(tokens));
	        while (parser.isPending()) {
	            tokens = parser.parseLineMulti(lnReader.readLine());
	            cells.addAll(Arrays.asList(tokens));
	        }
	        return cells;
	    }

	@Override
	public ArrayList<WorksheetDataViewModel> readWorksheetData(PreviewFileViewModel previewFileViewModel) throws FileNotFoundException, IOException {
		
		ArrayList<WorksheetDataViewModel> result = new ArrayList<WorksheetDataViewModel>();
		
		int startRow = previewFileViewModel.getPreviewRowsPerPage() * (previewFileViewModel.getPreviewPage() - 1);
		int endRow = startRow + previewFileViewModel.getPreviewRowsPerPage();
		
		ArrayList<String[]> data = readLinesFromTo(previewFileViewModel.getFileAbsoluteName(), startRow, endRow);
		        
        WorksheetDataViewModel worksheetDataViewModel = new WorksheetDataViewModel();
        worksheetDataViewModel.setWorksheetName(previewFileViewModel.getFileName());
       
        worksheetDataViewModel.setWorksheetData(data);
        
        result.add(worksheetDataViewModel);
		
        return result;
	} 
}
