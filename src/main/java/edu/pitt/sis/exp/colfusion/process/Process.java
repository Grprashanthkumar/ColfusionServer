/**
 * 
 */
package edu.pitt.sis.exp.colfusion.process;

import java.io.Serializable;
import java.util.List;

/**
 * @author Evgeny
 *
 * This interface describes Col*Fusion processes that are run in a background by {@link ProcessManager}. This process are not OS processes. 
 * Each Col*Fusion process is executed in a separate Thread. 
 */
public interface Process extends Runnable, Serializable {
	
	/**
	 * Thread id is not unique. So we can use this method to be able to identify processes uniquely at any time. 
	 * At this should be probably mapped to the id of the row in DB where ProcessesManager stores all processes.
	 * 
	 * This method must be called in the {@link ProcessManager} when process is added to the database to set the id from the database.
	 * 
	 * @param uniqueName the name of the process to set.
	 */
	//TODO: rename to process id?
	public void setID(int id);
	
	/**
	 * The unique name of the process. Always unique. Mapped to the row id in the database table where Process Manager stores the processes.
	 * 
	 * @return the unique name of the process.
	 */
	public int getID();
	
	/**
	 * Checks whether a thread that executes the Col*Fusion process is active or not.
	 * 
	 * @return true if the thread is running and false otherwise.
	 */
    public abstract boolean isRunning();
    
    /**
     * Check whether a thread that executes the Col*Fusion process is done or not.
     * 
     * @return true if the thread is done and false otherwise.
     */
    public abstract boolean isDone();
    
    /**
     * Starts a new thread to execute the process in a separate thread. 
     * 
     * This method should only be called from ProcessManager.
     * 
     * @param manager is the {@link ProcessManager} that handles all Col*Fusion processes.
     */
    public abstract void startPerforming(ProcessManager manager);
    
    /**
     * Cancel execution of the process. 
     * 
     * This method should only be called from ProcessManager.
     */
    public abstract void cancel();
    
    /**
     * @return the {@link ProcessManager} that handles this process.
     */
    public ProcessManager getManager();
    
    /**
     * List of exceptions occurred during process execution.
     * 
     * @return the list of exceptions which occurred during the process execution.
     */
    public List<Exception> getExceptions();
    
    public void execute() throws Exception;
}
