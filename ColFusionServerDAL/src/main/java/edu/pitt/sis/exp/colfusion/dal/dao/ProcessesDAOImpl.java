/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionProcesses;

/**
 * @author Evgeny
 *
 */
public class ProcessesDAOImpl extends GenericDAOImpl<ColfusionProcesses, Integer> implements ProcessesDAO {

	@Override
	public ColfusionProcesses findPendingProcess(int limit) {
		List<ColfusionProcesses> result = null;
		
		String sql = "SELECT cp FROM ColfusionProcesses cp where cp.status = 'new' ORDER BY cp.pid ASC";
	    	    
	    Query query = this.getSession().createQuery(sql).setMaxResults(limit);
						
		result = this.findMany(query);
		
		if (result.size() == 0) 
			return null;
		
		return result.get(0);
	}
}
