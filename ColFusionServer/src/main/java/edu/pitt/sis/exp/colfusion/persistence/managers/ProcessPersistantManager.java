/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionProcesses;
import edu.pitt.sis.exp.colfusion.process.ProcessStatusEnum;

/**
 * @author Evgeny
 *
 */
public interface ProcessPersistantManager extends GeneralManager<ColfusionProcesses, Integer> {

	/**
	 * Find oldest pending process from the db.
	 * @param string 
	 * @param running 
	 * 
	 * @param limit is the number of processes to search in db.
	 * @return the found process.
	 */
	ColfusionProcesses getNewPendingProcessAndSetAsRunningFromDB(String status, String reason, int limit);

}
