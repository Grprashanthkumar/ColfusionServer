/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.dal.dao.GenericDAO;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author Evgeny
 * @param <T>
 *
 */
public abstract class GeneralManagerImpl<Dao extends GenericDAO<T, ID>, T extends Object, ID extends Serializable> implements GeneralManager<T, ID> {

	private static Logger logger = LogManager.getLogger(GeneralManagerImpl.class.getName());
	
	protected final Dao _dao;
	private final Class<?> _clazz;
	
	protected GeneralManagerImpl(final Dao dao, final Class<?> clazz) {
		_dao = dao;
		_clazz = clazz;
	}
	
	protected void checkIfDaoSet() {
		if (_dao == null) {
			logger.error("Dao is not set up");
			//TODO: create custom exception
			throw new RuntimeException("Dao is not set up");
		}
	}
	
	@Override
	public ID save(final T entity) throws Exception {
		
		checkIfDaoSet();
		
		try {
            HibernateUtil.beginTransaction();
            
            ID result = _dao.save(entity);
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public void saveOrUpdate(final T entity) throws Exception {
		
		checkIfDaoSet();
		
		try {
            HibernateUtil.beginTransaction();
            
            _dao.saveOrUpdate(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            
        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public T merge(final T entity) throws Exception {
		
		checkIfDaoSet();
		
		try {
            HibernateUtil.beginTransaction();
            
            T result = _dao.merge(entity);
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("merge failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("merge failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public void delete(final T entity) throws Exception {
		
		checkIfDaoSet();
		
		try {
            HibernateUtil.beginTransaction();
            
            _dao.delete(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("delete failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("delete failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public List<T> findAll() throws Exception {
		
		checkIfDaoSet();
		
		List<T> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = _dao.findAll(_clazz);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findAll failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findAll failed HibernateException", ex);
        	throw ex;
        }
		return result;
	}

	@Override
	public T findByID(final ID id) throws Exception {
		
		checkIfDaoSet();
		
		T result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = _dao.findByID(_clazz, id);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed HibernateException", ex);
        	throw ex;
        }
		return result;	
	}
	
	 public static <T extends Object> T initializeField(final T detachedParent, final String fieldName) throws NoSuchFieldException, IllegalAccessException  {
		 try {
			 //TODO:add a check whether field is already initialized
			 
	           HibernateUtil.beginTransaction();
	            
	            @SuppressWarnings("unchecked")
				T reattachedParent = (T) HibernateUtil.getSession().merge(detachedParent); 
	            
	            // get the field from the entity and initialize it
	            Field fieldToInitialize = detachedParent.getClass().getDeclaredField(fieldName);
	            fieldToInitialize.setAccessible(true);
	            Object objectToInitialize = fieldToInitialize.get(reattachedParent);

	            Hibernate.initialize(objectToInitialize);
	            
	            HibernateUtil.commitTransaction();
	            
	            return reattachedParent;
	        } catch (NonUniqueResultException ex) {

	        	HibernateUtil.rollbackTransaction();
	        	
	        	logger.error("findByID failed NonUniqueResultException", ex);
	        	throw ex;
	        } catch (HibernateException ex) {

	        	HibernateUtil.rollbackTransaction();
	        	
	        	logger.error("findByID failed HibernateException", ex);
	        	throw ex;
	        } catch (NoSuchFieldException e) {
	        	
	        	HibernateUtil.rollbackTransaction();
	        	
	        	logger.error(String.format("initializeField FAILED. No such field as %s.", fieldName), e);
	        	throw e;
			} catch (SecurityException e) {
				
				HibernateUtil.rollbackTransaction();
				
				logger.error(String.format("initializeField FAILED something wrong. Field name is %s.", fieldName), e);
				throw e;
			} catch (IllegalArgumentException e) {
				
				HibernateUtil.rollbackTransaction();
				
				logger.error(String.format("initializeField FAILED something wrong. Field name is %s.", fieldName), e);
				throw e;
			} catch (IllegalAccessException e) {
				
				HibernateUtil.rollbackTransaction();
				
				logger.error(String.format("initializeField FAILED something wrong. Field name is %s.", fieldName), e);
				throw e;
			}
	 }

}
