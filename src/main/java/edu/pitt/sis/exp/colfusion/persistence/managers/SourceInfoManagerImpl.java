/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.LinksDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.LinksDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.UsersDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.UsersDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionLinks;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;

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
	public Integer save(ColfusionSourceinfo entity) throws NonUniqueResultException, HibernateException {
		try {
            HibernateUtil.beginTransaction();
            
            Integer result = sourceInfoDAO.save(entity);
            
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
	public void saveOrUpdate(ColfusionSourceinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            sourceInfoDAO.saveOrUpdate(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        }
	}

	@Override
	public ColfusionSourceinfo merge(ColfusionSourceinfo entity) throws NonUniqueResultException, HibernateException {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionSourceinfo result = sourceInfoDAO.merge(entity);
            
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

	/**
	 * Creates new story with given input parameters and stores it in the database and the resulting entity has automatically generated sid.
	 * 
	 * @param userId is the id of the user who is creating the new story.
	 * @param date when the new story is created.
	 * @param source_type type of the source from which the data will be imported.
	 * @return newly created story which is stored in the db. Has auto generated sid.
	 */
	@Override
	public ColfusionSourceinfo newStory(int userId, Date date, String source_type) throws NonUniqueResultException, HibernateException {
		try {
            HibernateUtil.beginTransaction();
            
            UsersDAO usersDAO = new UsersDAOImpl();
            
            ColfusionUsers userCreator = usersDAO.findByID(ColfusionUsers.class, userId);
            
            ColfusionSourceinfo newStoryEntity = new ColfusionSourceinfo(userCreator, date, source_type);
            
            sourceInfoDAO.save(newStoryEntity);
            
           // newStoryEntity.setSid(sid);
            
            HibernateUtil.commitTransaction();
            
            return newStoryEntity;
        } catch (NonUniqueResultException ex) {
            logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }
	}

	/**
	 * Updates both sourceinfo and links table with story metadata.
	 * 
	 * @param metadata the metadata to be used to update the tables in database.
	 */
	@Override
	public void updateStory(StoryMetadataViewModel metadata) {
		try {
            HibernateUtil.beginTransaction();
            
            UsersDAO usersDAO = new UsersDAOImpl();
            
            ColfusionUsers userCreator = usersDAO.findByID(ColfusionUsers.class, metadata.getUserId());
            
            ColfusionSourceinfo newStoryEntity = new ColfusionSourceinfo(userCreator, metadata.getDateSubmitted(), metadata.getSourceType());
            newStoryEntity.setSid(metadata.getSid());
            newStoryEntity.setTitle(metadata.getTitle());
            newStoryEntity.setStatus(metadata.getStatus());
            
            sourceInfoDAO.saveOrUpdate(newStoryEntity);
            
            ColfusionLinks newLink = new ColfusionLinks(metadata.getSid(), metadata.getUserId(), 0, 0, 0, 0, new BigDecimal(0.0), metadata.getDateSubmitted(), metadata.getDateSubmitted(), 
            		metadata.getDateSubmitted(), 0, 1, 0, metadata.getStatus(), 0);
            
            LinksDAO linksDAO = new LinksDAOImpl();
            linksDAO.saveOrUpdate(newLink);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }
	}
}
