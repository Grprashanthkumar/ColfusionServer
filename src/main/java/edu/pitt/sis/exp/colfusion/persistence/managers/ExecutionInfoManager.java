/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionExecuteinfo;

/**
 * @author Evgeny
 *
 */
public interface ExecutionInfoManager extends GeneralManager<ColfusionExecuteinfo, Integer> {

	/**
	 * 
	 * Creates a new record in the exectioninfo table for given sid and table name and return the id of that record (logid). That id could be used to update 
	 * execution info about execution process (e.g. write any errors or status).
	 * 
	 * If there is a record in the table for provided sid and tableName, then that logid will be returned.
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 * @throws Exception 
	 */
	int getExecutionLogId(int sid, String tableName) throws Exception;

}
