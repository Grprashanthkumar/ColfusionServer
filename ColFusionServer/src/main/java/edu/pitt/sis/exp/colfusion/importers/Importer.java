/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetDataViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 * @author Evgeny
 *
 */
public interface Importer {
	
	/**
	 * Gets tables from uploaded file.
	 * 
	 * @param fileModel {@link IOUtilsStoredFileInfoModel} the model which describes uploaded file. TODO: need to change it to work with db too.
	 * @return {@link Collection} of {@link WorksheetViewModel} that describes each table.
	 * @throws Exception
	 */
	public Collection<WorksheetViewModel> getTables(IOUtilsStoredFileInfoModel fileModel) throws Exception;
		
	
	
	/**
	 * Reads header row from given file.
	 * 
	 * @param fileAndSheetsInfo {@link FileContentInfoViewModel} info about a file from which to read header row.
	 * @return {@link HashMap} where key is worksheet name and the value is the {@link ArrayList} of {@link DatasetVariableViewModel} which describes
	 * each variables in the header row of the file.
	 * @throws Exception
	 */
	public HashMap<String, ArrayList<DatasetVariableViewModel>> readVariables(FileContentInfoViewModel fileAndSheetsInfo) throws Exception;


	/**
	 * Read a range of rows from all sheets/tables in given file. The rage is specified by the number of rows to read and the page number.
	 * 
	 * @param previewFileViewModel has info about the data file and row rages.
	 * @return data for each sheet.
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	public ArrayList<WorksheetDataViewModel> readWorksheetData(PreviewFileViewModel previewFileViewModel) throws FileNotFoundException, IOException;
}
