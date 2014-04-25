/**
 * 
 */
package edu.pitt.sis.exp.colfusion.process;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Evgeny
 *
 */
public class ProcessManager {
	
	private static ProcessManager instance = null;
	
	protected List<Process> _processes;
    protected List<Exception> _latestExceptions;
    
    protected ProcessManager() {
    	_processes  = new LinkedList<Process>();
    }
    
    public static ProcessManager getInstance() {
		if(instance == null) {
	         instance = new ProcessManager();
	    }
	      
		return instance;
	}
   
    public void queueProcess(Process process) throws Exception {
        if (process.isImmediate() && _processes.size() == 0) {
            _latestExceptions = null;
            //TODO
        } else {
            _processes.add(process);
            
            update();
        }
    }
    
    public boolean hasPending() {
        return _processes.size() > 0;
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
            if (!p.isImmediate() && p.isRunning()) {
                p.cancel();
            }
        }
        _processes.clear();
        _latestExceptions = null;
    }
    
    protected void update() {
        while (_processes.size() > 0) {
            Process p = _processes.get(0);
            if (p.isImmediate()) {
                _latestExceptions = null;
                try {
                    //TODO: p.performImmediate();
                } catch (Exception e) {
                    // TODO: Not sure what to do yet
                    e.printStackTrace();
                }
                _processes.remove(0);
            } else if (p.isDone()) {
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
