/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.ExecutionInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.ExecutionInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionExecuteinfo;

/**
 * @author Evgeny
 *
 */
public class ExecutionInfoManagerImpl implements ExecutionInfoManager {

	Logger logger = LogManager.getLogger(ExecutionInfoManagerImpl.class.getName());
	
	private ExecutionInfoDAO executionInfoDAO = new ExecutionInfoDAOImpl();
	
	//***************************************
	// General manager interface
	//***************************************
	
	@Override
	public Integer save(ColfusionExecuteinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            Integer result = executionInfoDAO.save(entity);
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public void saveOrUpdate(ColfusionExecuteinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            executionInfoDAO.saveOrUpdate(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        }
	}

	@Override
	public ColfusionExecuteinfo merge(ColfusionExecuteinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionExecuteinfo result = executionInfoDAO.merge(entity);
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {
            logger.error("merge failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {
        	logger.error("merge failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public void delete(ColfusionExecuteinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            executionInfoDAO.delete(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("delete failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("delete failed HibernateException", ex);
        }
	}

	@Override
	public List<ColfusionExecuteinfo> findAll() {
		List<ColfusionExecuteinfo> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = executionInfoDAO.findAll(ColfusionExecuteinfo.class);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findAll failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findAll failed HibernateException", ex);
        }
		return result;
	}

	@Override
	public ColfusionExecuteinfo findByID(Integer id) {
		ColfusionExecuteinfo result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = executionInfoDAO.findByID(ColfusionExecuteinfo.class, id);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findByID failed HibernateException", ex);
        }
		return result;		
	}

}
