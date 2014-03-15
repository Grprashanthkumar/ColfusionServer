/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

/**
 * The interface defines only one method which will be responsible for the whole data load process.
 * The actual implementations should inherit from the base implementation and call execute of the base class first.
 * 
 * @author Evgeny
 *
 */
public interface DataLoadExecutor extends edu.pitt.sis.exp.colfusion.process.Process {
	
	/**
	 * Performs the data load execution.
	 * 
	 * @param sid
	 */
	void execute();
	
	public void setSid(int sid);
	
	public int getSid();
}
