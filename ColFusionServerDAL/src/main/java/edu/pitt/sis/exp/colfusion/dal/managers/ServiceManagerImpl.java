package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.dal.dao.ServicesDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.ServicesDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author Hao Bai
 *
 */
public class ServiceManagerImpl extends GeneralManagerImpl<ColfusionServices, Integer> implements ServiceManager{

	Logger logger = LogManager.getLogger(ServiceManagerImpl.class.getName());
	
	public ServiceManagerImpl() {
		super(new ServicesDAOImpl(), ColfusionServices.class);
	}

	//***************************************
	// From ServiceManager interface
	//***************************************
	
	@Override
	public boolean queryServieExistance(int serviceID){
		try{
			HibernateUtil.beginTransaction();
            
            boolean queryResult = ((ServicesDAO) _dao).queryServieExistance(serviceID);
            
            HibernateUtil.commitTransaction();
            
            return queryResult;
        }
		catch(NonUniqueResultException ex){
        	HibernateUtil.rollbackTransaction();     	
        	logger.error("save failed NonUniqueResultException", ex);
            throw ex;
        }
		catch(HibernateException ex){
        	HibernateUtil.rollbackTransaction();     	
        	logger.error("save failed HibernateException", ex);
        	throw ex;
        }
	}
	
	@Override
	public String queryServiceStatus(int serviceID){
		try{
			HibernateUtil.beginTransaction();
            
            String serviceStatus = ((ServicesDAO) _dao).queryServiceStatus(serviceID);
            
            HibernateUtil.commitTransaction();
            
            return serviceStatus;
        }
		catch(NonUniqueResultException ex){
        	HibernateUtil.rollbackTransaction();     	
        	logger.error("save failed NonUniqueResultException", ex);
            throw ex;
        }
		catch(HibernateException ex){
        	HibernateUtil.rollbackTransaction();     	
        	logger.error("save failed HibernateException", ex);
        	throw ex;
        }
	}
}