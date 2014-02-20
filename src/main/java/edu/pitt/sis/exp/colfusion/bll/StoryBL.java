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
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		
		ColfusionSourceinfo storyInfo = storyMgr.findBySid(sid,  true);
		StoryMetadataResponse result = new StoryMetadataResponse();
		
		if (storyInfo == null) {
			result.isSuccessful = false;
			result.message = "No story found with sid:  " + sid;
			result.setPayload(null);
		}
		else {
			LinksManager linksMgr = new LinksManagerImpl();
			ColfusionLinks link = linksMgr.findByID(sid);
			
			StoryMetadataViewModel storyMetadata = new StoryMetadataViewModel();
			storyMetadata.setSid(sid);
			storyMetadata.setSource_type(storyInfo.getSourceType());
			storyMetadata.setStatus(storyInfo.getStatus());
			
			if (link != null) {
				storyMetadata.setTitle(link.getLinkTitle());
				storyMetadata.setDescription(link.getLinkContent());
			}
			else {
				storyMetadata.setTitle("");
				storyMetadata.setDescription("");
			}
			
			result.isSuccessful = true;
			result.message = "OK";
			result.setPayload(storyMetadata);
		}
		
		return result;
	}
}
