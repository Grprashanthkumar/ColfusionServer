package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.List;
import java.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

/**
 * @author Hao Bai
 * 
 * This class is used for:
 *   1) Start/stop the monitoring process;
 *   2) return all six services's information 
 *   including status to lower users.
 */
public class ServiceMonitorController{

	private ServiceMonitor serviceMonitor;
	private int monitorPeriod;
	Timer timer;
	
	private Logger logger = LogManager.getLogger(ServiceMonitorController.class.getName());
	
	public ServiceMonitorController(){
		timer = new Timer();
		this.serviceMonitor = new ServiceMonitor();
		this.monitorPeriod = Integer.parseInt(ConfigManager.getInstance().getPropertyByName(PropertyKeys.ServiceMonitorPeriod));;
	}
	
	public ServiceMonitorController(ServiceMonitor servicemonitor, int period, Timer timerParameter){
		this.serviceMonitor = servicemonitor;
		this.monitorPeriod = period;
		this.timer = timerParameter;
	}
	
	public void setMonitorPeriod(int period){
		this.monitorPeriod = period;
	}
	
	public int getMonitorPeriod(){
		return this.monitorPeriod;
	}
	
	/*
	 * This function starts the process of service monitoring.
	 * If monitoring is started successfully, returns true.
	 * Otherwise, returns false.
	 */
	public boolean startServiceMonitor(){
		try{
			/*executes task as schedule(task, initial delay, delay period)*/
			this.timer.schedule(this.serviceMonitor, 0, this.monitorPeriod);
			return true;
		}
		catch(Exception exception){
			logger.error("In ServiceMonitorController.startServiceMonitor()\n"
					+exception.toString()+" "+exception.getMessage()+" "+exception.getCause());
			return false;
		}
	}
	
	/*
	 * This function stops the process of service monitoring.
	 * If monitoring is stopped successfully, returns true.
	 * Otherwise, returns false.
	 */
	public boolean stopServiceMonitor(){
		if(this.timer == null)
			return true;
		try{
			this.timer.cancel();
			this.serviceMonitor = null;
			return true;
		}
		catch(Exception exception){
			logger.error("In ServiceMonitorController.stopServiceMonitor()\n"
					+exception.toString()+" "+exception.getMessage()+" "+exception.getCause());		
			return false;
		}
	}
	
	/*
	 * This function returns all six services's status 
	 * with other information in a Service list.
	 * Note: Either the name or the function of this function
	 * is different from getServiceStatus() in Service.java
	 */
	public List<Service> getServicesStatus(){
		if(this.serviceMonitor.getServiceList().isEmpty() == true ||
		   this.serviceMonitor.getServiceList().size() < this.serviceMonitor.getServiceNumInDatabase())
			return null;
		
		return this.serviceMonitor.getServiceList();
	}
	
}
