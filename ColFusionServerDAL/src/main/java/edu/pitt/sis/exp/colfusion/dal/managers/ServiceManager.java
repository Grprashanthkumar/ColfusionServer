package edu.pitt.sis.exp.colfusion.dal.managers;

import org.hibernate.HibernateException;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;

/**
 * @author Hao Bai
 *
 */
public interface ServiceManager extends GeneralManager<ColfusionServices, Integer> {
	
	/**
	 * 
	 * Query if service is existed in database.
	 * 
	 * @param serviceName to find to be contained in service's name.
	 * @return if service with serviceName exists (boolean). 
	 * @throws Exception 
	 */
	public boolean queryServieExistance(String serviceName);
	
	/**
	 * 
	 * Query service's status.
	 * 
	 * @param serviceName to find to be contained in service's name.
	 * @return service's status. 
	 * @throws Exception 
	 */
	public String queryServiceStatus(String serviceName);
	
	/**
	 * 
	 * Update service's status.
	 * 
	 * @param Object service to find to be contained in service.
	 * @return updating result (boolean). 
	 * @throws Exception 
	 */
	public boolean updateServiceStatus(ColfusionServices service) throws HibernateException;
	
}
