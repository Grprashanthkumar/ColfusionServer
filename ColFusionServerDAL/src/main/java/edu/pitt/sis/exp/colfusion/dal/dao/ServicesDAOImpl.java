package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author Hao Bai
 *
 */
public class ServicesDAOImpl extends GenericDAOImpl<ColfusionServices, Integer> implements ServicesDAO {

	Logger logger = LogManager.getLogger(ServicesDAOImpl.class.getName());
	
	@Override
	public boolean queryServieExistance(int serviceID) throws HibernateException{
		boolean queryResult = false;
		String hql = "FROM ColfusionServices WHERE serviceID = :serviceID";
	    
		try{    	
	    	@SuppressWarnings("unchecked")
	    	List<ColfusionServices> serviceList = (List<ColfusionServices>)HibernateUtil.getSession().createQuery(hql).setParameter("serviceID", serviceID).list();
	    
	    	if(serviceList.isEmpty() == false)
	    		queryResult = true;
	    }
	    catch(Exception e){
			logger.error(String.format("queryServieExistance failed on HibernateUtil.getSession()..."));
			throw new HibernateException(e);
		}	
		return queryResult;
	}
	
	@Override
	public String queryServiceStatus(int serviceID) throws HibernateException{
		String serviceStatus = null;
		String hql = "SELECT serviceStatus FROM ColfusionServices WHERE serviceID = :serviceID";
	    
		try{    	
			Query query = HibernateUtil.getSession().createQuery(hql).setParameter("serviceID", serviceID);
	    
			if(query != null)
	    		serviceStatus = query.list().get(0).toString();
	    }
	    catch(Exception e){
			logger.error(String.format("queryServiceStatus failed on HibernateUtil.getSession()..."));
			throw new HibernateException(e);
		}	
		return serviceStatus;
	}
}
