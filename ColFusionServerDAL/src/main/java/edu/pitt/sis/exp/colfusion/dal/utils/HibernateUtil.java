/**
 *
 */
package edu.pitt.sis.exp.colfusion.dal.utils;

import java.io.InputStream;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.ResourceUtils;
import edu.pitt.sis.exp.colfusion.utils.StringUtils;

public class HibernateUtil {

	private static final Logger logger = LogManager.getLogger(HibernateUtil.class.getName());

	private static final String COLFUSION_PREFIX = "colfusion.";

	/**
	 * Prefix of all colfusion hibernate properties.
	 */
	//TODO: Maybe it is better to switch to different config format (e.g. https://github.com/typesafehub/config#using-hocon-the-json-superset)
	private static final String COLFUSION_HIBERNATE_PROPERTIES_PREFIX = "colfusion.hibernate";
	private static SessionFactory sessionFactory;

	static {
		initiSessionFactory();
	}

	/**
	 * @throws RuntimeException
	 */
	public static void initiSessionFactory() throws RuntimeException {
		try {
			//TODO FIXME: workaround to read a file from jar. First copy to temp location then provide file from there, then delete the temp file.

			final InputStream xmlInput = ResourceUtils.getResourceAsStream(HibernateUtil.class, "hibernate.cfg.xml");
			//			final File temp = File.createTempFile("colfusion", "hibernateProp");
			//IOUtils.writeToFile(xmlInput, temp, true);
			final Document doc = IOUtils.readXMLDocument(xmlInput);

			final Configuration cfg = new Configuration().configure(doc);

			//			temp.delete();

			setProperties(cfg);

			cfg.setNamingStrategy(DefaultQuotedNamingStrategy.INSTANCE);

			final StandardServiceRegistryBuilder sb = new StandardServiceRegistryBuilder();
			sb.applySettings(cfg.getProperties());
			final StandardServiceRegistry standardServiceRegistry = sb.build();
			sessionFactory = cfg.buildSessionFactory(standardServiceRegistry);
		} catch (final Throwable ex) {
			// Log the exception.
			logger.error("sessionFactory initialization failed!", ex);
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Set hibernate properties from {@link ConfigManager}
	 *
	 * @param cfg
	 * 			hibernate configuration to set properties to
	 */
	private static void setProperties(final Configuration cfg) {
		final Set<PropertyKeys> hibernateProperties = PropertyKeys.getPropertiesForPrefix(COLFUSION_HIBERNATE_PROPERTIES_PREFIX);
		final ConfigManager configMng = ConfigManager.getInstance();

		for (final PropertyKeys propertyKey : hibernateProperties) {
			cfg.setProperty(removeColfuionPrefix(propertyKey.getKey()), configMng.getProperty(propertyKey));
			logger.info(String.format("Set %s = %s", removeColfuionPrefix(propertyKey.getKey()), configMng.getProperty(propertyKey)));
		}
	}

	/**
	 * Removes the colfusion. prefix from the given key.
	 * E.g. given the key colfusion.hibernate.default_catalog the result will be hibernate.default_catalog
	 * @param hibernateConnectionDriverClass
	 * @return
	 */
	static String removeColfuionPrefix(
			final String hibernateConnectionDriverClass) {

		return StringUtils.removePrefix(hibernateConnectionDriverClass, COLFUSION_PREFIX);
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void beginTransaction() throws HibernateException {
		try {
			final Session hibernateSession = HibernateUtil.getSession();

			if (hibernateSession.getTransaction() != null
					&& hibernateSession.getTransaction().isActive()) {

				//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
				logger.error("beginTransaction hibernateSession.getTransaction() != null and hibernateSession.getTransaction().isActive() is TURE. Does it mean "
						+ "the code is trying to open nested tractions? Check!!!.");

				final Transaction transaction = hibernateSession.getTransaction();
				logger.info(String.format("beginTransaction: There is an active transaction %s", transaction));
			} else {
				final Transaction transaction = hibernateSession.beginTransaction();
				//		    	logger.info(String.format("beginTransaction: Began transaction %s", transaction));
			}
		}
		catch (final Exception e) {
			logger.error("beginTransaction failed. ", e);

			throw new HibernateException(e);
		}
	}

	public static void commitTransaction() throws HibernateException {
		try {

			if (HibernateUtil.getSession().getTransaction() != null) {
				final org.hibernate.Transaction trans = HibernateUtil.getSession().getTransaction();

				trans.commit();

				if (!trans.wasCommitted()) {

					//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
					logger.error("commitTransaction trans.wasCommitted() is false. Starting while loop until wasCommitted is true. Might lead for inf loop though, maybe put break after a some time.");

					while (!trans.wasCommitted()) {

					}

					//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
					logger.error("commitTransaction: after while (!trans.wasCommitted()).");
				}

				final String message = String.format("commitTransaction: commited trasaction %s", trans);
				//				logger.info(message);
			}
			else {
				logger.error("commitTransaction was trying to commit but HibernateUtil.getSession().getTransaction() equals NULL");
				//TODO: maybe here we need to throw an error, because the data was not committed.
			}
		}
		catch (final Exception e) {
			logger.error("commitTransaction failed", e);

			//TODO should be throw e further?

			throw new HibernateException(e);
		}
	}

	public static void rollbackTransaction() {
		Session session = null;
		try {
			session = HibernateUtil.getSession();
		} catch (final Exception e1) {
			logger.error("rollbackTransaction failed on HibernateUtil.getSession()", e1);

			//TODO: maybe we should throw e1 here further.
			return;
		}

		if (session.getTransaction() != null) {
			try {
				final org.hibernate.Transaction trans = HibernateUtil.getSession().getTransaction();

				trans.rollback();

				if (!trans.wasRolledBack()) {

					//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
					logger.error("rollbackTransaction trans.rollback() is false. Starting while loop until wasRolledBack is true. Might lead for inf loop though, maybe put break after a some time.");

					while (!trans.wasRolledBack()) {

					}

					//TODO change from Error to INFO. right now use error, because I want to receive an email when it happens.
					logger.error("rollbackTransaction: after while (!trans.wasRolledBack()).");
				}

				final String message = String.format("commitTransaction: commited trasaction %s", trans);
				logger.info(message);
			} catch (final Exception e) {
				logger.error("rollbackTransaction: HibernateUtil.getSession().getTransaction().rollback()  failed even though HibernateUtil.getSession().getTransaction() not equals", e);
			}

		}
		else {
			logger.error("rollbackTransaction: HibernateUtil.getSession().getTransaction() equals NULL");
		}
	}

	/**
	 * Gets current session.
	 *
	 * Right now it logs if something failed and also propagates the exception, but this is only for testing. Later this method will just throw exception if something happened, without logging.
	 * @return current session.
	 * @throws Exception
	 */
	public static Session getSession() throws HibernateException {

		if (sessionFactory == null) {
			final String message = String.format("getSession: sessionFactory == null, need to do something here");
			logger.error(message);

			throw new NullPointerException(message);
		}

		try {
			final Session hibernateSession = sessionFactory.getCurrentSession();
			return hibernateSession;
		} catch (final Exception e) {
			logger.error("getSession failed", e);

			throw new HibernateException(e);
		}
	}

	/**
	 * Checks whether there is an active transaction.
	 *
	 * @return true if there is an active transaction, otherwise false
	 */
	public static boolean isTransactionOpen() {
		final Session hibernateSession = HibernateUtil.getSession();

		if (hibernateSession == null) {
			return false;
		}

		return hibernateSession.getTransaction() != null && hibernateSession.getTransaction().isActive();
	}
}
