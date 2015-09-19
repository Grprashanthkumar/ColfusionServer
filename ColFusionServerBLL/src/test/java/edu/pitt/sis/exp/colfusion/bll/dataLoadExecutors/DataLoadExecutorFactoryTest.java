/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.dataLoadExecutors;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

/**
 * @author Evgeny
 *
 */
public class DataLoadExecutorFactoryTest  extends TestCase {

	Logger logger = LogManager.getLogger(DataLoadExecutorFactoryTest.class.getName());
	
	public DataLoadExecutorFactoryTest(final String name) {
		super(name);
	}
	
	// TODO: redo DataLoadExecutorFactoryTest.testGetDataLoadExecutor
	
	@Ignore
	@Test
	public void testGetDataLoadExecutor() {
		
//		int sid = 0; //TODO this should be updated when we start to use test database.
//		
//		try {
////			sid = prepareDatabase();
//		} catch (Exception e1) {
//			logger.error("prepareDatabase failed", e1);
//			
//			fail("prepareDatabase failed");
//		}
//		
//		Random rand = new Random();
//
//		int alterValue = rand.nextInt(50) + 1;
//		
//		//alterKTR(sid, alterValue);
//		
//		try {
//			
//			SourceInfoManager storyMng = new SourceInfoManagerImpl();
//			
//			DataSourceTypes sourceType = storyMng.getStorySourceType(sid);
//			
//			DataLoadExecutor executor = DataLoadExecutorFactory.getDataLoadExecutor(sourceType);
//			
//			executor.setSid(sid);
//			
//			int processId = ProcessManager.getInstance().queueProcess(executor);
//			
//			ProcessStatusEnum status = ProcessManager.getInstance().getProcessStatus(processId);
//			
//			while (status != ProcessStatusEnum.DONE && status != ProcessStatusEnum.FAILED) {
//				logger.info("testGetDataLoadExecutor: going to sleep for 2000 ms");
//				Thread.sleep(2000);
//				
//				status = ProcessManager.getInstance().getProcessStatus(processId);
//			}
//			
//			ColfusionSourceinfoDb storyTargetDB = storyMng.getStorySourceInfoDB(sid);
//			
//			ConfigManager configManager = ConfigManager.getInstance();
//			
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_DATABASE_NAME_PREFIX) + sid, // + alterValue
//					storyTargetDB.getSourceDatabase());
//			
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_USERNAME), // + alterValue
//					storyTargetDB.getUserName());
//			
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PASSWORD), // + alterValue
//					storyTargetDB.getPassword());
//			
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_HOST), // + alterValue 
//					storyTargetDB.getServerAddress());
//			
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PORT), // + alterValue 
//					String.valueOf(storyTargetDB.getPort()));
//			
//			assertEquals(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_VENDOR), // + alterValue 
//					storyTargetDB.getDriver());
//			
//			assertEquals(sid, 
//					storyTargetDB.getSid());
//			
//			if (executor.getExceptions().size() > 0) {
//				
//				for (Exception e : executor.getExceptions()) {
//					logger.error(String.format("testGetDataLoadExecutor failed: executor has more than 0 exceptions: %s", e.toString()));
//				}
//				
//				fail("testGetDataLoadExecutor failed. Executor has more than 0 exceptions.");
//			}
//			
//			
//			
//		} catch (Exception e) {
//			
//			
//			logger.error("triggerDataLoadExecution failed: Couldn't trigger KTR execution for " + sid);
//			
//			fail("triggerDataLoadExecution failed: Couldn't trigger KTR execution for " + sid);
//		}
	}

	//TODO:redo
//	private int prepareDatabase() throws Exception {
//		int sid = Utils.getTestSid();
//		
//		//TODO: again depend on other test, BAD.
//		DataSubmissionWizzardTest dataSubmissionTest = new DataSubmissionWizzardTest("");
//		dataSubmissionTest.testStoreUploadedFiles();
//		
//		//TODO: this is bad, because unit tests should be FIRST (the important part is independent), but I couldn't figure our how to better do it.
//		//we need to have ktr file created and the location of it stored in the DB.
//		KTRManagerTest ktrManagerTest = new KTRManagerTest("");
//		ktrManagerTest.testCreateKTR();
//		
//		return sid;
//	}

	private void alterKTR(final int sid, final int alterValue) throws Exception {
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(sid);
		
		for(String ktrLocation : ktrLocations) {
			
			
			KTRManager ktrManager = new KTRManager(sid);
			
			try {
				ktrManager.loadKTR(ktrLocation);
							
				ConfigManager configManager = ConfigManager.getInstance();
				
				ktrManager.updateTargetDBConnectionInfo(configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_DATABASE_NAME_PREFIX) + sid + alterValue,
						configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_USERNAME) + alterValue,
						configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PASSWORD) + alterValue,
						configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_HOST) + alterValue,
						configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_PORT) + alterValue,
						configManager.getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_VENDOR) + alterValue);
								
				return;
			} catch (Exception e) {
				
				logger.error(String.format("alterKTR failed: could not modify KTR file for %d", sid), e);
				
				throw e;
			}	
		}

	}
}
