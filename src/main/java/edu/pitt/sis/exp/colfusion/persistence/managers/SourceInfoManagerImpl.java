/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.LinksDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.LinksDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoMetadataEditHistoryDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoMetadataEditHistoryDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceinfoUserRolesDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceinfoUserRolesDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.TagsCacheDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.TagsCacheDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.TagsDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.TagsDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.UserRolesDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.UserRolesDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.UsersDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.UsersDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionLinks;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoUser;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoUserId;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionTags;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionTagsId;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUserroles;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryAuthorViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;


/**
 * @author Evgeny
 *
 */
public class SourceInfoManagerImpl implements SourceInfoManager {

	Logger logger = LogManager.getLogger(SourceInfoManagerImpl.class.getName());
	
	private SourceInfoDAO sourceInfoDAO = new SourceInfoDAOImpl();
	
	public enum HistoryItem {
	    TITLE("title"), DESCRIPTION("description"), TAGS("tags"), STATUS("status");
	    
	    private String value;

	    private HistoryItem(String value) {
	            this.value = value;
	    }
	    
	    public String getValue(){
	    	return this.value;
	    }
	};
	
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
            
            Hibernate.initialize(result.getColfusionUsers());
            
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
            
            Hibernate.initialize(sourceinfo.getColfusionUsers());
            Hibernate.initialize(sourceinfo.getColfusionSourceinfoUsers());
            
            for (Object sourceInfoUserObj : sourceinfo.getColfusionSourceinfoUsers().toArray()) {
    			ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;
    			Hibernate.initialize(sourceInfoUser.getColfusionUsers());
    		}
            
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
	@SuppressWarnings("unchecked")
	@Override
	public ColfusionSourceinfo newStory(int userId, Date date, String source_type) throws NonUniqueResultException, HibernateException {
		try {
            HibernateUtil.beginTransaction();
            
            UsersDAO usersDAO = new UsersDAOImpl();
            
            ColfusionUsers userCreator = usersDAO.findByID(ColfusionUsers.class, userId);
            
            ColfusionSourceinfo newStoryEntity = new ColfusionSourceinfo(userCreator, date, source_type);
            
            int sid = sourceInfoDAO.save(newStoryEntity);
            
            //Add user as submitter/contributer for newly created story.
            
            UserRolesDAO userRolesDAO = new UserRolesDAOImpl();
            // 1 is for contributor/submitter, TODO: maybe we should use enum here not hard wired value.
            ColfusionUserroles userRole = userRolesDAO.findByID(ColfusionUserroles.class, 1);
            
            SourceinfoUserRolesDAO sourceinfoUserRoles = new SourceinfoUserRolesDAOImpl();
            
            
            ColfusionSourceinfoUser colfusionSourceinfoUser = new ColfusionSourceinfoUser(new ColfusionSourceinfoUserId(sid, userCreator.getUserId(), userRole.getRoleId()), 
            														newStoryEntity, userRole, userCreator);
            
            sourceinfoUserRoles.save(colfusionSourceinfoUser);
            
            newStoryEntity.getColfusionSourceinfoUsers().add(colfusionSourceinfoUser);
            //sourceInfoDAO.saveOrUpdate(newStoryEntity);
            
            Hibernate.initialize(newStoryEntity.getColfusionUsers());
            
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
            
            ColfusionSourceinfo addedUpdatedStory = updateSourceInfo(metadata);
            updateUserRoles(addedUpdatedStory, metadata);
            updateLink(metadata);
            updateTags(metadata);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }
	}

	private ColfusionSourceinfo updateSourceInfo(StoryMetadataViewModel metadata) {
		UsersDAO usersDAO = new UsersDAOImpl();
        
        ColfusionUsers userCreator = usersDAO.findByID(ColfusionUsers.class, metadata.getStorySubmitter().getUserId());
        
        ColfusionSourceinfo newStoryEntity = new ColfusionSourceinfo(userCreator, metadata.getDateSubmitted(), metadata.getSourceType());
        newStoryEntity.setSid(metadata.getSid());
        newStoryEntity.setTitle(metadata.getTitle());
        newStoryEntity.setStatus(metadata.getStatus());
        
        handleHistoryEdits(newStoryEntity, metadata.getUserId());
        sourceInfoDAO.merge(newStoryEntity);
        
        return newStoryEntity;
	}
	
