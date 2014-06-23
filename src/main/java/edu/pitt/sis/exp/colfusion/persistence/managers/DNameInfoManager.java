/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.viewmodels.WorksheetViewModel;

/**
 * @author Evgeny
 *
 */
public interface DNameInfoManager extends GeneralManager<ColfusionDnameinfo, Integer> {

	/**
	 * Creates or updates metadata for all variables in a given table/worksheet
	 * 
	 * @param worksheet for which variables metadata need to be created/updated.
	 * @param tableNamePrefix a string to workhseet name
	 */
	void createOrUpdateSheetMetadata(WorksheetViewModel worksheet, String tableNamePrefix, int sid, int userId)  throws Exception;

	//TODO: wrintt a comment
	/**
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 */
	List<ColfusionDnameinfo> getColumnsMetadata(int sid, String tableName);
}
