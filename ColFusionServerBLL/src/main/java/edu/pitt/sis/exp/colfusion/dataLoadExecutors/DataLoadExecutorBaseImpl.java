/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTargetDBViewModel;
import edu.pitt.sis.exp.colfusion.process.ProcessBase;

/**
 * All executors should inherit from this class and run this class execute method as the first instruction in their execute method.
 * 
 * @author Evgeny
 *
 */
public abstract class DataLoadExecutorBaseImpl extends ProcessBase implements DataLoadExecutor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8124562049915428600L;
	
	@Expose protected int sid;

	/**
	 * Updates/inserts record about target database (the database where the data should be loaded).
	 * @param executionLogId 
	 * @param executionInfoMgr 
	 * 
	 * @param sourceDBInfo the info about target database.
	 * @throws Exception 
	 */
	protected void updateSourceDBInfo(final ExecutionInfoManager executionInfoMgr, final int executionLogId, final StoryTargetDBViewModel sourceDBInfo) throws Exception {
		
		executionInfoMgr.appendLog(executionLogId, String.format("Starting to update sourceintoDB record with target database conneciton info : %s ", 
				sourceDBInfo.toString()));
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		
		storyMgr.saveOrUpdateSourceInfoDB(sourceDBInfo);
		
		executionInfoMgr.appendLog(executionLogId, "Finished update sourceintoDB record with target database conneciton info");
	}
		
	@Override
	public void setSid(final int sid) {
		this.sid = sid;
	}
	
	@Override
	public int getSid() {
		return this.sid;
	}
}
