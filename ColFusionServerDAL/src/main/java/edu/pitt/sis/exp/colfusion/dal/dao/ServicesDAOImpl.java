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
	public boolean queryServieExistance(String serviceName) throws HibernateException{
		boolean queryResult = false;
		String hql = "FROM ColfusionServices WHERE serviceName = '" + serviceName + "'";
	    
		try{    	
	    	@SuppressWarnings("unchecked")
	    	List<ColfusionServices> serviceList = (List<ColfusionServices>)HibernateUtil.getSession().createQuery(hql).list();
	    
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
	public String queryServiceStatus(String serviceName) throws HibernateException{
		String serviceStatus = null;
		String hql = "SELECT serviceStatus FROM ColfusionServices WHERE serviceName = '" + serviceName + "'";
	    
		try{    	
			Query query = HibernateUtil.getSession().createQuery(hql);
	    
			if(query != null)
	    		serviceStatus = query.list().get(0).toString();
	    }
	    catch(Exception e){
			logger.error(String.format("queryServiceStatus failed on HibernateUtil.getSession()..."));
			throw new HibernateException(e);
		}	
		return serviceStatus;
	}
	
	@Override
	public boolean updateServiceStatus(ColfusionServices service) throws HibernateException{
		boolean updateResult = false;
		int serviceID = service.getServiceID();
		String serviceStatus = service.getServiceStatus();
	    
		try{  
			ColfusionServices serviceUpdate = (ColfusionServices) HibernateUtil.getSession().get(ColfusionServices.class, serviceID); 
	    	serviceUpdate.setServiceStatus(serviceStatus);
	    	HibernateUtil.getSession().update(serviceUpdate); 
	    	
	    	updateResult = true;
	    }
	    catch(Exception e){
			logger.error(String.format("updateServiceStatus failed on HibernateUtil.getSession()..."));
			throw new HibernateException(e);
		}	
		return updateResult;
	}
}
