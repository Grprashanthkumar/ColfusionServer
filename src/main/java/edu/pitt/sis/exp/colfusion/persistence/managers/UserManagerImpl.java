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
import edu.pitt.sis.exp.colfusion.persistence.dao.UsersDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.UsersDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;

/**
 * @author Evgeny
 *
 */
public class UserManagerImpl implements UserManager {

	Logger logger = LogManager.getLogger(UserManagerImpl.class.getName());
	
	private UsersDAO userDAO = new UsersDAOImpl();
	
	//***************************************
	// General manager interface
	//***************************************
	
	@Override
	public Integer save(ColfusionUsers entity) {
		try {
            HibernateUtil.beginTransaction();
            
            Integer result = userDAO.save(entity);
            
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
	public void saveOrUpdate(ColfusionUsers entity) {
		try {
            HibernateUtil.beginTransaction();
            
            userDAO.saveOrUpdate(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        }
	}

	@Override
	public ColfusionUsers merge(ColfusionUsers entity) {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionUsers result = userDAO.merge(entity);
            
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
	public void delete(ColfusionUsers entity) {
		try {
            HibernateUtil.beginTransaction();
            
            userDAO.delete(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("delete failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("delete failed HibernateException", ex);
        }
	}

	@Override
	public List<ColfusionUsers> findAll() {
		List<ColfusionUsers> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = userDAO.findAll(ColfusionUsers.class);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findAll failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findAll failed HibernateException", ex);
        }
		return result;
	}

	@Override
	public ColfusionUsers findByID(Integer id) {
		ColfusionUsers result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = userDAO.findByID(ColfusionUsers.class, id);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findByID failed HibernateException", ex);
        }
		return result;		
	}

	//***************************************
	// From UserManager interface
	//***************************************
	
	@Override
	public List<ColfusionUsers> lookUpUser(String searchTerm, int limit) {
		try {
            HibernateUtil.beginTransaction();
            
            List<ColfusionUsers> result = userDAO.lookUpUser(searchTerm, limit);
            
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

}
