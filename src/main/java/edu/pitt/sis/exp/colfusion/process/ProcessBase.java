/**
 * 
 */
package edu.pitt.sis.exp.colfusion.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;


/**
 * @author Evgeny
 *
 */
public abstract class ProcessBase implements Process {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	protected int _id = -1; // -1 means not set
	final protected String       _description;
	final private Date creationsTime = new Date();
	protected Date runStartTime;
	protected Date runEndTime;
	
	transient private ProcessManager     	 _manager;
	transient protected Thread             _thread;
    
    protected List<Exception>    _exceptions = new ArrayList<>();
    
    abstract protected Runnable getRunnable();
    
    protected ProcessBase() {
        _description = "";
    }
    
    protected ProcessBase(String description) {
        _description = description;        
    }

    @Override
    public void cancel() {
        if (_thread != null && _thread.isAlive()) {
            _thread.interrupt();
        }
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
            _thread.setName(String.valueOf(_id));
            
            _thread.start();
        }
    }
    
    @Override
	public ProcessManager getManager() {
		return _manager;
	}

	/**
	 * @param _manager the _manager to set
	 */
	protected void setManager(ProcessManager _manager) {
		this._manager = _manager;
	}

	@Override
	public List<Exception> getExceptions() {
		return _exceptions;
	}

	/**
	 * @param _exceptions the _exceptions to set
	 */
	protected void setExceptions(List<Exception> _exceptions) {
		this._exceptions = _exceptions;
	}
	
	@Override
	public void setID(int id) {
		_id = id;
	}
	
	@Override
	public int getID() {
		return _id;
	}
	
	/**
     * Serializes the object into JSON string.
     * @return string with a JSON representation of the object.
     */
	public static String toJson(Process process) {
		Gson gson = new Gson();
		String json = gson.toJson(process);
		
		return json;
	}
    
    @SuppressWarnings("unchecked")
    /**
     * Deserializes object from JSON string.
     * @return the object form JSON string.
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
    	Gson gson = new Gson();
    	
    	return gson.fromJson(json, clazz);
    }

	/**
	 * @return the creationsTime
	 */
	public Date getCreationsTime() {
		return creationsTime;
	}

	/**
	 * @return the runStratTime
	 */
	public Date getRunStartTime() {
		return runStartTime;
	}

	/**
	 * @param runStratTime the runStratTime to set
	 */
	public void setRunStartTime(Date runStartTime) {
		this.runStartTime = runStartTime;
	}

	/**
	 * @return the runEndTime
	 */
	public Date getRunEndTime() {
		return runEndTime;
	}

	/**
	 * @param runEndTime the runEndTime to set
	 */
	public void setRunEndTime(Date runEndTime) {
		this.runEndTime = runEndTime;
	}
	
	@Override
	public void run() {
		try {
			
			setRunStartTime(new Date());
			
			execute();
			
			//Not really the end of the run, but the next statement can hold the process for a long time.
			setRunEndTime(new Date());
			
			this.getManager().onDoneProcess(this);
		} catch (Exception e) {
			//TODO: add logger if needed here, or maybe all exceptions should be logged by process manager
			
			this._exceptions.add(e);
			
			//Not really the end of the run, but the next statement can hold the process for a long time.
			setRunEndTime(new Date());
			
			this.getManager().onFailedProcess(this, e);
		}	
	}
}
