/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.importers.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.LinksManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.LinksManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl.HistoryItem;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionLinks;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUserroles;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.responseModels.ColumnMetadataResponse;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataHistoryResponse;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataResponse;
import edu.pitt.sis.exp.colfusion.utils.MappingUtils;
import edu.pitt.sis.exp.colfusion.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryAuthorViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataHistoryViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;


/**
 * @author Evgeny
 *
 */
public class StoryBL {
	
	final Logger logger = LogManager.getLogger(StoryBL.class.getName());
	
	/**
	 * Creates empty story.
	 * @return story metadata almost empty but with auto generated sid.
	 */
	public StoryMetadataResponse createStory(final int userId) {
		StoryMetadataResponse result = new StoryMetadataResponse();
		
		try {
			logger.info(String.format("Creating new story by user with id %d", userId));
			
			SourceInfoManager storyMgr = new SourceInfoManagerImpl();
			
			ColfusionSourceinfo newStory = storyMgr.newStory(userId, new Date(), DataSourceTypes.DATA_FILE);
			
			StoryMetadataViewModel storyMetadata = new StoryMetadataViewModel();
			storyMetadata.setSid(newStory.getSid());
			storyMetadata.setTitle("");
			storyMetadata.setTags("");
			storyMetadata.setDescription("");
			storyMetadata.setSourceType(DataSourceTypes.DATA_FILE.getValue());
			storyMetadata.setDateSubmitted(newStory.getEntryDate());
			storyMetadata.setStatus(newStory.getStatus());
			
			Map<Integer, ColfusionUserroles> userRoles = storyMgr.getUsersInRolesForStory(newStory);
			storyMetadata.setStorySubmitter(this.makeStorySubmitterViewModel(newStory, newStory.getColfusionUsers(), userRoles));
			
			ArrayList<StoryAuthorViewModel> authors = new ArrayList<StoryAuthorViewModel>();
			ArrayList<ColfusionUsers> users = (ArrayList<ColfusionUsers>) storyMgr.findStoryAuthors(newStory);
			for (ColfusionUsers author : users) {
				authors.add(this.makeStorySubmitterViewModel(newStory, author, userRoles));
			}
			
			storyMetadata.setStoryAuthors(authors);
			
			result.isSuccessful = true;
			result.message = "OK";
			result.setPayload(storyMetadata);
			
			logger.info(String.format("Successfully created new story by user with id %d", userId));
		} catch (Exception e) {
			this.logger.error("createStory failed", e);
			result.isSuccessful = false;
			result.message = "Could not create new story. Try again later please";
		}
		
		return result;
	}
	
	/**
	 * Transforms {@link ColfusionUsers} user of a given {@link ColfusionSourceinfo} story into {@link StoryAuthorViewModel} model. 
	 * @param story {@link ColfusionSourceinfo} for which to get authors information.
	 * @param user {@link ColfusionUsers}  is the author for who we get information.
	 * @param userRoles 
	 * @return {@link StoryAuthorViewModel} model which has user/author information for given story.
	 */
	private StoryAuthorViewModel makeStorySubmitterViewModel(final ColfusionSourceinfo story, final ColfusionUsers user, final Map<Integer, ColfusionUserroles> userRoles) {
		
		StoryAuthorViewModel result = MappingUtils.getInstance().mapColfusionUserToStoryAuthorViewModel(user);
		
		if (userRoles.containsKey(user.getUserId())) {
			ColfusionUserroles userRole = userRoles.get(user.getUserId());
			result.setRoleId(userRole.getRoleId());		
		}	
		
		return result;
	}

