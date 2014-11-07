/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionColumnTableInfo;

/**
 * @author Evgeny
 *
 */
public interface ColumnTableInfoDAO extends GenericDAO<ColfusionColumnTableInfo, Integer> {
	
	//TODO FIXME: this is workaround until we fix our database design
	public ColfusionColumnTableInfo findBySidAndOriginalTableName(int sid, String tableName);
}
