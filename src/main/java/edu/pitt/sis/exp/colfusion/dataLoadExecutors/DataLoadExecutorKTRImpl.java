/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.ExecutionInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDB;

/**
 * @author Evgeny
 *
 */
public class DataLoadExecutorKTRImpl extends DataLoadExecutorBaseImpl implements
		DataLoadExecutor {
	
	Logger logger = LogManager.getLogger(DataLoadExecutorKTRImpl.class.getName());
	
	public DataLoadExecutorKTRImpl() {
	
	}
	
	public DataLoadExecutorKTRImpl(int sid) {
		this.sid = sid;
	}
	
	@Override
	public void execute() throws Exception {
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(sid);
		
		//If there are several files, there will be several KTR files accosted with one sid, however they all will be associated whit one target database
		//therefore we need only one KTR file to extract and save target database connection info.
		boolean firstKtr = true;
		
		ExecutionInfoManager executionInfoMgr = new ExecutionInfoManagerImpl();
		
		for(String ktrLocation : ktrLocations) {
			
			KTRManager ktrManager = new KTRManager(sid);
			ktrManager.loadKTR(ktrLocation);
			
			int executionLogId = executionInfoMgr.getExecutionLogId(sid, ktrManager.getTableName());
			
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.IN_PROGRESS);
			
			if (firstKtr) {
				
				//If there are several files, there will be several KTR files accosted with one sid, however they all will be associated whit one target database
				//therefore we need only one KTR file to extract and save target database connection info.
				
				updateTargetDatabaseConnectionInfo(executionInfoMgr, executionLogId, ktrManager, ktrLocation);
				
				firstKtr = false;
			}
			
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.SUCCESS);
		}
	}

	private void updateTargetDatabaseConnectionInfo(ExecutionInfoManager executionInfoMgr, int executionLogId, KTRManager ktrManager, String ktrLocation) throws Exception {
		try {
			executionInfoMgr.appendLog(executionLogId, String.format("Starting to read traget database info from the KTR file located at %s", ktrLocation));
			
			StoryTargetDB sourceDBInfo = ktrManager.readTargetDatabaseInfo();
			
			executionInfoMgr.appendLog(executionLogId, String.format("Finished reading traget database info from the KTR file located at %s", ktrLocation));
			
			executionInfoMgr.appendLog(executionLogId, String.format("Starting to update sourceintoDB record with target database conneciton info fetched form the ktr file %s. "
					+ "Here is what connection info is: %s ", ktrLocation, sourceDBInfo.toString()));
			
			super.updateSourceDBInfo(sourceDBInfo);
			
			executionInfoMgr.appendLog(executionLogId, "Finished update sourceintoDB record with target database conneciton info");
		} catch (Exception e) {
			
			executionInfoMgr.updateStatus(executionLogId, DataLoadExecutionStatus.FAILED);
			
			throw e;
		}
		
	}

	@Override
	public void run() {
		
		try {
			execute();
			
			this._manager.onDoneProcess(this);
		} catch (Exception e) {
			//TODO: add logger if needed here, or maybe all exceptions should be logged by process manager
			
			this._manager.onFailedProcess(this, e);
		}	
	}

	@Override
	protected Runnable getRunnable() {
		return this;
	}
}