	/**
	 * Get metadata of the a story by story sid. If sid is not found in the sourceinfo table, then it will return falst in isSuccessful. 
	 * If link is not found, then it will just return empty title and description.
	 * 
	 * @param sid of the story of which metadata will be returned.
	 * @return the 
	 */
	public StoryMetadataResponse getStoryMetadata(final int sid) {
		StoryMetadataResponse result = new StoryMetadataResponse();
		
		//Get Story info
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ColfusionSourceinfo storyInfo = storyMgr.findBySid(sid,  true);
		
		if (storyInfo == null) { // if store was not found, then return not success, the user should probably refresh the page.
			result.isSuccessful = false;
			result.message = "No story found with sid:  " + sid;
			result.setPayload(null);
		}
		else { // if there is a record in the sourceinfo table, then get any available information from links table and populate viewmodel.
			
			StoryMetadataViewModel storyMetadata = new StoryMetadataViewModel();
			storyMetadata.setSid(sid);
			storyMetadata.setSourceType(storyInfo.getSourceType());
			
			
			//Getting user's role in the story and put in the view model with role details: role name, description and role id.
			Map<Integer, ColfusionUserroles> userRoles = storyMgr.getUsersInRolesForStory(storyInfo);
			
			storyMetadata.setStorySubmitter(this.makeStorySubmitterViewModel(storyInfo, storyInfo.getColfusionUsers(), userRoles));
			
			ArrayList<StoryAuthorViewModel> authors = new ArrayList<StoryAuthorViewModel>();
			ArrayList<ColfusionUsers> users = (ArrayList<ColfusionUsers>) storyMgr.findStoryAuthors(storyInfo);
			for (ColfusionUsers author : users) {
				authors.add(this.makeStorySubmitterViewModel(storyInfo, author, userRoles));
			}
			
			storyMetadata.setStoryAuthors(authors);
						
			LinksManager linksMgr = new LinksManagerImpl();
			ColfusionLinks link = null;
			try {
				link = linksMgr.findByID(sid);
			} catch (Exception e) {
				result.isSuccessful = false;
				result.message = "No story found with sid:  " + sid;
				result.setPayload(null);
				
				this.logger.error(String.format("Failed to findById for sid = %d", sid), e);
				
				return result;
			} 
			
			if (link != null) {
				storyMetadata.setTitle(link.getLinkTitle());
				storyMetadata.setDescription(link.getLinkContent());
				storyMetadata.setTags(link.getLinkTags());
				storyMetadata.setDateSubmitted(link.getLinkDate());
				storyMetadata.setStatus(storyInfo.getStatus());
			}
			else {
				storyMetadata.setTitle("");
				storyMetadata.setDescription("");
				storyMetadata.setTags("");
			}
			
			result.isSuccessful = true;
			result.message = "OK";
			result.setPayload(storyMetadata);
		}
		
		return result;
	}

	/**
	 * Update story metadata with provided information.
	 * 
	 * @param metadata information to be updated.
	 * @return response with information if the update was successful or not.
	 */
	public StoryMetadataResponse updateStoryMetadata(final StoryMetadataViewModel metadata) {
		StoryMetadataResponse result = new StoryMetadataResponse();
		
		try {
			SourceInfoManager storyMgr = new SourceInfoManagerImpl();
			//TODO, FIXME: for now I will just pass the view model, however I think view model should not got that level, the storage level could be implemented
			// in another project and then this way would lead to cycle in project dependencies.
			storyMgr.updateStory(metadata);
			
			result.isSuccessful = true;
			result.message = "OK";
		} catch (Exception e) {
			this.logger.error("updateStoryMetadata failed", e);
			result.isSuccessful = false;
			result.message = "Could not update story. Please try again later.";
		}
		
		return result;
	}

	public StoryMetadataHistoryResponse getStoryMetadataHistory(final int sid, final String historyItem) {
		StoryMetadataHistoryResponse result = new StoryMetadataHistoryResponse();
		
		try {
			SourceInfoManager storyMgr = new SourceInfoManagerImpl();
			//TODO, FIXME: for now I will just pass the view model, however I think view model should not got that level, the storage level could be implemented
			// in another project and then this way would lead to cycle in project dependencies.
			
			if (!HistoryItem.isMember(historyItem)) {
				result.isSuccessful = false;
				result.message = "The history item is not valid";	
				this.logger.info(String.format("getStoryMetadataHistory provided history item %s is not valid for story %d", historyItem, sid));
			}
			else {
			
				StoryMetadataHistoryViewModel storyMedataEdit = storyMgr.getStoryMetadataHistory(sid, historyItem);
				
				result.setPayload(storyMedataEdit);
				result.isSuccessful = true;
				result.message = "OK";
			}
		} catch (Exception e) {
			this.logger.error(String.format("getStoryMetadataHistory failed for %d sid and %s history item", sid, historyItem), e);
			result.isSuccessful = false;
			result.message = "Could not fetch history for the story. Please try again later.";
		}
		
		return result;
	}
	public ColumnMetadataResponse getColumnMetaData(final int sid, final String tableName) {
		ColumnMetadataResponse result = new ColumnMetadataResponse();
		try{
			
			DNameInfoManager columnManager = new DNameInfoManagerImpl();
			List<ColfusionDnameinfo> columnsMetadataFromDB = columnManager.getColumnsMetadata(sid, tableName);
			
			for (ColfusionDnameinfo colfusionDnameinfo : columnsMetadataFromDB) {
				result.getPayload().add(new DatasetVariableViewModel(colfusionDnameinfo.getCid(), 
						colfusionDnameinfo.getDnameOriginalName(), 
						colfusionDnameinfo.getDnameChosen(),
						colfusionDnameinfo.getDnameValueDescription(), 
						colfusionDnameinfo.getDnameValueUnit(), 
						colfusionDnameinfo.getDnameValueType(), 
						colfusionDnameinfo.getDnameValueFormat(), 
						colfusionDnameinfo.getMissingValue(), false));
			}
			
			result.isSuccessful = true;
			result.message = "OK";	
		}catch (Exception e){
			result.isSuccessful = false;
			result.message = "Could not fetch column metadata for the table. Please try again later.";
		}
		return result;
	}
}