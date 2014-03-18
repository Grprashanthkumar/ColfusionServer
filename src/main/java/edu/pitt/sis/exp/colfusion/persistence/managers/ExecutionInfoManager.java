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

	/**
	 * Updates status attribute with a given value for a specific execution info record.
	 * 
	 * @param executionLogId the eid of the the execution info record for which ot update the status attribute.
	 * @param statusValue the value to set.
	 * @throws Exception 
	 */
	void updateStatus(int executionLogId, String statusValue) throws Exception;

	/**
	 * Appends provided string value to the log attribute in the execution info table for the record identified by provided executionInfoId value.
	 * @param executionLogId the id (eid) of the record to update.
	 * @param logValueToAppend is the value to append.
	 */
	void appendLog(int executionLogId, String logValueToAppend) throws Exception;

}
