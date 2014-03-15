/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

import java.util.ArrayList;

import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDB;

/**
 * @author Evgeny
 *
 */
public class DataLoadExecutorKTRImpl extends DataLoadExecutorBaseImpl implements
		DataLoadExecutor {
	
	public DataLoadExecutorKTRImpl() {
	
	}
	
	public DataLoadExecutorKTRImpl(int sid) {
		this.sid = sid;
	}
	
	@Override
	public void execute() {
		
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ArrayList<String> ktrLocations = storyMgr.getStoryKTRLocations(sid);
		
		boolean firstKtr = true;
		
		for(String ktrLocation : ktrLocations) {
			
			if (firstKtr) {
				KTRManager ktrManager = new KTRManager(sid);
				
				try {
					ktrManager.loadKTR(ktrLocation);
								
					StoryTargetDB sourceDBInfo = ktrManager.readTargetDatabaseInfo();
					super.updateSourceDBInfo(sourceDBInfo);
					firstKtr = false;
				} catch (Exception e) {
					this._manager.onFailedProcess(this, e);
					return;
				}
				
			}		
		}
	}

	@Override
	public void run() {
		execute();	
		
		this._manager.onDoneProcess(this);
	}

	@Override
	protected Runnable getRunnable() {
		return this;
	}
}
