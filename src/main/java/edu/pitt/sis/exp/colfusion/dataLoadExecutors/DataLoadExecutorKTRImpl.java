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
			
			if (firstKtr) {
				
				//If there are several files, there will be several KTR files accosted with one sid, however they all will be associated whit one target database
				//therefore we need only one KTR file to extract and save target database connection info.
				
				try {
					StoryTargetDB sourceDBInfo = ktrManager.readTargetDatabaseInfo();
					super.updateSourceDBInfo(sourceDBInfo);
					firstKtr = false;
				} catch (Exception e) {
					//TODO: add logger if needed here, or maybe all exceptions should be logged by process manager
					
					this._manager.onFailedProcess(this, e);
					return;
				}
			}
			
			
		}
	}

	@Override
	public void run() {
		
		try {
			execute();
		} catch (Exception e) {
			//TODO: add logger if needed here, or maybe all exceptions should be logged by process manager
			
			this._manager.onFailedProcess(this, e);
		}	
		
		this._manager.onDoneProcess(this);
	}

	@Override
	protected Runnable getRunnable() {
		return this;
	}
}
