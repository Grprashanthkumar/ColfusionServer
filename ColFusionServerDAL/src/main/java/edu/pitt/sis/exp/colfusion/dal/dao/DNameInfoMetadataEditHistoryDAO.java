/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManagerImpl.VariableMetadataHistoryItem;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfoMetadataEditHistory;

/**
 * @author Evgeny
 *
 */
public interface DNameInfoMetadataEditHistoryDAO extends GenericDAO<ColfusionDnameinfoMetadataEditHistory, Integer> {

	/**
	 * Checks if two values differ and if so writes the new value in as dnameinfo edit metadata history record.
	 * 
	 * @param cid id of the column/dnameinfo for which metadata edit has happened.
	 * @param userId who did change.
	 * @param oldValue 
	 * @param newValue
	 * @param itemType of this enum type {@link VariableMetadataHistoryItem}
	 * @param reason the comment user left when did the change.
	 * @throws Exception 
	 */
	void saveHistoryIfChanged(Integer cid, int userId, String oldValue, String newValue, VariableMetadataHistoryItem editedAttribute, String reason) throws Exception;

}
