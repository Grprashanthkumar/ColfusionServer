/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

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
		
		super.execute();
		
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		execute();
		
	}

	@Override
	protected Runnable getRunnable() {
		return this;
	}
}
