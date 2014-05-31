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
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.importers.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.importers.utils.StoryStatusTypes;
import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.LinksDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.LinksDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDBDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDBDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoMetadataEditHistoryDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoMetadataEditHistoryDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoTableKTRDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoTableKTRDAOImpl;
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
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoMetadataEditHistory;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoUser;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoUserId;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionTags;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionTagsId;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUserroles;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.utils.MappingUtils;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryAuthorViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataHistoryLogRecordViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataHistoryViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDBViewModel;


/**
 * @author Evgeny
 *
 */
public class SourceInfoManagerImpl extends GeneralManagerImpl<ColfusionSourceinfo, Integer> implements SourceInfoManager {

	Logger logger = LogManager.getLogger(SourceInfoManagerImpl.class.getName());
	
	public SourceInfoManagerImpl() {
		super(new SourceInfoDAOImpl(), ColfusionSourceinfo.class);
	}
	
	public enum HistoryItem {
	    TITLE("title"), DESCRIPTION("description"), TAGS("tags"), STATUS("status");
	    
	    private String value;

	    private HistoryItem(final String value) {
	            this.value = value;
	    }
	    
	    public String getValue(){
	    	return this.value;
	    }
	    
	    static public boolean isMember(final String enumValueToTest) {
	    	HistoryItem[] enumValues = HistoryItem.values();
	        for (HistoryItem enumValue : enumValues) {
				if (enumValue.value.equals(enumValueToTest)) {
					return true;
				}
			}
	        return false;
	    }
	};
	
	//***************************************
	// General manager interface
	//***************************************
	
