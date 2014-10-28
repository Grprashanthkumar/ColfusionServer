package edu.pitt.sis.exp.colfusion.dal.dao;

import org.hibernate.HibernateException;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;

/**
 * @author Hao Bai
 *
 */
public interface ServicesDAO extends GenericDAO<ColfusionServices, Integer> {
	/**
	 * 
	 * Query if service is existed in database.
	 * 
	 * @param serviceName to find to be contained in service's name.
	 * @return if service with serviceName exists (boolean). 
	 * @throws Exception 
	 */
	public boolean queryServieExistance(int serviceID) throws HibernateException;
	
	/**
	 * 
	 * Query service's status.
	 * 
	 * @param serviceName to find to be contained in service's name.
	 * @return service's status. 
	 * @throws Exception 
	 */
	public String queryServiceStatus(int serviceID) throws HibernateException;
}
