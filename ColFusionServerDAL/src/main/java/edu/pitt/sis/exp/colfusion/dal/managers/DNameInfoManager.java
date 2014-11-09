/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.BasicTableInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DnameViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataHistoryLogRecordViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;

/**
 * @author Evgeny
 *
 */
public interface DNameInfoManager extends GeneralManager<ColfusionDnameinfo, Integer> {

	/**
	 * Creates or updates metadata for all variables in a given table/worksheet
	 * 
	 * @param 	worksheet 
	 * 				for which variables metadata need to be created/updated.
	 * @param	sid
	 * 				id of the story
	 * @param	userId
	 * 				id of the user  to is making the changes
	 */
	void createOrUpdateSheetMetadata(WorksheetViewModel worksheet, int sid, int userId)  throws Exception;

	//TODO: wrintt a comment
	/**
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 */
	List<ColfusionDnameinfo> getColumnsMetadata(int sid, String tableName);

	void addColumnMetaEditHistory(int cid, int userid, String editAttribute,
			String reason, String editValue);

	List<StoryMetadataHistoryLogRecordViewModel> getColumnMetaEditHistory(
			int cid, String editAttribute);
	
	/**
	 * 
	 * @param sid
	 * @param ColfusionDnameInfo table
	 * @return
	 */
	List<BasicTableInfoViewModel> getTableInfo(int sid);
	
	/**
	 * @param int sid
	 * @return Dname list table info.
	 */
	public List<DnameViewModel> getDnameListViewModelBySid(int sid);
	
	/**
	 * @param int cid
	 * @return Dname list table info.
	 */
	public List<DnameViewModel> getDnameListViewModelByCid(int cid);

	
}