	@Override
	public ColfusionSourceinfo findByID(final Integer id) {
		ColfusionSourceinfo result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = _dao.findByID(ColfusionSourceinfo.class, id);
            
            if (result != null) { 
            
            	Hibernate.initialize(result.getColfusionUsers());
            }
            
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
	
	//************************************
	// SOURCINFOMANAGER interface
	//***********************************

	@Override
	public List<ColfusionSourceinfo> findByUserId(final int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColfusionSourceinfo> findBySidOrTitle(
			final String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColfusionSourceinfo findBySid(final int sid, final boolean includeDraft) {
		ColfusionSourceinfo sourceinfo = null;
        try {
            HibernateUtil.beginTransaction();
            
            sourceinfo = ((SourceInfoDAO)_dao).findDatasetInfoBySid(sid, includeDraft);
            
            if (sourceinfo != null) {
            
	            Hibernate.initialize(sourceinfo.getColfusionUsers());
	            Hibernate.initialize(sourceinfo.getColfusionSourceinfoUsers());
	            
	            for (Object sourceInfoUserObj : sourceinfo.getColfusionSourceinfoUsers().toArray()) {
	    			ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;
	    			
	    			if (sourceInfoUser != null) {
	    				Hibernate.initialize(sourceInfoUser.getColfusionUsers());
	    			}
	    		}
            }
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }
        return sourceinfo;
	}

	@Override
	public List<ColfusionSourceinfo> findByTitle(final String searchTerm, final int limit) {
		try {
            HibernateUtil.beginTransaction();
            
            List<ColfusionSourceinfo> result = ((SourceInfoDAO) _dao).lookupStories(searchTerm, limit);
            
            for (ColfusionSourceinfo sourceinfo : result) {
            	
            	Hibernate.initialize(sourceinfo.getColfusionUsers());
	            Hibernate.initialize(sourceinfo.getColfusionSourceinfoUsers());
	            
	            for (Object sourceInfoUserObj : sourceinfo.getColfusionSourceinfoUsers().toArray()) {
	    			ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;
	    			
	    			if (sourceInfoUser != null) {
	    				Hibernate.initialize(sourceInfoUser.getColfusionUsers());
	    			}
	    		}
			}
            
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

	/**
	 * Creates new story with given input parameters and stores it in the database and the resulting entity has automatically generated sid.
	 * 
	 * @param userId is the id of the user who is creating the new story.
	 * @param date when the new story is created.
	 * @param source_type type of the source from which the data will be imported.
	 * @return newly created story which is stored in the db. Has auto generated sid.
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ColfusionSourceinfo newStory(final int userId, final Date date, final DataSourceTypes source_type) throws Exception {
		try {
            HibernateUtil.beginTransaction();
            
            UsersDAO usersDAO = new UsersDAOImpl();
            
            ColfusionUsers userCreator = usersDAO.findByID(ColfusionUsers.class, userId);
            
            if (userCreator == null) {
            	logger.error(String.format("newStory failed: could not find user by %d id", userId));
            	
            	throw new Exception(String.format("newStory failed: could not find user by %d id", userId));
            }
            
            ColfusionSourceinfo newStoryEntity = new ColfusionSourceinfo(userCreator, date, source_type.getValue());
            
            newStoryEntity.setStatus(StoryStatusTypes.DRAFT.getValue());
            
            int sid = _dao.save(newStoryEntity);
            
            //Add user as submitter/contributer for newly created story.
            
            UserRolesDAO userRolesDAO = new UserRolesDAOImpl();
            // 1 is for contributor/submitter, TODO: maybe we should use enum here not hard wired value.
            ColfusionUserroles userRole = userRolesDAO.findByID(ColfusionUserroles.class, 1);
            
            if (userRole == null) {
            	logger.error(String.format("newStory failed: could not find userRole by %d id", 1));
            	
            	throw new Exception(String.format("newStory failed: could not find user by %d id", 1));
            }
            
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

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }
	}

	/**
	 * Updates both sourceinfo and links table with story metadata.
	 * 
	 * @param metadata the metadata to be used to update the tables in database.
	 * @throws Exception 
	 */
	@Override
	public void updateStory(final StoryMetadataViewModel metadata) throws Exception {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionSourceinfo addedUpdatedStory = updateSourceInfo(metadata);
            updateUserRoles(addedUpdatedStory, metadata);
            updateLink(metadata);
            updateTags(metadata);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        } catch (Exception e) {
        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed most probably because of updateSourceInfo failed", e);
        	throw e;
		}
	}

	private ColfusionSourceinfo updateSourceInfo(final StoryMetadataViewModel metadata) throws Exception {
		UsersDAO usersDAO = new UsersDAOImpl();
        
        ColfusionUsers userCreator = usersDAO.findByID(ColfusionUsers.class, metadata.getStorySubmitter().getUserId());
        
        ColfusionSourceinfo newStoryEntity = new ColfusionSourceinfo(userCreator, metadata.getDateSubmitted(), metadata.getSourceType());
        newStoryEntity.setSid(metadata.getSid());
        newStoryEntity.setTitle(metadata.getTitle());
        newStoryEntity.setStatus(metadata.getStatus());
        
        try {
			handleHistoryEdits(newStoryEntity, metadata.getUserId(), metadata.getEditReason());
		} catch (Exception e) {
			logger.error(String.format("updateSourceInfo failed on handleHistoryEdits"));
			
			throw e;
		}
        _dao.merge(newStoryEntity);
        
        return newStoryEntity;
	}
	
	//TODO: should we move the next two methods to other class? together with enum?
	/**
	 * Checks if new instance of a record of sourceinfo has title and status difference from the one stored in the database already.
	 * If they do differ, then old value is copied into history of edits table before replaced by new value.
	 * 
	 * @param newStory new instance of the sourceinfo record possibly updated by user during edit operation.
	 * @param userId id of the user who did edit (note, it is not the submitter, it is id of the user who was logged in and performed the edit).
	 * @throws Exception 
	 */
	private void handleHistoryEdits(final ColfusionSourceinfo newStory, final int userId, final String reason) throws Exception {
		SourceInfoMetadataEditHistoryDAO editHistorDAO = new SourceInfoMetadataEditHistoryDAOImpl();
		
		ColfusionSourceinfo oldStory = _dao.findByID(ColfusionSourceinfo.class, newStory.getSid());
		
		try {
			String oldValue = (oldStory == null) ? null : oldStory.getTitle();
			editHistorDAO.saveHistoryIfChanged(newStory.getSid(), userId, oldValue, newStory.getTitle(), HistoryItem.TITLE,  reason);
		
			oldValue = (oldStory == null) ? null : oldStory.getStatus();
			editHistorDAO.saveHistoryIfChanged(newStory.getSid(), userId, oldValue, newStory.getStatus(), HistoryItem.STATUS,  reason);
		
		} catch (Exception e) {
			logger.error(String.format("handleHistoryEdits for sourceinfo failed due to one of the two calls of editHistorDAO.saveHistoryIfChanged. userId = %d and reason is %s", userId, reason));
			
			throw e;
		}
	}
	
	/**
	 * Checks if new instance of a record of links has content/description and tags difference from the one stored in the database already.
	 * If they do differ, then old value is copied into history of edits table before replaced by new value.
	 * 
	 * @param newLink new instance of the links record possibly updated by user during edit operation.
	 * @param userId id of the user who did edit (note, it is not the submitter, it is id of the user who was logged in and performed the edit).
	 * @throws Exception 
	 */
	private void handleHistoryEdits(final ColfusionLinks newLink, final int userId, final String reason) throws Exception {
		SourceInfoMetadataEditHistoryDAO editHistorDAO = new SourceInfoMetadataEditHistoryDAOImpl();
		
		LinksDAO linksDAO = new LinksDAOImpl();
		ColfusionLinks oldLink = linksDAO.findByID(ColfusionLinks.class, newLink.getLinkId());
		
		try {
			String oldValue = (oldLink == null) ? null : oldLink.getLinkContent();
			editHistorDAO.saveHistoryIfChanged(newLink.getLinkId(), userId, oldValue, newLink.getLinkContent(), HistoryItem.DESCRIPTION, reason);
		
			oldValue = (oldLink == null) ? null : oldLink.getLinkTags();
			editHistorDAO.saveHistoryIfChanged(newLink.getLinkId(), userId, oldValue, newLink.getLinkTags(), HistoryItem.TAGS, reason);
		} catch (Exception e) {
			logger.error(String.format("handleHistoryEdits for links failed due to one of the two calls of editHistorDAO.saveHistoryIfChanged. userId = %d and reason is %s", userId, reason));
			
			throw e;
		}
	}
	
	
	/**
	 * Adding or updating information about roles of users for a given dataset/story.
	 * @param addedUpdatedStory {@link ColfusionSourceinfo} is the story for which information about user and their roles needs to be added/updated.
	 * @param metadata {@link StoryMetadataViewModel} holds the information which need to be inserted/updated in the db.
	 */
	private void updateUserRoles(final ColfusionSourceinfo addedUpdatedStory, final StoryMetadataViewModel metadata) {
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

	private void updateLink(final StoryMetadataViewModel metadata) throws Exception {
		ColfusionLinks newLink = new ColfusionLinks(metadata.getSid(), metadata.getStorySubmitter().getUserId(), 0, 0, 0, 0, new BigDecimal(0.0), metadata.getDateSubmitted(), metadata.getDateSubmitted(), 
        		metadata.getDateSubmitted(), 0, 1, 0, metadata.getStatus(), 0);
        newLink.setLinkTitle(metadata.getTitle());
        newLink.setLinkContent(metadata.getDescription());
        newLink.setLinkSummary(metadata.getDescription());
        newLink.setLinkStatus(metadata.getStatus());
        newLink.setLinkTags(metadata.getTags());
        newLink.setLinkTitleUrl(String.valueOf(metadata.getSid()));
        
        try {
			handleHistoryEdits(newLink, metadata.getUserId(), metadata.getEditReason());
		} catch (Exception e) {
			logger.error(String.format("updateLink failed on handleHistoryEdits"));
			
			throw e;
		}
        
        LinksDAO linksDAO = new LinksDAOImpl();
        linksDAO.merge(newLink);
	}

	private void updateTags(final StoryMetadataViewModel metadata) throws Exception {
		TagsDAO tagsDAO = new TagsDAOImpl();
		
		try {
			tagsDAO.deleteAllBySid(metadata.getSid());
		} catch (Exception e) {
			logger.error(String.format("updateTags failed on tagsDAO.deleteAllBySid(metadata.getSid()); for sid = %d", metadata.getSid()));
			
			throw e;
		}
			
		
		if (metadata.getTags().length() > 0) {
			String[] tagsSplit = metadata.getTags().split(",|;");
			
			for(String tag : tagsSplit) {
				ColfusionTags colfusionTag = new ColfusionTags(new ColfusionTagsId(metadata.getSid(), "en", metadata.getDateSubmitted(), tag.trim()));
				tagsDAO.saveOrUpdate(colfusionTag);
			}
		}
		
		TagsCacheDAO tagsCacheDAO = new TagsCacheDAOImpl();
		
		try {
			tagsCacheDAO.deleteAll();
		} catch (Exception e) {
			logger.error(String.format("updateTags failed on tagsCacheDAO.deleteAll();for sid = %d", metadata.getSid()));
			
			throw e;
		}
	}

	/**
	 * Transforms referenced field ColfusionSourceinfoUsers of a given story into map where keys are user ids and values are records from StoryUserRoles table which
	 * describes in which role each user is participating in the given story.
	 * 
	 * @param newStory for which the information need to be transformed.
	 * @return the map as described in the description.
	 */
	@Override
	public Map<Integer, ColfusionUserroles> getUsersInRolesForStory(final ColfusionSourceinfo newStory) {
		
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
	public List<ColfusionUsers> findStoryAuthors(final ColfusionSourceinfo story) {
		ArrayList<ColfusionUsers> result = new ArrayList<>();
		
		for (Object sourceInfoUserObj : story.getColfusionSourceinfoUsers().toArray()) {
			ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;
			
			result.add(sourceInfoUser.getColfusionUsers());
		}
		
		return result;
	}

	@Override
	public StoryMetadataHistoryViewModel getStoryMetadataHistory(final int sid, final String historyItem) {
		try {
            HibernateUtil.beginTransaction();
            
            SourceInfoMetadataEditHistoryDAO metadataHistoryDAO = new SourceInfoMetadataEditHistoryDAOImpl();

            //TODO: this should be moved to hitory manager
            String sql = "select h from ColfusionSourceinfoMetadataEditHistory h where h.colfusionSourceinfo = :sid and h.item = :item ORDER BY h.hid DESC";
            ColfusionSourceinfo sourceInfo = _dao.findByID(ColfusionSourceinfo.class, sid);
            Query query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sourceInfo).setParameter("item", historyItem);
            ArrayList<ColfusionSourceinfoMetadataEditHistory> historyLog = (ArrayList<ColfusionSourceinfoMetadataEditHistory>) metadataHistoryDAO.findMany(query);
            
            StoryMetadataHistoryViewModel result = new StoryMetadataHistoryViewModel();
            result.setHistoryItem(historyItem);
            result.setSid(sid);
            
            for (ColfusionSourceinfoMetadataEditHistory historyRecord : historyLog) {
            	
            	if (historyRecord != null) {
            	
	            	Hibernate.initialize(historyRecord.getColfusionUsers());
	            	StoryAuthorViewModel author = MappingUtils.getInstance().mapColfusionUserToStoryAuthorViewModel(historyRecord.getColfusionUsers());
	            	
	            	StoryMetadataHistoryLogRecordViewModel historyRecordViewModel = new StoryMetadataHistoryLogRecordViewModel();
	            	historyRecordViewModel.setHid(historyRecord.getHid());
	            	historyRecordViewModel.setItem(historyRecord.getItem());
	            	historyRecordViewModel.setItemValue(historyRecord.getItemValue());
	            	historyRecordViewModel.setReason(historyRecord.getReason());
	            	historyRecordViewModel.setWhenSaved(historyRecord.getWhenSaved());
	            	historyRecordViewModel.setAuthor(author);
	            	
	            	result.getHistoryLogRecords().add(historyRecordViewModel);
            	}
            }
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public DataSourceTypes getStorySourceType(final int sid) throws Exception {
		ColfusionSourceinfo story = this.findByID(sid);
		
		if (story != null) {
			return DataSourceTypes.fromString(story.getSourceType());
		}
		else {
			logger.error(String.format("getStorySourceType failed: Story with %d sid not found", sid));
			
			//TODO: create custom exception StoryNotFound
			throw new Exception(String.format("Story with %d sid not found", sid));
		}
	}

	
	
	@Override
	public void saveOrUpdateSourceInfoDB(final StoryTargetDBViewModel sourceDBInfo) throws Exception {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionSourceinfo story = _dao.findByID(ColfusionSourceinfo.class, sourceDBInfo.getSid());
            
            if (story == null) {
            	logger.error(String.format("saveOrUpdateSourceInfoDB failed: could not find story with %d sid", sourceDBInfo.getSid()));
            	
            	throw new Exception(String.format("saveOrUpdateSourceInfoDB failed: could not find story with %d sid", sourceDBInfo.getSid()));
            }
            
            ColfusionSourceinfoDb storyDB = null;
            
            if (story.getColfusionSourceinfoDb() == null) {
            	storyDB = new ColfusionSourceinfoDb(story, 
            			sourceDBInfo.getServerAddress(), sourceDBInfo.getPort(), sourceDBInfo.getUserName(), sourceDBInfo.getPassword(),
            			sourceDBInfo.getDatabaseName(), sourceDBInfo.getDriver(), sourceDBInfo.getIsLocal(), sourceDBInfo.getLinkedServerName());
            }
            else {
            	storyDB = story.getColfusionSourceinfoDb();
            	storyDB.setServerAddress(sourceDBInfo.getServerAddress());
            	storyDB.setPort(sourceDBInfo.getPort());
            	storyDB.setUserName(sourceDBInfo.getUserName());
            	storyDB.setPassword(sourceDBInfo.getPassword());
            	storyDB.setSourceDatabase(sourceDBInfo.getDatabaseName());
            	storyDB.setDriver(sourceDBInfo.getDriver());
            	storyDB.setIsLocal(sourceDBInfo.getIsLocal());
            	storyDB.setLinkedServerName(sourceDBInfo.getLinkedServerName());
            }
            
            story.setSid(sourceDBInfo.getSid());
            
            SourceInfoDBDAO sourceInfoDBDAO = new SourceInfoDBDAOImpl();
            
            sourceInfoDBDAO.saveOrUpdate(storyDB);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }
	}

	//TODO: add tests
	@Override
	public ArrayList<String> getStoryKTRLocations(final int sid) throws Exception {
		try {
            HibernateUtil.beginTransaction();
            
            SourceInfoTableKTRDAO sourceTableKTRDAO = new SourceInfoTableKTRDAOImpl();
            
            ArrayList<ColfusionSourceinfoTableKtr> sourceTableKTRs = sourceTableKTRDAO.getKTRLocationsBySid(sid);
            
            ArrayList<String> result = new ArrayList<>();
            
            for(ColfusionSourceinfoTableKtr sourceTableKTR : sourceTableKTRs) {
            	result.add(sourceTableKTR.getPathToKtrfile());
            }
            
            HibernateUtil.commitTransaction();
            
            return result;
		
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        } catch (Exception e) {
			logger.error(String.format("getStoryKTRLocations faild on most probably sourceTableKTRDAO.getKTRLocationsBySid(sid); for sid = %d", sid));
			
			throw e;
		}		
	}

	@Override
	public StoryTargetDBViewModel getStorySourceInfoDB(final int sid) {
		try {
            HibernateUtil.beginTransaction();
            
            SourceInfoDBDAO sourceInfoDBDAO = new SourceInfoDBDAOImpl();
            
            ColfusionSourceinfoDb sourceinfoDB = sourceInfoDBDAO.findByID(ColfusionSourceinfoDb.class, sid);
            
            StoryTargetDBViewModel result = new StoryTargetDBViewModel();
            result.setDatabaseName(sourceinfoDB.getSourceDatabase());
            result.setDriver(sourceinfoDB.getDriver());
            result.setIsLocal(sourceinfoDB.getIsLocal());
            result.setLinkedServerName(sourceinfoDB.getLinkedServerName());
            result.setPassword(sourceinfoDB.getPassword());
            result.setPort(sourceinfoDB.getPort());
            result.setServerAddress(sourceinfoDB.getServerAddress());
            result.setUserName(sourceinfoDB.getUserName());
            result.setSid(sourceinfoDB.getSid());
            
            HibernateUtil.commitTransaction();
            
            return result;
		
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }	
	}

	@Override
	public ColfusionSourceinfo findStoryByCid(final Integer cid) {
		try {
            HibernateUtil.beginTransaction();
            
            Query query = HibernateUtil.getSession().createQuery("SELECT di.colfusionSourceinfo FROM ColfusionDnameinfo di join di.colfusionSourceinfo where di.cid =:cid");
            query.setParameter("cid", cid);
            
            ColfusionSourceinfo sourceinfo = this._dao.findOne(query);
                        
            HibernateUtil.commitTransaction();
            
            return sourceinfo;
		
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }	
	}
}