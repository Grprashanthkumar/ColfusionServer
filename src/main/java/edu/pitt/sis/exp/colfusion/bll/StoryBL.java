/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.managers.LinksManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.LinksManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionLinks;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataResponse;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;


/**
 * @author Evgeny
 *
 */
public class StoryBL {
	
	final Logger logger = LogManager.getLogger(StoryBL.class.getName());
	
	/**
	 * Get metadata of the a story by story sid. If sid is not found in the sourceinfo table, then it will return falst in isSuccessful. 
	 * If link is not found, then it will just return empty title and description.
	 * 
	 * @param sid of the story of which metadata will be returned.
	 * @return the 
	 */
	public StoryMetadataResponse getStoryMetadata(int sid) {
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
			storyMetadata.setSource_type(storyInfo.getSourceType());
			storyMetadata.setStatus(storyInfo.getStatus());
			
			LinksManager linksMgr = new LinksManagerImpl();
			ColfusionLinks link = linksMgr.findByID(sid);
			
			if (link != null) {
				storyMetadata.setTitle(link.getLinkTitle());
				storyMetadata.setDescription(link.getLinkContent());
				storyMetadata.setTags(link.getLinkTags());
				storyMetadata.setDateSubmitted(link.getLinkDate());
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
	public StoryMetadataResponse updateStoryMetadata(StoryMetadataViewModel metadata) {
		// TODO Auto-generated method stub
		return null;
	}
}
