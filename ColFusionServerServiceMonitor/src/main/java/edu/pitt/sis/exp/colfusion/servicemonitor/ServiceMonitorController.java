package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.List;
import java.util.Timer;

/**
 * @author Hao Bai
 * 
 * This class is used for:
 *   1) Start/stop the monitoring process;
 *   2) return all six services's information 
 *   including status to lower users.
 */
public class ServiceMonitorController{

	private ServiceMonitor serv_mon;
	private int monitorPeriod;
	Timer timer;
	
	public ServiceMonitorController(){
		timer= new Timer();
		this.serv_mon= new ServiceMonitor();
		this.monitorPeriod= 5000;
	}
	
	public ServiceMonitorController(ServiceMonitor servmon, int period, Timer timer_t){
		this.serv_mon= servmon;
		this.monitorPeriod= period;
		this.timer= timer_t;
	}
	
	public void setMonitorPeriod(int period){
		this.monitorPeriod= period;
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
			this.timer.schedule(this.serv_mon, 0, this.monitorPeriod);
			return true;
		}
		catch(Exception excep){
			System.out.println(excep.toString()+" "+ excep.getMessage()+" "+ excep.getCause());
			return false;
		}
	}
	
	/*
	 * This function stops the process of service monitoring.
	 * If monitoring is stopped successfully, returns true.
	 * Otherwise, returns false.
	 */
	public boolean stopServiceMonitor(){
		if(this.timer==null)
			return true;
		try{
			this.timer.cancel();
			this.serv_mon= null;
			return true;
		}
		catch(Exception excep){
			System.out.println(excep.toString()+" "+ excep.getMessage()+" "+ excep.getCause());			
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
		if(this.serv_mon.getServiceList().isEmpty()==true || this.serv_mon.getServiceList().size()<6)
			return null;
		
		return this.serv_mon.getServiceList();
	}
	
}
