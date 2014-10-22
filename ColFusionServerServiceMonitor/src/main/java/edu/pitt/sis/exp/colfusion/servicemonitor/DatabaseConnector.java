package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.List;
import org.hibernate.HibernateException; 
import org.hibernate.Query;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Hao Bai
 * 
 * This class is used for establishing connection with
 * database, and update colfusion database with operations 
 * such as update service table (update service attributes).   
 */
public class DatabaseConnector {

	private static SessionFactory sessionFactory; 
	private static ServiceRegistry serviceRegistry;
	
	private Logger logger = LogManager.getLogger(DatabaseConnector.class.getName());

	public DatabaseConnector(){
		try{
			Configuration configuration = new Configuration();
		    configuration.configure();
		    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
		            configuration.getProperties()).build();
		    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		}
		catch(Exception exception){
			logger.error("In DatabaseConnector.Constructor\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
		}
	}
	
	/*query if service is existed in database
	 * */
	public boolean queryServieExistance(String serviceName){
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    boolean queryResult = false;
	    
	    try{
	    	transaction = session.beginTransaction();
	    	String hql = "FROM Service WHERE serviceName = '" + serviceName + "'";
	    	@SuppressWarnings("unchecked")
	    	List<Service> serviceList = (List<Service>)session.createQuery(hql).list();
	    
	    	if(serviceList.isEmpty() == false)
	    		queryResult = true;
	
	        transaction.commit();
	        return queryResult;
	    }
	    catch(HibernateException exception){
	    	if(transaction != null) 
	    		transaction.rollback();
	    	logger.error("In DatabaseConnector.queryServieExistance()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
	    	return queryResult;
	    }
	    finally{
	    	session.close();
	    }
	}
	
	/*query all services in database
	 * */
	public List<Service> queryAllServies(){
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    //List<Service> serviceList = null;
	    
	    try{
	    	transaction = session.beginTransaction();
	    	@SuppressWarnings("unchecked")
	    	List<Service> serviceList = (List<Service>)session.createQuery("FROM Service").list(); 
	    	
	        transaction.commit();
	        return serviceList;
	    }
	    catch(HibernateException exception){
	    	if(transaction != null) 
	    		transaction.rollback();
	    	logger.error("In DatabaseConnector.queryAllServies()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
	    	return null;
	    }
	    finally{
	    	session.close();
	    }
	}
	
	/*query service's status
	 * */
	public String queryServiceStatus(String serviceName){
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    String serviceStatus = null;
	    
	    try{
	    	transaction = session.beginTransaction();
	    	String hql = "SELECT serviceStatus FROM Service WHERE serviceName = '" + serviceName + "'";
	    	Query query = session.createQuery(hql);
	    	if(query != null)
	    		serviceStatus = query.list().get(0).toString();
	
	        transaction.commit();
	        return serviceStatus;
	    }
	    catch(HibernateException exception){
	    	if(transaction != null) 
	    		transaction.rollback();
	    	logger.error("In DatabaseConnector.queryServiceStatus()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
	    	return serviceStatus;
	    }
	    finally{
	    	session.close();
	    }
	}
	
	/*update service's status
	 * */
	public boolean updateServiceStatus(Service service){
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    boolean updateResult = false;
		int serviceID = service.getServiceID();
		String serviceStatus = service.getServiceStatus();
	    
	    try{
	    	transaction = session.beginTransaction();
	    	
	    	Service serviceUpdate = (Service) session.get(Service.class, serviceID); 
	    	serviceUpdate.setServiceStatus(serviceStatus);
	    	session.update(serviceUpdate); 
	    	
	    	updateResult = true;
	        transaction.commit();
	        return updateResult;
	    }
	    catch(HibernateException exception){
	    	if(transaction != null) 
	    		transaction.rollback();
	    	logger.error("In DatabaseConnector.updateServiceStatus()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
	    	return updateResult;
	    }
	    finally{
	    	session.close();
	    }
	}

	/*query users' email address
	 * */
	public List<String> queryUserEmails(String userLevel){
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    List<String> queryResult = null;
	    
	    try{
	    	transaction = session.beginTransaction();
	    	String hql = "SELECT userEmail FROM ColfusionUsers WHERE userLevel = '" + userLevel + "'";
	    	Query query = session.createQuery(hql);
	    	if(query != null){
	    		@SuppressWarnings("unchecked")
	    		List<String> tempQueryResult = (List<String>)query.list();
	    		queryResult = tempQueryResult;
	    	}
	        transaction.commit();
	        return queryResult;
	    }
	    catch(HibernateException exception){
	    	if(transaction != null) 
	    		transaction.rollback();
	    	logger.error("In DatabaseConnector.queryUserEmails()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
	    	return queryResult;
	    }
	    finally{
	    	session.close();
	    }
	}
}