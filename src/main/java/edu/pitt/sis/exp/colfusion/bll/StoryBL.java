/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataResponse;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;


/**
 * @author Evgeny
 *
 */
public class StoryBL {
	final Logger logger = LogManager.getLogger(StoryBL.class.getName());
	
	public StoryMetadataResponse getStoryMetadata(int sid) {
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		
		ColfusionSourceinfo storyInfo = storyMgr.findDatasetInfoBySid(sid,  true);
		
		StoryMetadataViewModel storyMetadata = new StoryMetadataViewModel();
		storyMetadata.setSid(sid);
		storyMetadata.setSource_type(storyInfo.getSourceType());
		storyMetadata.setStatus(storyInfo.getStatus());
		storyMetadata.setTitle("");
		storyMetadata.setDescription("");
		
		StoryMetadataResponse result = new StoryMetadataResponse();
		result.isSuccessful = true;
		result.message = "OK";
		result.setPayload(storyMetadata);
		
		return result;
				
	}
}
