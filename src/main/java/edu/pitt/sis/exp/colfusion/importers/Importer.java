/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import edu.pitt.sis.exp.colfusion.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.WorksheetViewModel;

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
}
