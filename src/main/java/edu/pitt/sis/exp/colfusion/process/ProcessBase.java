/**
 * 
 */
package edu.pitt.sis.exp.colfusion.process;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Evgeny
 *
 */
public abstract class ProcessBase implements Process {

	final protected String       _description;
    private ProcessManager     	 _manager;
    protected Thread             _thread;
    protected int                _progress; // out of 100
    protected boolean            _canceled;
    
    private List<Exception>    _exceptions;
    
    protected ProcessBase() {
        _description = "";
        _exceptions = new ArrayList<>();
    }
    
    protected ProcessBase(String description) {
        _description = description;
    }

    @Override
    public void cancel() {
        _canceled = true;
        if (_thread != null && _thread.isAlive()) {
            _thread.interrupt();
        }
    }

    @Override
    public boolean isImmediate() {
        return false;
    }
    
    @Override
    public boolean isRunning() {
        return _thread != null && _thread.isAlive();
    }
    
    @Override
    public boolean isDone() {
        return _thread != null && !_thread.isAlive();
    }

    @Override
    public void startPerforming(ProcessManager manager) {
        if (_thread == null) {
            setManager(manager);
            
            _thread = new Thread(getRunnable());
            _thread.start();
        }
    }
    
    abstract protected Runnable getRunnable();

	/**
	 * @return the _manager
	 */
	public ProcessManager getManager() {
		return _manager;
	}

	/**
	 * @param _manager the _manager to set
	 */
	protected void setManager(ProcessManager _manager) {
		this._manager = _manager;
	}

	/**
	 * @return the _exceptions
	 */
	public List<Exception> getExceptions() {
		return _exceptions;
	}

	/**
	 * @param _exceptions the _exceptions to set
	 */
	protected void setExceptions(List<Exception> _exceptions) {
		this._exceptions = _exceptions;
	}
}
