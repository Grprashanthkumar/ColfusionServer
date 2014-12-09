/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests;

import java.util.Date;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManager;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeysTest;

/**
 * @author Evgeny
 *
 */
public class Utils {
	
	
	/**
	 * Find the sid of the story which is used for testing. If the story doesn't exist, then it will be created.
	 * 
	 * @return the id of the story.
	 * @throws Exception
	 */
	public static int getTestSid() throws Exception {
		// TODO the same code is used in the KTRManagerTest, should be probably moved to somewhere like TestUtils
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		List<ColfusionSourceinfo> stories = storyMgr.findByTitle(ConfigManager.getInstance().getProperty(PropertyKeysTest.testStoryTitle), 1);
		
		ColfusionSourceinfo story = null;
		
		if (stories.size() == 0 || stories.get(0) == null) {
			
			UserManager userMgr = new UserManagerImpl();
			
			List<ColfusionUsers> users = userMgr.lookUpUser(ConfigManager.getInstance().getProperty(PropertyKeysTest.testUserLogin), 1);
			ColfusionUsers user = null;
			
			if (users.size() == 0 || users.get(0) == null) {
				user = new ColfusionUsers(new Date(), new Date(),
						new Date(), new Date(), new Date(), true);
				
				user.setUserLogin(ConfigManager.getInstance().getProperty(PropertyKeysTest.testUserLogin));
				
				userMgr.save(user);
				
				userMgr.saveOrUpdate(user);
			}
			else {
				user = users.get(0);
			}
			
			story = storyMgr.newStory(user.getUserId(), new Date(), DataSourceTypes.DATA_FILE);
			story.setTitle(ConfigManager.getInstance().getProperty(PropertyKeysTest.testStoryTitle));
			storyMgr.saveOrUpdate(story);
		}
		else {
			story = stories.get(0);
			
			if (DataSourceTypes.fromString(story.getSourceType()) != DataSourceTypes.DATA_FILE) {
				story.setSourceType(DataSourceTypes.DATA_FILE.getValue());
				
				storyMgr.saveOrUpdate(story);
			}
		}
		
		return story.getSid();
	}
}
