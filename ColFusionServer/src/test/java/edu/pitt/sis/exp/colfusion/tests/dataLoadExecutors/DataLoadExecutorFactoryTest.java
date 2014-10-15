/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.dataLoadExecutors;

import java.util.ArrayList;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.dataLoadExecutors.DataLoadExecutor;
import edu.pitt.sis.exp.colfusion.dataLoadExecutors.DataLoadExecutorFactory;
import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.process.ProcessManager;
import edu.pitt.sis.exp.colfusion.process.ProcessStatusEnum;
import edu.pitt.sis.exp.colfusion.tests.Utils;
import edu.pitt.sis.exp.colfusion.tests.bll.dataSubmissionWizard.DataSubmissionWizzardTest;
import edu.pitt.sis.exp.colfusion.tests.importers.ktr.KTRManagerTest;
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
			
			int processId = ProcessManager.getInstance().queueProcess(executor);
			
			ProcessStatusEnum status = ProcessManager.getInstance().getProcessStatus(processId);
			
			while (status != ProcessStatusEnum.DONE && status != ProcessStatusEnum.FAILED) {
				logger.info("testGetDataLoadExecutor: going to sleep for 2000 ms");
				Thread.sleep(2000);
				
				status = ProcessManager.getInstance().getProcessStatus(processId);
			}
			
			ColfusionSourceinfoDb storyTargetDB = storyMng.getStorySourceInfoDB(sid);
			
			ConfigManager configManager = ConfigManager.getInstance();
			
			assertEquals(configManager.getPropertyByName(PropertyKeys.targetFileToDBDatabase_DatabaseNamePrefix) + sid, // + alterValue
					storyTargetDB.getSourceDatabase());
			
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
			
			if (executor.getExceptions().size() > 0) {
				
				for (Exception e : executor.getExceptions()) {
					logger.error(String.format("testGetDataLoadExecutor failed: executor has more than 0 exceptions: %s", e.toString()));
				}
				
				fail("testGetDataLoadExecutor failed. Executor has more than 0 exceptions.");
			}
			
			
			
		} catch (Exception e) {
			
			
			logger.error("triggerDataLoadExecution failed: Couldn't trigger KTR execution for " + sid);
			
			fail("triggerDataLoadExecution failed: Couldn't trigger KTR execution for " + sid);
		}
	}

	private int prepareDatabase() throws Exception {
		int sid = Utils.getTestSid();
		
		//TODO: again depend on other test, BAD.
		DataSubmissionWizzardTest dataSubmissionTest = new DataSubmissionWizzardTest("");
		dataSubmissionTest.testStoreUploadedFiles();
		
		//TODO: this is bad, because unit tests should be FIRST (the important part is independent), but I couldn't figure our how to better do it.
		//we need to have ktr file created and the location of it stored in the DB.
		KTRManagerTest ktrManagerTest = new KTRManagerTest("");
		ktrManagerTest.testCreateKTR();
		
		return sid;
	}

	private void alterKTR(final int sid, final int alterValue) throws Exception {
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
