package edu.pitt.sis.exp.colfusion.servicemonitor;

/**
 * @author Hao Bai
 * 
 * This class is used for establishing service structure,
 * collecting its information and reporting these information
 * to other classes like ServiceMonitor.
 */
public class Service {

	/*
	 * Service Attributes include: 1)name, 2)IP address, 3)port #,
	 * 4)the director under which the service is installed,
	 * 5)the command(s) by which the service can be 
	 * started, stopped, renewed or suspended etc,
	 * and the status of service, which could be: stopped, started.
	 */
	private int serviceID;
	private String serviceName;
	private String serviceAddress;
	private int portNumber;
	private String serviceDir;
	private String serviceCommand;
	private String serviceStatus;
	private static final String[] serviceStatusType= {"stopped", "started", "running"};
	
	public Service(){
	}
	
	public Service(String servicename, String address, int portnum){
		this.serviceName= servicename;
		this.serviceAddress= address;
		this.portNumber= portnum;
	}
	
	public void setServiceID(int id){
		this.serviceID= id;
	}
	
	public int getServiceID(){
		return this.serviceID;
	}
	
	public void setServiceName(String servicename){
		this.serviceName= servicename;
	}
	
	public String getServiceName(){
		return this.serviceName;
	}
	
	public void setServiceAddress(String address){
		this.serviceAddress= address;
	}
	
	public String getServiceAddress(){
		return this.serviceAddress;
	}
	
	public void setPortNumber(int portnum){
		this.portNumber= portnum;
	}
	
	public int getPortNumber(){
		return this.portNumber;
	}
	
	public void setServiceDir(String dir){
		this.serviceDir= dir;
	}
	
	public String getServiceDir(){
		return this.serviceDir;
	}
	
	public void setServiceCommand(String command){
		this.serviceCommand= command;
	}
	
	public String getServiceCommand(){
		return this.serviceCommand;
	}
	
	public void setServiceStatus(int statusNum){
		this.serviceStatus= serviceStatusType[statusNum];
	}
	
	public void setServiceStatus(String status){
		this.serviceStatus= status;
	}
	
	public String getServiceStatus(){
		return this.serviceStatus;
	}
	
	public String toString(){
		return this.getServiceID()+ this.getServiceName()+ this.getServiceAddress()+ this.getPortNumber()
				+ this.getServiceDir()+ this.getServiceCommand()+ this.getServiceStatus();
	}
}
