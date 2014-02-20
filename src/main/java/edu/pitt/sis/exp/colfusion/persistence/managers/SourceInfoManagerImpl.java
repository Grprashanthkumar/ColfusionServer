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
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;

/**
 * @author Evgeny
 *
 */
public class SourceInfoManagerImpl implements SourceInfoManager {

	Logger logger = LogManager.getLogger(SourceInfoManagerImpl.class.getName());
	
	private SourceInfoDAO sourceInfoDAO = new SourceInfoDAOImpl();
	
	//***************************************
	// General manager interface
	//***************************************
	
	@Override
	public void save(ColfusionSourceinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            sourceInfoDAO.save(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        }
	}

	@Override
	public void merge(ColfusionSourceinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            sourceInfoDAO.merge(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("merge failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("merge failed HibernateException", ex);
        }
	}

	@Override
	public void delete(ColfusionSourceinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            sourceInfoDAO.delete(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("delete failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("delete failed HibernateException", ex);
        }
	}

	@Override
	public List<ColfusionSourceinfo> findAll() {
		List<ColfusionSourceinfo> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = sourceInfoDAO.findAll(ColfusionSourceinfo.class);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findAll failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findAll failed HibernateException", ex);
        }
		return result;
	}

	@Override
	public ColfusionSourceinfo findByID(Integer id) {
		ColfusionSourceinfo result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = sourceInfoDAO.findByID(ColfusionSourceinfo.class, id);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findByID failed HibernateException", ex);
        }
		return result;		
	}
	
	
	
	//************************************
	// SOURCINFOMANAGER interface
	//***********************************

	@Override
	public List<ColfusionSourceinfo> findByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColfusionSourceinfo> findBySidOrTitle(
			String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColfusionSourceinfo findBySid(int sid, boolean includeDraft) {
		ColfusionSourceinfo sourceinfo = null;
        try {
            HibernateUtil.beginTransaction();
            
            sourceinfo = sourceInfoDAO.findDatasetInfoBySid(sid, includeDraft);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        }
        return sourceinfo;
	}

	@Override
	public List<ColfusionSourceinfo> findByTitle(String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}
}
