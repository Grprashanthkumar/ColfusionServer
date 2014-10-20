package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

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
	
	private Logger logger = LogManager.getLogger(DatabaseConnection.class.getName());

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
	/*
	 ***************** IN PROGRESS**************************
	 * */
	public boolean queryServieExistance(String serviceName){
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    boolean queryResult = false;
	    
	    try{
	    	transaction = session.beginTransaction();
	    	String hql = "FROM Service WHERE serviceName = " + serviceName;
	    	List<Service> serviceList = session.createQuery(hql).list();
	    	
	    	if(serviceList.size() != 0)
	    		queryResult = true;
	
	        transaction.commit();
	        session.close();
	        return queryResult;
	    }
	    catch(HibernateException exception){
	    	if(transaction != null) 
	    		transaction.rollback();
	    	logger.error("In DatabaseConnector.queryServieExistance()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
	    	session.close();
	    	return queryResult;
	    }
	}
	
	/*query all services in database
	 * */
	public List<Service> queryAllServies(){
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
	    List<Service> serviceList = null;
	    
	    try{
	    	transaction = session.beginTransaction();
	    	serviceList = session.createQuery("FROM Service").list(); 
	    	
	        transaction.commit();
	        session.close();
	        return serviceList;
	    }
	    catch(HibernateException exception){
	    	if(transaction != null) 
	    		transaction.rollback();
	    	logger.error("In DatabaseConnector.queryAllServies()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
	    	session.close();
	    	return null;
	    }
	}
	
	/*query service's status
	 * */
	public String queryServiceStatus(String serviceName){
		return null;
	}
	
	/*update service's status
	 * */
	public boolean updateServiceStatus(Service service){
		return true;
	}

	/*query users' email address
	 * */
	public List<String> queryUserEmails(String userLevel){
		return null;
	}
}