	//TODO: should we move the next two methods to other class? together with enum?
	/**
	 * Checks if new instance of a record of sourceinfo has title and status difference from the one stored in the database already.
	 * If they do differ, then old value is copied into history of edits table before replaced by new value.
	 * 
	 * @param newStory new instance of the sourceinfo record possibly updated by user during edit operation.
	 * @param userId id of the user who did edit (note, it is not the submitter, it is id of the user who was logged in and performed the edit).
	 */
	private void handleHistoryEdits(ColfusionSourceinfo newStory, int userId) {
		SourceInfoMetadataEditHistoryDAO editHistorDAO = new SourceInfoMetadataEditHistoryDAOImpl();
		
		ColfusionSourceinfo oldStory = sourceInfoDAO.findByID(ColfusionSourceinfo.class, newStory.getSid());
		
		String oldValue = (oldStory == null) ? null : oldStory.getTitle();
		editHistorDAO.saveHistoryIfChanged(newStory.getSid(), userId, oldValue, newStory.getTitle(), HistoryItem.TITLE,  "");
		
		oldValue = (oldStory == null) ? null : oldStory.getStatus();
		editHistorDAO.saveHistoryIfChanged(newStory.getSid(), userId, oldValue, newStory.getStatus(), HistoryItem.STATUS,  "");
	}
	
	/**
	 * Checks if new instance of a record of links has content/description and tags difference from the one stored in the database already.
	 * If they do differ, then old value is copied into history of edits table before replaced by new value.
	 * 
	 * @param newLink new instance of the links record possibly updated by user during edit operation.
	 * @param userId id of the user who did edit (note, it is not the submitter, it is id of the user who was logged in and performed the edit).
	 */
	private void handleHistoryEdits(ColfusionLinks newLink, int userId) {
		SourceInfoMetadataEditHistoryDAO editHistorDAO = new SourceInfoMetadataEditHistoryDAOImpl();
		
		LinksDAO linksDAO = new LinksDAOImpl();
		ColfusionLinks oldLink = linksDAO.findByID(ColfusionLinks.class, newLink.getLinkId());
		
		String oldValue = (oldLink == null) ? null : oldLink.getLinkContent();
		editHistorDAO.saveHistoryIfChanged(newLink.getLinkId(), userId, oldValue, newLink.getLinkContent(), HistoryItem.DESCRIPTION,  "");
		
		oldValue = (oldLink == null) ? null : oldLink.getLinkTags();
		editHistorDAO.saveHistoryIfChanged(newLink.getLinkId(), userId, oldValue, newLink.getLinkTags(), HistoryItem.TAGS,  "");
	}
	
	
	/**
	 * Adding or updating information about roles of users for a given dataset/story.
	 * @param addedUpdatedStory {@link ColfusionSourceinfo} is the story for which information about user and their roles needs to be added/updated.
	 * @param metadata {@link StoryMetadataViewModel} holds the information which need to be inserted/updated in the db.
	 */
	private void updateUserRoles(ColfusionSourceinfo addedUpdatedStory, StoryMetadataViewModel metadata) {
		UsersDAO usersDAO = new UsersDAOImpl();
		SourceinfoUserRolesDAO userStoryRolesDAO = new SourceinfoUserRolesDAOImpl();
        UserRolesDAO userRolesDAO = new UserRolesDAOImpl();
		
        logger.info("started updatingUserRolesFor story");
        
        //TODO: add checks if user or role could be found
		for (StoryAuthorViewModel author : metadata.getStoryAuthors()) {
			ColfusionUsers user = usersDAO.findByID(ColfusionUsers.class, author.getUserId());
			
			if (author.getRoleId() <= 0) {
				//TODO:handle it better.
				continue;
			}
			ColfusionUserroles userRole = userRolesDAO.findByID(ColfusionUserroles.class, author.getRoleId());
			
			if (userRole == null) {
				//TODO: handle it better
				continue;
			}
			ColfusionSourceinfoUserId colfusionSourceinfoUserId = new ColfusionSourceinfoUserId(addedUpdatedStory.getSid(), user.getUserId(), userRole.getRoleId());
			
			ColfusionSourceinfoUser userRolesInStory = new ColfusionSourceinfoUser(colfusionSourceinfoUserId, addedUpdatedStory, userRole, user);
			
			userStoryRolesDAO.saveOrUpdate(userRolesInStory);
		}
		
		for (StoryAuthorViewModel author : metadata.getRemovedStoryAuthors()) {
			ColfusionUsers user = usersDAO.findByID(ColfusionUsers.class, author.getUserId());
			
			if (author.getRoleId() <= 0) {
				//TODO:handle it better.
				continue;
			}
			ColfusionUserroles userRole = userRolesDAO.findByID(ColfusionUserroles.class, author.getRoleId());
			
			if (userRole == null) {
				//TODO: handle it better
				continue;
			}
			ColfusionSourceinfoUserId colfusionSourceinfoUserId = new ColfusionSourceinfoUserId(addedUpdatedStory.getSid(), user.getUserId(), userRole.getRoleId());
			
			ColfusionSourceinfoUser userRolesInStory = new ColfusionSourceinfoUser(colfusionSourceinfoUserId, addedUpdatedStory, userRole, user);
			
			userStoryRolesDAO.delete(userRolesInStory);
		}
		
        logger.info("finished updatingUserRolesFor story");
	}

