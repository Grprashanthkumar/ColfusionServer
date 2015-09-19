/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.managers.ProcessPersistantManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ProcessPersistantManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionProcesses;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

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
	
	Logger logger = LogManager.getLogger(ProcessManager.class.getName());
	
	private final int _maxNumberOfConcurrentProceses; 
	
	private final ProcessPersistantManager processPersistantManager;
	
	
	private static ProcessManager instance = null;
	
	protected Map<Integer, Process> _runningProcesses;
    
    protected ProcessManager() {
    	_runningProcesses  = new HashMap<Integer, Process>();
    	
    	int maxNumberOfConcurrentProceses = 5; //This number comes from config file, but if it is missing in the config file then use this value as default.
    	
    	try {
    		maxNumberOfConcurrentProceses= Integer.parseInt(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_PROCESS_MANAGER_MAX_CONCURRENT_PROCESSES));
    	}
    	catch (Exception e) {
    		logger.info("maxNumberOfConcurrentProceses property was not parsed into int correctly (e.g., it might be missing), therefore is going to use the defaul number of max threads.");
    	}
    	
    	logger.info(String.format("Process Manager is going to use max of threads to execute Col*Fusion processes", maxNumberOfConcurrentProceses));
    	
    	_maxNumberOfConcurrentProceses = maxNumberOfConcurrentProceses;
    	
    	processPersistantManager = new ProcessPersistantManagerImpl();
    	
    	updateRunningProcessesQueue();
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
     * @return the process id is retured which can be used to get the actually process which was added to the queue.
     * @throws Exception if something goes wrong.
     */
    public int queueProcess(final Process process) throws Exception {
        
        int processId = addProcessToDB(process);
        
        //At this point process should have id assigned from DB. Check if it worked.
        
        //TODO: maybe this should be run in a separate queue?
        updateRunningProcessesQueue();    
        
        return processId;
    }
    
    public boolean hasRunningProcesses() {
    	return _runningProcesses.size() > 0;
    }
    
    public int countRunningProcess() {
    	return _runningProcesses.size();
    }
    
    /**
     * Saves given process to the database. If the process was new, then new process id is generated and process unique name is set to the project id.
     * 
     * NOTE: right now not sure what to do if the saving to the db fails... Need to come back and work on it.
     * 
     * @param process {@link Process} to save in the db.
     */
    private int addProcessToDB(final Process process) throws Exception {
    	
    	String processJson = ProcessBase.toJson(process);
    	
    	logger.info(String.format("Adding new process to the database %s", processJson));
    	
		try {
						
			ColfusionProcesses colfusionProcess = new ColfusionProcesses(ProcessStatusEnum.NEW.getValue(), processJson, process.getClass().getName(), "newly added process", null);
			
			processPersistantManager.saveOrUpdate(colfusionProcess);
			
			process.setID(colfusionProcess.getPid());
			
			//the originally serialized process doesn't have the id set up because the id is assigned only after the record is added to DB.
			colfusionProcess.setProcessSer(ProcessBase.toJson(process));
			processPersistantManager.saveOrUpdate(colfusionProcess);
			
			logger.info(String.format("Add process. Process id is %d", process.getID()));
			
			return colfusionProcess.getPid();
			
		} catch (Exception e) {
			// TODO: could not save the process to the db, should try to execute the process or fire an exception???
			logger.error(String.format("Could not save new process to the db. Not sure what to do next. %s", processJson), e);
			
			//TODO: create custom exception for this case?
			throw e;
		}	
	}

    /**
     * Removes the process from the list of running processes, updates process status and json ser in db, and run update method to trigger other processes if there are any in the db.
     * @param p the process which need to be updated. 
     */
	public void onDoneProcess(final Process p) {

		logger.info(String.format("About to the delete process %d from the list of running processes because the process is done. Actual status of the process is %b", p.getID(), p.isDone()));
		
		updateProcess(p, ProcessStatusEnum.DONE, "process succesfully finished");	
		
		updateRunningProcessesQueue();
    }
    
	/**
     * Removes the process from the list of running processes, updates process status and json ser in db, and run update method to trigger other processes if there are any in the db.
     * @param p the process which need to be updated. 
     */
	public void onFailedProcess(final Process p, final Exception exception) {
        //TODO: maybe exceptions should be kept somewhere, they used be added into a list.
		
		logger.info(String.format("About to the delete process %d from the list of running processes because the process is failed. Actual status of the process is %b", p.getID(), p.isDone()));
		
		updateProcess(p, ProcessStatusEnum.FAILED, "see process serialization exceptions");
		
		updateRunningProcessesQueue();
    }
	
	private void updateProcess(final Process p, final ProcessStatusEnum status, final String reasonForStatus) {
		//TODO: do we need to stop the thread associated with given colfusion process???
		
		updateProcessInDB(p, status, reasonForStatus);
		
		//TODO: seems like this if is redundant, the remove will check if the key exists or not...
		if (_runningProcesses.containsKey(p.getID())) {
			_runningProcesses.remove(p.getID());
		}
	}

	/**
     * Set process's status to given value and updated processSer in the db.
     * 
     * NOTE: right now not sure what to do if the saving to the db fails... Need to come back and work on it.
     * 
     * @param process {@link Process} to update in the db.
     * @param status the {@link ProcessStatusEnum} status to set.
     * @param reasonForStatus is the text that specify why the given statu is set.
     */
    private void updateProcessInDB(final Process p, final ProcessStatusEnum status, final String reasonForStatus) {
		logger.info(String.format("A process - %d - has finished, setting its status as done in the db.", p.getID()));
		
		if (p.getID() == -1) {
			logger.error(String.format("In the setProcessAsDone, but process has empty unique name. Not sure what to update in the db. Process: %s", ProcessBase.toJson(p)));
			
			return;
		}
		
		try {
			
			ColfusionProcesses colfusionProcess = processPersistantManager.findByID(p.getID());
			
			colfusionProcess.setStatus(status.getValue());
			colfusionProcess.setProcessSer(ProcessBase.toJson(p));
			colfusionProcess.setReasonForStatus(reasonForStatus);
			
			processPersistantManager.saveOrUpdate(colfusionProcess);
						
			logger.info(String.format("Updated process %d. Set status to %s because %s", p.getID(), status.getValue(), reasonForStatus));
			
		} catch (Exception e) {
			// TODO: could not save the process to the db, should try to execute the process or fire an exception???
			logger.error(String.format("Could not update process to the db. Not sure what to do next. %s", ProcessBase.toJson(p)));
		}
	}
    
    protected void updateRunningProcessesQueue() {
    	
    	logger.info(String.format("Updating processes queue. Currently there are %d running processes and max can be %d", _runningProcesses.size(), _maxNumberOfConcurrentProceses));
    	
        while (_runningProcesses.size() <= _maxNumberOfConcurrentProceses) {
        	        
        	//Because ProcessManager is initiated only once at app start and nowhere else process status are changed from new to running, this implementation should be safe.
        	// however because of parallel deployment this might be tricky.
            ColfusionProcesses processFromDB = getNewPendingProcessAndSetAsRunningFromDB();
            
            if (processFromDB == null) { // no more new processes.
            	
            	logger.info(String.format("No more pending processes, breaking the while loop in updateRunningProcessesQueue"));
            	
            	break;
            }
                  
            Process p = null;
            
            try {            	
            	String procesClass = processFromDB.getProcessClass();
            	
            	logger.info(String.format("Got a new pending process from the DB, Process Class is: %s", procesClass));
            	
				Class<?> cl = Class.forName(procesClass);
				
				logger.info(String.format("About to deserialize process for class %s from this JSON: %s", procesClass, processFromDB.getProcessSer()));
				
				p = (Process) ProcessBase.fromJson(processFromDB.getProcessSer(), cl);
	            
				logger.info(String.format("OK, process is deserialized and ready to be run. Process id = %d", p.getID()));				 
			} catch (ClassNotFoundException e) {
				logger.error(String.format("ClassNotFoundException in updateRunningProcessesQueue:"), e);
				
				updateColfusionProcessStatusOnlyFromDB(processFromDB, ProcessStatusEnum.FAILED, "ClassNotFoundException in updateRunningProcessesQueue: " + e.getMessage());
			}
			catch (Exception e) {
				logger.error(String.format("Exception in updateRunningProcessesQueue: "), e);
				
				updateColfusionProcessStatusOnlyFromDB(processFromDB, ProcessStatusEnum.FAILED, "Exception in updateRunningProcessesQueue: " + e.getMessage());
			}
            
            if (p != null) {
            	_runningProcesses.put(p.getID(), p);
    			
                p.startPerforming(this);
            }
            else {
            	logger.error(String.format("The process to run is null. Probably could not deserialize or instantiate a process form DB process"), processFromDB);
            	
            	updateColfusionProcessStatusOnlyFromDB(processFromDB, ProcessStatusEnum.FAILED, "The process to run is null. Probably could not deserialize or instantiate a process form DB process");
            }
        }
    }

	private void updateColfusionProcessStatusOnlyFromDB(final ColfusionProcesses processFromDB, final ProcessStatusEnum status, final String reasonForStatus) {
		try {
					
			processFromDB.setStatus(status.getValue());
			processFromDB.setReasonForStatus(reasonForStatus);
			
			processPersistantManager.saveOrUpdate(processFromDB);
						
			logger.info(String.format("Updated process %d. Set status to %s because %s", processFromDB.getPid(), status.getValue(), reasonForStatus));
			
		} catch (Exception e) {
			// TODO: not sure what to do next
			logger.error(String.format("failProcesFromDB failed for some reasons for process %d", processFromDB.getPid()), e);
		}
	}

	//This could be made synchronized, but because getting "new" pending process and setting it status to running are done in one transaction
	//then no two threads can read the same process as "new".
	private ColfusionProcesses getNewPendingProcessAndSetAsRunningFromDB() {
		ColfusionProcesses process = processPersistantManager.getNewPendingProcessAndSetAsRunningFromDB(ProcessStatusEnum.RUNNING.getValue(), 
				"added to running queue", _maxNumberOfConcurrentProceses);
		
		return process;
	}

	public ProcessStatusEnum getProcessStatus(final int processId) throws Exception {
		
		if (_runningProcesses.containsKey(processId)) {
			return ProcessStatusEnum.RUNNING;
		}
		else {
			ColfusionProcesses process = processPersistantManager.findByID(processId);
			
			if (process != null) {
				return ProcessStatusEnum.fromString(process.getStatus());
			}
			else {
				//TODO: make custom exception?
				throw new Exception(String.format("Process with id = %d not found in getProcessStatus", processId));
			}	
		}
	}
}
