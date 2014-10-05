/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl.HistoryItem;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoMetadataEditHistory;

/**
 * @author Evgeny
 *
 */
public interface SourceInfoMetadataEditHistoryDAO extends GenericDAO<ColfusionSourceinfoMetadataEditHistory, Integer> {

	/**
	 * Checks if two values differ and if so writes the new value in as sourceinfo edit metadata history record.
	 * 
	 * @param sid id of the story for which metadata edit has happened.
	 * @param userId who did change.
	 * @param oldValue 
	 * @param newValue
	 * @param itemType of this enum type {@link HistoryItem}
	 * @param reason the comment user left when did the change.
	 * @throws Exception 
	 */
	public void saveHistoryIfChanged(int sid, int userId, String oldValue, String newValue, HistoryItem itemType, String reason) throws Exception;
}
