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
public class CSVImporter implements Importer {

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
		// TODO Auto-generated method stub
		return null;
	}

}
