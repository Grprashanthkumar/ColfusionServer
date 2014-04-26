/**
 * 
 */
package edu.pitt.sis.exp.colfusion.process;

import java.util.ArrayList;
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
	private static final long serialVersionUID = -1618488906520414665L;
	
	protected String _uniqueName;
	final protected String       _description;
	transient private ProcessManager     	 _manager;
	transient protected Thread             _thread;
    
    private List<Exception>    _exceptions;
    
    abstract protected Runnable getRunnable();
    
    protected ProcessBase() {
        _description = "";
        _exceptions = new ArrayList<>();
    }
    
    protected ProcessBase(String description) {
        _description = description;
        _exceptions = new ArrayList<>();
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
            _thread.setName(_uniqueName);
            
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
	public void setUniqueName(String uniqueName) {
		_uniqueName = uniqueName;
	}
	
	@Override
	public String getUniqueName() {
		return _uniqueName;
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
}
