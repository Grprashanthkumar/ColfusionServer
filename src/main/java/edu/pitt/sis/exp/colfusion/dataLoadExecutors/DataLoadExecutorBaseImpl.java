/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

import edu.pitt.sis.exp.colfusion.process.ProcessBase;

/**
 * All executors should inherit from this class and run this class execute method as the first instruction in their execute method.
 * 
 * @author Evgeny
 *
 */
public abstract class DataLoadExecutorBaseImpl extends ProcessBase implements DataLoadExecutor {

	protected int sid;
	
	@Override
	public void execute() {
		// TODO add insert into sourceinfoDB table and linked servers.

	}

	
	public void setSid(int sid) {
		this.sid = sid;
	}
	
	public int getSid() {
		return this.sid;
	}
}
