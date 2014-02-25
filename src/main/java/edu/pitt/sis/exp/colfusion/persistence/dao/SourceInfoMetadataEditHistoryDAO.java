/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl.HistoryItem;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoMetadataEditHistory;

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
	 */
	public void saveHistoryIfChanged(int sid, int userId, String oldValue, String newValue, HistoryItem itemType, String reason);
}
