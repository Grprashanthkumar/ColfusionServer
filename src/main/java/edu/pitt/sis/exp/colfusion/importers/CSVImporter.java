/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers;

import java.io.File;
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
import org.apache.poi.ss.usermodel.Sheet;

import sun.util.logging.resources.logging;
import au.com.bytecode.opencsv.CSVParser;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import edu.pitt.sis.exp.colfusion.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FileContentInfoViewModel;
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
		
		Reader reader = new FileReader(new File(fileAndSheetsInfo.getFileAbsoluteName()));
		
		final LineNumberReader lnReader = new LineNumberReader(reader);
		
		String line = lnReader.readLine();
		
		if (line == null) {
			logger.error("readVariables for CSV importer fialed, no line read, is file empty?");
			
			throw new Exception("no line read, is file empty?");
		}
    	
		HashMap<String, ArrayList<DatasetVariableViewModel>> result = new HashMap<>();
		
		//TODO: the separator should be passed from UI as parameter.
		char sep = ',';
		
		final CSVParser parser = new CSVParser(
	            sep,
	            CSVParser.DEFAULT_QUOTE_CHARACTER,
	            (char) 0, // we don't want escape processing
	            false,
	            CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
		
		ArrayList<String> headerRowAr = getCells(line, parser, lnReader);
    	
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
            
            result.put(fileAndSheetsInfo.getWorksheets().get(0).getSheetName(), oneSheetResult);
        }
    	else {
    		logger.error("readVariables in CSV importer failed: could not read header row");
    		
    		throw new Exception("readVariables in CSV importer failed: could not read header row");
    	}

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
	

}
