package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
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
	
	private static List<Service> serviceList;
	private int monitorPeriod;
	private int timeOut;
	
	/**
	 * Set the default time out as 3 seconds.
	 */
	public ServiceMonitor(){
		serviceList= new ArrayList<Service>();
		this.monitorPeriod= 10000;
		this.timeOut= 3000;
	}
	
	public ServiceMonitor(int period, int timeout){
		this.monitorPeriod= period;
		this.timeOut= timeout;
	}
	
	public void setMonitorPeriod(int period){
		this.monitorPeriod= period;
	}
	
	public int getMonitorPeriod(){
		return this.monitorPeriod;
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
            //e.printStackTrace();
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
	
	public void run(){
		try{
			Service ser_1= new Service("Carte", "127.0.0.1", 8081);
			Service ser_2= new Service("Apache", "127.0.0.1", 80);
			Service ser_3= new Service("MySQL", "127.0.0.1", 3306);
			Service ser_4= new Service("Tomcat", "127.0.0.1", 8080);
			Service ser_5= new Service("OpenRefine", "127.0.0.1", 3333);
			Service ser_6= new Service("Neo4j", "127.0.0.1", 7474);
			
			this.updateServiceStatus(ser_1);
			this.updateServiceStatus(ser_2);
			this.updateServiceStatus(ser_3);
			this.updateServiceStatus(ser_4);
			this.updateServiceStatus(ser_5);
			this.updateServiceStatus(ser_6);
			
			if(serviceList.isEmpty()==true){
				serviceList.add(0, ser_1);
				serviceList.add(1, ser_2);
				serviceList.add(2, ser_3);
				serviceList.add(3, ser_4);
				serviceList.add(4, ser_5);
				serviceList.add(5, ser_6);
			}
			else{
				serviceList.set(0, ser_1);
				serviceList.set(1, ser_2);
				serviceList.set(2, ser_3);
				serviceList.set(3, ser_4);
				serviceList.set(4, ser_5);
				serviceList.set(5, ser_6);
			}
		}
		catch(Exception excep){
			System.out.println(excep.toString()+" "+ excep.getMessage()+" "+ excep.getCause());
		}
	}
	
	/*
	 * This function starts the process of service monitoring.
	 * If monitoring is started successfully, returns true.
	 * Otherwise, returns false.
	 */
	public boolean startServiceMonitor(){
		Timer timer= new Timer();
		try{
			/*executes task as schedule(task, initial delay, delay period)*/
			timer.schedule(new ServiceMonitor(), 0, this.monitorPeriod);
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
	//public boolean stopServiceMonitor(){
		
	//}
	
	/*
	 * This function returns all six services's status
	 * in a list of string, as <"stopped","started"...>
	 * Note: Either the name or the function of this function
	 * is different from getServiceStatus() in Service.java
	 */
	public List<Service> getServicesStatus(){
		
		if(serviceList.isEmpty()==true || serviceList.size()<6){
			System.out.println("abc");
			return null;
		}
		for(Service item: serviceList)
			System.out.println(item.getServiceName());
		return serviceList;
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
