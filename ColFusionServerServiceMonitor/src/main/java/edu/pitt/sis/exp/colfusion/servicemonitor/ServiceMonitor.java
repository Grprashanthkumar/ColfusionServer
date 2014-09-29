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
	DatabaseConnection dbconn;
	
	/**
	 * Set the default time out as 3 seconds.
	 */
	public ServiceMonitor(){
		this.serviceList= new ArrayList<Service>();
		this.timeOut= 3000;
		dbconn= new DatabaseConnection();
	}
	
	public ServiceMonitor(List<Service> serv_list, int timeout){
		this.serviceList= serv_list;
		this.timeOut= timeout;
	}
	
	public void setServiceList(List<Service> serv_list){
		this.serviceList= serv_list;
	}
	
	public List<Service> getServiceList(){
		return this.serviceList;
	}
	
	public void setTimeOut(int timeout){
		this.timeOut= timeout;
	}
	
	public int getTimeOut(){
		return this.timeOut;
	}
	
	/**
	 * Use TelnetClient class in commons-net.jar to test connection of 
	 * a remote/local service providing its IP and Port.
	 * Also set the timeout, with default 3 seconds.
	 * If the service is connected, returns true;
	 * Else, connect() reports connect exceptions, thus, returns false.
	 */
	public boolean isServiceConnected(Service serv){	
        try{
            TelnetClient client= new TelnetClient();
            client.setDefaultTimeout(this.getTimeOut());
            client.connect(serv.getServiceAddress(), serv.getPortNumber());
            client.disconnect();
            return true;
        }
        catch(Exception e){
        	return false;
        }
	}
	
	/**
	 * Return current service's status:
	 * 0 is stopped, 2 is running.
	 */
	public void updateServiceStatus(Service serv){
		if(this.isServiceConnected(serv)==true){
			serv.setServiceStatus(2);
		}
		else{
			serv.setServiceStatus(0);
		}
	}
	
	/*
	 * This TimerTask run() function gets services' information
	 * from database, and monitor status of all the six services.
	 * If status of some service is changed, this function also 
	 * update the display information as well as the status data
	 * in database.
	 */
	public void run(){
		try{
			if(this.serviceList.isEmpty()==true)
				this.serviceList= this.dbconn.queryAllServies();
			for(Service serv: serviceList){
				this.updateServiceStatus(serv);
				this.dbconn.updateServiceStatus(serv);
				serviceList.set(serviceList.indexOf(serv), serv);
			}
		}
		catch(Exception excep){
			System.out.println(excep.toString()+" "+ excep.getMessage()+" "+ excep.getCause());
		}
	}
	
	/* Below are functions unused
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
	public int startService(Service serv){
		if(serv.getServiceName()==null|| serv.getServiceName()=="")
			return 0;
		else if(serv.getServiceStatus()== "running")
			return 1;
		else if(serv.getServiceStatus()== "stopped"){
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
			catch(Exception e){
				e.printStackTrace();
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
