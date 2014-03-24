/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.dataLoadExecutors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import junit.framework.TestCase;
import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;
import edu.pitt.sis.exp.colfusion.dataLoadExecutors.DataLoadExecutor;
import edu.pitt.sis.exp.colfusion.dataLoadExecutors.DataLoadExecutorFactory;
import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.importers.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.UserManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.process.ProcessManager;
import edu.pitt.sis.exp.colfusion.tests.PropertyKeysTest;
import edu.pitt.sis.exp.colfusion.tests.importers.ktr.KTRManagerTest;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDB;

/**
 * @author Evgeny
 *
 */
public class DataLoadExecutorFactoryTest  extends TestCase {

	Logger logger = LogManager.getLogger(DataLoadExecutorFactoryTest.class.getName());
	
	public void testGetDataLoadExecutor() {
		
		int sid = 0; //TODO this should be updated when we start to use test database.
		
		try {
			sid = prepareDatabase();
		} catch (Exception e1) {
			logger.error("prepareDatabase failed", e1);
			
			fail("prepareDatabase failed");
		}
		
		Random rand = new Random();

		int alterValue = rand.nextInt(50) + 1;
		
		//alterKTR(sid, alterValue);
		
		try {
			
			SourceInfoManager storyMng = new SourceInfoManagerImpl();
			
			DataSourceTypes sourceType = storyMng.getStorySourceType(sid);
			
			DataLoadExecutor executor = DataLoadExecutorFactory.getDataLoadExecutor(sourceType);
			
			executor.setSid(sid);
			
			ProcessManager.getInstance().queueProcess(executor);
			
			while (!executor.isDone()) {
				logger.info("testGetDataLoadExecutor: going to sleep for 200 ms");
				Thread.sleep(1000);
			}
			
			StoryTargetDB storyTargetDB = storyMng.getStorySourceInfoDB(sid);
			
			ConfigManager configManager = ConfigManager.getInstance();
			
			assertEquals(configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_DatabaseNamePrefix) + sid, // + alterValue
					storyTargetDB.getDatabaseName());
			
			assertEquals(configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_UserName), // + alterValue
					storyTargetDB.getUserName());
			
			assertEquals(configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_Password), // + alterValue
					storyTargetDB.getPassword());
			
			assertEquals(configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_Server), // + alterValue 
					storyTargetDB.getServerAddress());
			
			assertEquals(configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_Port), // + alterValue 
					String.valueOf(storyTargetDB.getPort()));
			
			assertEquals(configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_Type), // + alterValue 
					storyTargetDB.getDriver());
			
			assertEquals(sid, 
					storyTargetDB.getSid());
			
			
		} catch (Exception e) {
			
			
			logger.error("triggerDataLoadExecution failed: Couldn't trigger KTR execution for " + sid);
			
			fail("triggerDataLoadExecution failed: Couldn't trigger KTR execution for " + sid);
		}
	}

	private int prepareDatabase() throws Exception {
		// TODO the same code is used in the KTRManagerTest, should be probably moved to somewhere like TestUtils
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		List<ColfusionSourceinfo> stories = storyMgr.findByTitle(ConfigManager.getInstance().getPropertyByName(PropertyKeysTest.testStoryTitle), 1);
		
		ColfusionSourceinfo story = null;
		
		if (stories.size() == 0 || stories.get(0) == null) {
			
			UserManager userMgr = new UserManagerImpl();
			
			List<ColfusionUsers> users = userMgr.lookUpUser(ConfigManager.getInstance().getPropertyByName(PropertyKeysTest.testUserLogin), 1);
			ColfusionUsers user = null;
			
			if (users.size() == 0 || users.get(0) == null) {
				user = new ColfusionUsers(new Date(), new Date(),
						new Date(), new Date(), new Date(), true);
				
				user.setUserLogin(ConfigManager.getInstance().getPropertyByName(PropertyKeysTest.testUserLogin));
				
				userMgr.save(user);
				
				userMgr.saveOrUpdate(user);
			}
			else {
				user = users.get(0);
			}
			
			story = storyMgr.newStory(user.getUserId(), new Date(), DataSourceTypes.DATA_FILE);
			story.setTitle(ConfigManager.getInstance().getPropertyByName(PropertyKeysTest.testStoryTitle));
			storyMgr.saveOrUpdate(story);
		}
		else {
			story = stories.get(0);
			
			if (DataSourceTypes.fromString(story.getSourceType()) != DataSourceTypes.DATA_FILE) {
				story.setSourceType(DataSourceTypes.DATA_FILE.getValue());
				
				storyMgr.saveOrUpdate(story);
			}
		}
		
		//TODO: this is bad, because unit tests should be FIRST (the important part is independent), but I couldn't figure our how to better do it.
		//we need to have ktr file created and the location of it stored in the DB.
		KTRManagerTest ktrManagerTest = new KTRManagerTest();
		ktrManagerTest.testCreateKTR();
		
		return story.getSid();
	}

	private void alterKTR(int sid, int alterValue) throws Exception {
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(sid);
		
		for(String ktrLocation : ktrLocations) {
			
			
			KTRManager ktrManager = new KTRManager(sid);
			
			try {
				ktrManager.loadKTR(ktrLocation);
							
				ConfigManager configManager = ConfigManager.getInstance();
				
				ktrManager.updateTargetDBConnectionInfo(configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_DatabaseNamePrefix) + sid + alterValue,
						configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_UserName) + alterValue,
						configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_Password) + alterValue,
						configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_Server) + alterValue,
						configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_Port) + alterValue,
						configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_Type) + alterValue);
								
				return;
			} catch (Exception e) {
				
				logger.error(String.format("alterKTR failed: could not modify KTR file for %d", sid), e);
				
				throw e;
			}	
		}

	}
}
