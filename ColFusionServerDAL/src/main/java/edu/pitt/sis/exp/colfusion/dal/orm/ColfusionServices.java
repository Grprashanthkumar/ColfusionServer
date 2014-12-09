package edu.pitt.sis.exp.colfusion.dal.orm;

/**
 * @author Hao Bai
 * 
 * This class is used for establishing service structure,
 * collecting its information and reporting these information
 * to other classes like ServiceMonitor.
 */
public class ColfusionServices implements java.io.Serializable {

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
	private String servicePreviousStatus;
	
	public ColfusionServices(){
	}
	
	/**
	 * constructor
	 * 
	 * @param servicename
	 * @param address
	 * @param portnum
	 */
	public ColfusionServices(String servicename, String address, int portnum){
		this.serviceName = servicename;
		this.serviceAddress = address;
		this.portNumber = portnum;
	}
	
	/**
	 * set Service ID
	 * 
	 * @param id
	 */
	public void setServiceID(int id){
		this.serviceID = id;
	}
	
	/**
	 * get Service ID
	 * 
	 * @return id
	 */
	public int getServiceID(){
		return this.serviceID;
	}
	
	/**
	 * set Service name
	 * 
	 * @param servicename
	 */
	public void setServiceName(String servicename){
		this.serviceName = servicename;
	}
	
	/**
	 * get Service name
	 * 
	 * @return servicename
	 */
	public String getServiceName(){
		return this.serviceName;
	}
	
	/**
	 * set Service address
	 * 
	 * @param address
	 */
	public void setServiceAddress(String address){
		this.serviceAddress = address;
	}
	
	/**
	 * get Service address
	 * 
	 * @return address
	 */
	public String getServiceAddress(){
		return this.serviceAddress;
	}
	
	/**
	 * set Service port number
	 * 
	 * @param portnum
	 */
	public void setPortNumber(int portnum){
		this.portNumber = portnum;
	}
	
	/**
	 * get Service port number
	 * 
	 * @return portnum
	 */
	public int getPortNumber(){
		return this.portNumber;
	}
	
	/**
	 * set Service directory
	 * 
	 * @param dir
	 */
	public void setServiceDir(String dir){
		this.serviceDir = dir;
	}
	
	/**
	 * get Service directory
	 * 
	 * @return dir
	 */
	public String getServiceDir(){
		return this.serviceDir;
	}
	
	/**
	 * set Service command
	 * 
	 * @param command
	 */
	public void setServiceCommand(String command){
		this.serviceCommand = command;
	}
	
	/**
	 * get Service command
	 * 
	 * @return command
	 */
	public String getServiceCommand(){
		return this.serviceCommand;
	}
	
	/**
	 * set Service status
	 * 
	 * @param status
	 */
	public void setServiceStatus(String status){
		this.serviceStatus = status;
	}
	
	/**
	 * get Service status
	 * 
	 * @return status
	 */
	public String getServiceStatus(){
		return this.serviceStatus;
	}
	
	/**
	 * set Service Previous Status
	 * 
	 * @param status
	 */
	public void setServicePreviousStatus(String status){
		this.servicePreviousStatus = status;
	}
	
	/**
	 * get Service Previous Status
	 * 
	 * @return servicePreviousStatus
	 */
	public String getServicePreviousStatus(){
		return this.servicePreviousStatus;
	}
	
	public String toString(){
		return this.getServiceID() + this.getServiceName() + this.getServiceAddress() + this.getPortNumber()
				+ this.getServiceDir() + this.getServiceCommand() + this.getServiceStatus();
	}
}
