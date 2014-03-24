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
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoTableKTRDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoTableKTRDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtrId;

/**
 * @author Evgeny
 *
 */
public class SourceInfoTableKTRManagerImpl implements SourceInfoTableKTRManager {

	Logger logger = LogManager.getLogger(SourceInfoTableKTRManagerImpl.class.getName());
	
	private SourceInfoTableKTRDAO sourceInfoTableKTRDAO  = new SourceInfoTableKTRDAOImpl();
	
	//***************************************
	// General manager interface
	//***************************************
	
	
	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#save(java.lang.Object)
	 */
	@Override
	public ColfusionSourceinfoTableKtrId save(ColfusionSourceinfoTableKtr entity) {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionSourceinfoTableKtrId result = sourceInfoTableKTRDAO.save(entity);
            
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

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(ColfusionSourceinfoTableKtr entity) {
		try {
            HibernateUtil.beginTransaction();
            
            sourceInfoTableKTRDAO.saveOrUpdate(entity);
            
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

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#merge(java.lang.Object)
	 */
	@Override
	public ColfusionSourceinfoTableKtr merge(ColfusionSourceinfoTableKtr entity) {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionSourceinfoTableKtr result = sourceInfoTableKTRDAO.merge(entity);
            
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

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#delete(java.lang.Object)
	 */
	@Override
	public void delete(ColfusionSourceinfoTableKtr entity) {
		try {
            HibernateUtil.beginTransaction();
            
            sourceInfoTableKTRDAO.delete(entity);
            
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

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#findAll()
	 */
	@Override
	public List<ColfusionSourceinfoTableKtr> findAll() {
		List<ColfusionSourceinfoTableKtr> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = sourceInfoTableKTRDAO.findAll(ColfusionSourceinfoTableKtr.class);
            
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

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#findByID(java.lang.Object)
	 */
	@Override
	public ColfusionSourceinfoTableKtr findByID(ColfusionSourceinfoTableKtrId id) {
		ColfusionSourceinfoTableKtr result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = sourceInfoTableKTRDAO.findByID(ColfusionSourceinfoTableKtr.class, id);
            
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