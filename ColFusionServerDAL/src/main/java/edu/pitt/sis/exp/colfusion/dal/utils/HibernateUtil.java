/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;



public class HibernateUtil {
 
	private static final Logger logger = LogManager.getLogger(HibernateUtil.class.getName());
	private static SessionFactory sessionFactory;
	
	static {
		try {
			Configuration cfg = new Configuration().configure("hibernate.cfg.xml");         
	        StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
	        sb.applySettings(cfg.getProperties());
	        StandardServiceRegistry standardServiceRegistry = sb.build();                   
	        sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
		} catch (Throwable ex) {
			// Log the exception.
			logger.error("sessionFactory initialization failed!", ex);
			throw new RuntimeException(ex);
		}
	}
 
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	 
	public static Session beginTransaction() throws HibernateException {
		try {
			Session hibernateSession = HibernateUtil.getSession();
			
			if (hibernateSession.getTransaction() != null
		            && hibernateSession.getTransaction().isActive()) {

				//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
				logger.error("beginTransaction hibernateSession.getTransaction() != null and hibernateSession.getTransaction().isActive() is TURE. Does it mean "
						+ "the code is trying to open nested tractions? Check!!!.");
				
				hibernateSession.getTransaction();
		    } else {
		    	hibernateSession.beginTransaction();
		    }
			
			return hibernateSession;
		}
		catch (Exception e) {
			logger.error("beginTransaction failed. ", e);
			
			throw new HibernateException(e);
		}
	}
	 
	public static void commitTransaction() throws HibernateException {
		try {
		
			if (HibernateUtil.getSession().getTransaction() != null) {
				org.hibernate.Transaction trans = HibernateUtil.getSession().getTransaction();
				
				trans.commit();
				
				if (!trans.wasCommitted()) {
				
					//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
					logger.error("commitTransaction trans.wasCommitted() is false. Starting while loop until wasCommitted is true. Might lead for inf loop though, maybe put break after a some time.");
					
					while (!trans.wasCommitted()) {
						
					}
					
					//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
					logger.error("commitTransaction: after while (!trans.wasCommitted()).");					
				}
				
			}
			else {
				logger.error("commitTransaction was trying to commit but HibernateUtil.getSession().getTransaction() equals NULL");
				//TODO: maybe here we need to throw an error, because the data was not committed.
			}
		}
		catch (Exception e) {
			logger.error("commitTransaction failed", e);
			
			//TODO should be throw e furher?
			
			throw new HibernateException(e);
		}
	}
	 
	public static void rollbackTransaction() {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
		} catch (Exception e1) {
			logger.error("rollbackTransaction failed on HibernateUtil.getSession()", e1);
			
			//TODO: maybe we should throw e1 here further.
			return;
		}
		
		if (session.getTransaction() != null) {
			try {
				org.hibernate.Transaction trans = HibernateUtil.getSession().getTransaction();
				
				trans.rollback();
				
				if (!trans.wasRolledBack()) {
				
					//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
					logger.error("rollbackTransaction trans.rollback() is false. Starting while loop until wasRolledBack is true. Might lead for inf loop though, maybe put break after a some time.");
					
					while (!trans.wasRolledBack()) {
						
					}
					
					//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
					logger.error("rollbackTransaction: after while (!trans.wasRolledBack()).");					
				}
			} catch (Exception e) {
				logger.error("rollbackTransaction: HibernateUtil.getSession().getTransaction().rollback()  failed even though HibernateUtil.getSession().getTransaction() not equals", e);
			}
			
		}
		else {
			logger.error("rollbackTransaction: HibernateUtil.getSession().getTransaction() equals NULL");
		}
	}
	 
	public static void closeSession() throws HibernateException, Exception {
		HibernateUtil.getSession().close();
	}
	 
	/**
	 * Gets current session.
	 * 
	 * Right now it logs if something failed and also propagates the exception, but this is only for testing. Later this method will just throw exception if something happened, without logging.
	 * @return current session.
	 * @throws Exception
	 */
	public static Session getSession() throws HibernateException {
		try {
			
			if (sessionFactory == null) {
				logger.error("getSession: sessionFactory == null, need to do something here");
				
				throw new Exception("getSession: sessionFactory == null, need to do something here");
			}
			
			Session hibernateSession = sessionFactory.getCurrentSession();
			return hibernateSession;
		} catch (Exception e) {
			logger.error("getSession failed", e);
			
			throw new HibernateException(e);
		}
	}
}
