package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.LinksDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.LinksDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionLinks;

public class LinksManagerImpl implements LinksManager {

	Logger logger = LogManager.getLogger(SourceInfoManagerImpl.class.getName());
	
	private LinksDAO linksDAO = new LinksDAOImpl();
	
	//***************************************
	// General manager interface
	//***************************************
	
	@Override
	public Integer save(ColfusionLinks entity) throws NonUniqueResultException, HibernateException {
		try {
            HibernateUtil.beginTransaction();
            
            Integer result = linksDAO.save(entity);
            
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
	public void saveOrUpdate(ColfusionLinks entity) {
		try {
            HibernateUtil.beginTransaction();
            
            linksDAO.saveOrUpdate(entity);
            
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
	public ColfusionLinks merge(ColfusionLinks entity) throws NonUniqueResultException, HibernateException {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionLinks result = linksDAO.merge(entity);
            
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
	public void delete(ColfusionLinks entity) {
		try {
            HibernateUtil.beginTransaction();
            
            linksDAO.delete(entity);
            
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
	public List<ColfusionLinks> findAll() {
		List<ColfusionLinks> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = linksDAO.findAll(ColfusionLinks.class);
            
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
	public ColfusionLinks findByID(Integer id) {
		ColfusionLinks result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = linksDAO.findByID(ColfusionLinks.class, id);
            
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