/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;



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
		}
	}
 

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	 
	public static Session beginTransaction() {
		Session hibernateSession = HibernateUtil.getSession();
		hibernateSession.beginTransaction();
		return hibernateSession;
	}
	 
	public static void commitTransaction() {
		HibernateUtil.getSession().getTransaction().commit();
	}
	 
	public static void rollbackTransaction() {
		HibernateUtil.getSession().getTransaction().rollback();
	}
	 
	public static void closeSession() {
		HibernateUtil.getSession().close();
	}
	 
	public static Session getSession() {
		Session hibernateSession = sessionFactory.getCurrentSession();
		return hibernateSession;
	}
}
