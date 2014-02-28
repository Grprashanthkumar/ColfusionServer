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
import edu.pitt.sis.exp.colfusion.persistence.dao.DNameInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.DNameInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionLinks;

/**
 * @author Evgeny
 *
 */
public class DNameInfoManagerImpl implements DNameInfoManager {

	Logger logger = LogManager.getLogger(DNameInfoManagerImpl.class.getName());
	
	private DNameInfoDAO dNameInfoDAO = new DNameInfoDAOImpl();
	
	//***************************************
	// General manager interface
	//***************************************
	
	@Override
	public Integer save(ColfusionDnameinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            Integer result = dNameInfoDAO.save(entity);
            
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
	public void saveOrUpdate(ColfusionDnameinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            dNameInfoDAO.saveOrUpdate(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        }
	}

	@Override
	public ColfusionDnameinfo merge(ColfusionDnameinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionDnameinfo result = dNameInfoDAO.merge(entity);
            
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

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#delete(java.lang.Object)
	 */
	@Override
	public void delete(ColfusionDnameinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            dNameInfoDAO.delete(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("delete failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("delete failed HibernateException", ex);
        }
	}

	@Override
	public List<ColfusionDnameinfo> findAll() {
		List<ColfusionDnameinfo> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = dNameInfoDAO.findAll(ColfusionDnameinfo.class);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findAll failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findAll failed HibernateException", ex);
        }
		return result;
	}

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#findByID(java.lang.Object)
	 */
	@Override
	public ColfusionDnameinfo findByID(Integer id) {
		ColfusionDnameinfo result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = dNameInfoDAO.findByID(ColfusionLinks.class, id);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findByID failed HibernateException", ex);
        }
		return result;		
	}

}