	private void updateLink(StoryMetadataViewModel metadata) {
		ColfusionLinks newLink = new ColfusionLinks(metadata.getSid(), metadata.getStorySubmitter().getUserId(), 0, 0, 0, 0, new BigDecimal(0.0), metadata.getDateSubmitted(), metadata.getDateSubmitted(), 
        		metadata.getDateSubmitted(), 0, 1, 0, metadata.getStatus(), 0);
        newLink.setLinkTitle(metadata.getTitle());
        newLink.setLinkContent(metadata.getDescription());
        newLink.setLinkSummary(metadata.getDescription());
        newLink.setLinkStatus(metadata.getStatus());
        newLink.setLinkTags(metadata.getTags());
        newLink.setLinkTitleUrl(String.valueOf(metadata.getSid()));
        
        handleHistoryEdits(newLink, metadata.getUserId());
        
        LinksDAO linksDAO = new LinksDAOImpl();
        linksDAO.merge(newLink);
	}

	private void updateTags(StoryMetadataViewModel metadata) {
		TagsDAO tagsDAO = new TagsDAOImpl();
		
		tagsDAO.deleteAllBySid(metadata.getSid());
			
		
		if (metadata.getTags().length() > 0) {
			String[] tagsSplit = metadata.getTags().split(",|;");
			
			for(String tag : tagsSplit) {
				ColfusionTags colfusionTag = new ColfusionTags(new ColfusionTagsId(metadata.getSid(), "en", metadata.getDateSubmitted(), tag.trim()));
				tagsDAO.saveOrUpdate(colfusionTag);
			}
		}
		
		TagsCacheDAO tagsCacheDAO = new TagsCacheDAOImpl();
		tagsCacheDAO.deleteAll();
	}

	/**
	 * Transforms referenced field ColfusionSourceinfoUsers of a given story into map where keys are user ids and values are records from StoryUserRoles table which
	 * describes in which role each user is participating in the given story.
	 * 
	 * @param newStory for which the information need to be transformed.
	 * @return the map as described in the description.
	 */
	@Override
	public Map<Integer, ColfusionUserroles> getUsersInRolesForStory(ColfusionSourceinfo newStory) {
		
		//TODO, FIXME: the key in the map should not be user id, it should be composition of userid and roleid.
		// Right now one user can have only one role, but in database we allow one user to have several roles.
		
		Map<Integer, ColfusionUserroles> result = new HashMap<Integer, ColfusionUserroles>();
		if (newStory.getColfusionSourceinfoUsers().iterator().hasNext()) {
			
			for ( Object userRoleObj : newStory.getColfusionSourceinfoUsers().toArray()) {
				
				ColfusionSourceinfoUser userRole = (ColfusionSourceinfoUser) userRoleObj;
				
				if (!result.containsKey(userRole.getId().getUid())) {
					result.put(userRole.getId().getUid(), userRole.getColfusionUserroles());
				}
			}
		}
		
		return result;
	}

	@Override
	public List<ColfusionUsers> findStoryAuthors(ColfusionSourceinfo story) {
		ArrayList<ColfusionUsers> result = new ArrayList<>();
		
		for (Object sourceInfoUserObj : story.getColfusionSourceinfoUsers().toArray()) {
			ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;
			
			result.add(sourceInfoUser.getColfusionUsers());
		}
		
		return result;
	}
}
