/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.GenericDAO;

/**
 * @author Evgeny
 * @param <T>
 *
 */
public abstract class GeneralManagerImpl<T extends Object, ID extends Serializable> implements GeneralManager<T, ID> {

	Logger logger = LogManager.getLogger(GeneralManagerImpl.class.getName());
	
	protected final GenericDAO<T, ID> _dao;
	private final Class<?> _clazz;
	
	protected GeneralManagerImpl(GenericDAO<T, ID>  dao, Class<?> clazz) {
		_dao = dao;
		_clazz = clazz;
	}
	
	private void checkIfDaoSet() throws Exception {
		if (_dao == null) {
			logger.error("Dao is not set up");
			//TODO: create custom exception
			throw new Exception("Dao is not set up");
		}
	}
	
	@Override
	public ID save(T entity) throws Exception {
		
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
	public void saveOrUpdate(T entity) throws Exception {
		
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
	public T merge(T entity) throws Exception {
		
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
	public void delete(T entity) throws Exception {
		
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
	public T findByID(ID id) throws Exception {
		
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

}
