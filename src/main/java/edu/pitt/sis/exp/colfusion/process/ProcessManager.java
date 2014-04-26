/**
 * 
 */
package edu.pitt.sis.exp.colfusion.process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;

/**
 * @author Evgeny
 *
 * ProcessManager is responsible to manage all the Col*Fusion processes that should be executed in a background. 
 * The processes are back up in the database and their states are reflected in the database too, e.g. if the process is being executed or if it failed or done.
 * 
 * The project manager is implemented as a singleton patter, but if something happens and the app crashes, on the next initialization, 
 * ProjectManager will read processes from database and start from the last not executed one.
 * 
 */
public class ProcessManager {
	
	private final int _maxNumberOfConcurrentProceses;
	
	private static ProcessManager instance = null;
	
	protected Map<String, Process> _runningProcesses;
    protected List<Exception> _latestExceptions;
    
    protected ProcessManager() {
    	_runningProcesses  = new HashMap<String, Process>();
    	
    	_maxNumberOfConcurrentProceses = Integer.parseInt(ConfigManager.getInstance().getPropertyByName(PropertyKeys.maxNumberOfConcurrentProceses));
    }
    
    public static ProcessManager getInstance() {
		if(instance == null) {
	         instance = new ProcessManager();
	    }
	      
		return instance;
	}
   
    /**
     * This adds a {@link Process} to the queue. 
     * Also at this moment the process is serialized into db.
     * Also at this moment the process gets the unique name. 
     * 
     * @param process is the process to add to the queue.
     * @throws Exception if something goes wrong.
     */
    public void queueProcess(Process process) throws Exception {
        
        addProcessToDB(process);
        
        update();        
    }
    
    public void onDoneProcess(Process p) {
        _processes.remove(p);
        update();
    }
    
    //TODO: check if the next two methods really works as they are supposed to work. Seems like at all times, the _exeptions list will have only one exception.
    
    public void onFailedProcess(Process p, Exception exception) {
        List<Exception> exceptions = new LinkedList<Exception>();
        
        p.getExceptions().add(exception);
        
        exceptions.add(exception);
        onFailedProcess(p, exceptions);
    }
    
    public void onFailedProcess(Process p, List<Exception> exceptions) {
        _latestExceptions = exceptions;
        _processes.remove(p);
        // Do not call update(); Just pause?
    }
    
    public void cancelAll() {
        for (Process p : _processes) {
            if (p.isRunning()) {
                p.cancel();
            }
        }
        _processes.clear();
        _latestExceptions = null;
    }
    
    protected void update() {
        while (_processes.size() > 0) {
            Process p = _processes.get(0);
            
            if (p.isDone()) {
                _processes.remove(0);
            } else {
                if (!p.isRunning()) {
                    _latestExceptions = null;
                    p.startPerforming(this);
                }
                
                break;
            }
        }
    }
}
