/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.process.ProcessBase;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDB;

/**
 * All executors should inherit from this class and run this class execute method as the first instruction in their execute method.
 * 
 * @author Evgeny
 *
 */
public abstract class DataLoadExecutorBaseImpl extends ProcessBase implements DataLoadExecutor {

	protected int sid;
	
	@Override
	public void execute() throws Exception {
		// TODO add insert into sourceinfoDB table and linked servers.

	}

	/**
	 * Updates/inserts record about target database (the database where the data should be loaded).
	 * 
	 * @param sourceDBInfo the info about target database.
	 * @throws Exception 
	 */
	public void updateSourceDBInfo(StoryTargetDB sourceDBInfo) throws Exception {
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		
		storyMgr.saveOrUpdateSourceInfoDB(sourceDBInfo);
	}
	
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public int getSid() {
		return this.sid;
	}
}
