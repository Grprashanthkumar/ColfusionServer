package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;




/**
 * Apache Commons Net library implements the client side of many basic Internet protocols. 
 * The purpose of the library is to provide fundamental protocol access, 
 * not higher-level abstractions.--Apache Commons
 * IMPORTNENT: Need to import commons-net-3.3.jar, commons-net-3.3-sources.jar and commons-net-examples-3.3.jar
 * 	and build paths for them.
 */
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

/**
 * @author Hao Bai
 * 
 * This class is used for:
 *   1) monitoring a specific service's status;
 *   2) start or stop a specific service.
 */
public class ServiceMonitor extends TimerTask{
	
	private List<Service> serviceList;
	private int timeOut;
	DatabaseConnection dataBaseconnection;
	EmailNotifier emailNotifier;
	
	private Logger logger = LogManager.getLogger(ServiceMonitor.class.getName());
	
	/**
	 * Set the default time out as 3 seconds.
	 */
	public ServiceMonitor(){
		this.serviceList = new ArrayList<Service>();
		this.timeOut = Integer.parseInt(ConfigManager.getInstance().getPropertyByName(PropertyKeys.ServiceMonitorTimeOut));;
		dataBaseconnection = new DatabaseConnection();
		emailNotifier = new EmailNotifier();
	}
	
	public ServiceMonitor(List<Service> servicelist, int timeout){
		this.serviceList = servicelist;
		this.timeOut = timeout;
	}
	
	public void setServiceList(List<Service> servicelist){
		this.serviceList = servicelist;
	}
	
	public List<Service> getServiceList(){
		return this.serviceList;
	}
	
	public void setTimeOut(int timeout){
		this.timeOut = timeout;
	}
	
	public int getTimeOut(){
		return this.timeOut;
	}
	
	public int getServiceNumInDatabase(){
		return this.dataBaseconnection.queryAllServies().size();
	}
	
	/**
	 * Use TelnetClient class in commons-net.jar to test connection of 
	 * a remote/local service providing its IP and Port.
	 * Also set the timeout, with default 3 seconds.
	 * If the service is connected, returns true;
	 * Else, connect() reports connect exceptions, thus, returns false.
	 */
	public boolean isServiceConnected(Service service){	
        try{
            TelnetClient client = new TelnetClient();
            client.setDefaultTimeout(this.getTimeOut());
            client.connect(service.getServiceAddress(), service.getPortNumber());
            client.disconnect();
            return true;
        }
        catch(Exception exception){
        	return false;
        }
	}
	
	/**
	 * Return current service's status.
	 */
	public String updateServiceStatus(Service service){
		if(this.isServiceConnected(service) == true){
			service.setServiceStatus(ServiceStatusEnum.RUNNING.getValue());
		}
		else{
			service.setServiceStatus(ServiceStatusEnum.STOPPED.getValue());
		}
		return service.getServiceStatus();
	}
	
	/*
	 * This TimerTask run() function gets services' information
	 * from database, and monitor status of all the six services.
	 * If status of some service is changed, this function also 
	 * update the display information as well as the status data
	 * in database.
	 */
	public void run(){
		String currentStatus = null;
		try{
			if(this.serviceList.isEmpty() == true)
				this.serviceList = this.dataBaseconnection.queryAllServies();
			for(Service service : serviceList){
				currentStatus = this.updateServiceStatus(service);
				this.dataBaseconnection.updateServiceStatus(service);
				serviceList.set(serviceList.indexOf(service), service);
				if(currentStatus == ServiceStatusEnum.STOPPED.getValue() && 
				   currentStatus != service.getServicePreviousStatus()){
					for(String userLevel : ConfigManager.getInstance().getPropertyByName(PropertyKeys.userLevel).split(",")){
						for(String emailAddress : this.dataBaseconnection.queryUserEmails(userLevel)){
							emailNotifier.sendMail(emailAddress, 
												   "Service Status changed: " + service.getServiceName(), 
												   service.getServiceName() + " has been stopped!");
						}
					}
				}
				if(currentStatus != service.getServicePreviousStatus())
					service.setServicePreviousStatus(currentStatus);
				currentStatus = null;
			}
		}
		catch(Exception exception){
			logger.error("In ServiceMonitor.run()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
		}
	}
	
	/* Below are functions unavailable yet.
	 * */
	/*
	 * This function starts the service which is currently
	 * stopped or without running.
	 * RETURN STATUS:
	 *   0: Unknown service
	 *   1: Already started
	 *   2: successfully started
	 *   3: Other Exceptions
	 */
	public int startService(Service service){
		if(service.getServiceName() == null || service.getServiceName() == "")
			return 0;
		else if(service.getServiceStatus() == ServiceStatusEnum.RUNNING.getValue())
			return 1;
		else if(service.getServiceStatus() == ServiceStatusEnum.STOPPED.getValue()){
			try{
				
				//String s;
		        Process proc;
		        //proc= Runtime.getRuntime().exec(serv.getServiceDir()+ serv.getServiceCommand());
		        proc= Runtime.getRuntime().exec("E:\\workspace\\delbat.bat");
	            //BufferedReader br = new BufferedReader(
	            //    new InputStreamReader(p.getInputStream()));
	            //while ((s = br.readLine()) != null)
	            //    System.out.println("line: " + s);
		        proc.waitFor();
	            //System.out.println ("exit: " + proc.exitValue());
	            proc.destroy();
	            
				return 2;
			}
			catch(Exception exception){
				logger.error("In ServiceMonitor.startService()\n"
						+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
				return 3;
			}
		}
		return 3;
	}
	
	/*
	 * This function stops the service which is currently
	 * started.
	 */
	//public boolean stopService(Service serv){
		
	//}
}
