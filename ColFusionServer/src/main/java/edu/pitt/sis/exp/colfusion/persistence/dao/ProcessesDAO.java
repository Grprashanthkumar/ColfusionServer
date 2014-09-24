/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionProcesses;

/**
 * @author Evgeny
 *
 */
public interface ProcessesDAO extends GenericDAO<ColfusionProcesses, Integer> {

	/**
	 * Find oldest pending process from the db.
	 * 
	 * @param limit is the max number of processes to search in db.
	 * @return the found process.
	 */
	ColfusionProcesses findPendingProcess(int limit);

}
