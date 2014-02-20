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
	public void save(ColfusionLinks entity) {
		try {
            HibernateUtil.beginTransaction();
            
            linksDAO.save(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        }
	}

	@Override
	public void merge(ColfusionLinks entity) {
		try {
            HibernateUtil.beginTransaction();
            
            linksDAO.merge(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("merge failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("merge failed HibernateException", ex);
        }
	}

	@Override
	public void delete(ColfusionLinks entity) {
		try {
            HibernateUtil.beginTransaction();
            
            linksDAO.delete(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("delete failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("delete failed HibernateException", ex);
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
            logger.error("findAll failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findAll failed HibernateException", ex);
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
            logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findByID failed HibernateException", ex);
        }
		return result;		
	}

}
