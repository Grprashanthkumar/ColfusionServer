/**
 * 
 */
package edu.pitt.sis.exp.colfusion.process;


/**
 * @author Evgeny
 *
 */
public abstract class ProcessBase implements Process {

	final protected String       _description;
    protected ProcessManager     _manager;
    protected Thread             _thread;
    protected int                _progress; // out of 100
    protected boolean            _canceled;
    
    protected ProcessBase() {
        _description = "";
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
            _manager = manager;
            
            _thread = new Thread(getRunnable());
            _thread.start();
        }
    }
    
    abstract protected Runnable getRunnable();
}
