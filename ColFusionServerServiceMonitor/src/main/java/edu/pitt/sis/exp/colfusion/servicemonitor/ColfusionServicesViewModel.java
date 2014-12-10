package edu.pitt.sis.exp.colfusion.servicemonitor;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

/**
 * @author Hao Bai
 *
 *Example JSON format is 
{
	"serviceName": "testService1",
	"serviceAddress": "127.0.0.1",
	"portNumber": 50,
	"serviceDir": "./",
	"serviceCommand": "cmd",
	"serviceStatus": "stopped"
}
 */
@XmlRootElement
public class ColfusionServicesViewModel {

	@Expose private int serviceID;
	@Expose private String serviceName;
	@Expose private String serviceAddress;
	@Expose private int portNumber;
	@Expose private String serviceDir;
	@Expose private String serviceCommand;
	@Expose private String serviceStatus;

	/**
	 * get id of service
	 * 
	 * @return serviceID
	 */
	public int getServiceID() {
		return serviceID;
	}

	/**
	 * set id of service
	 * 
	 * @param serviceID
	 */
	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	/**
     * get name of service.
     * 
     * @return serviceName.
     */
	public String getServiceName() {
		return serviceName;
	}
	
	/**
	 * set name of service
	 * 
	 * @param serviceName
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/**
	 * get address of service
	 * 
	 * @return serviceAddress
	 */
	public String getServiceAddress() {
		return serviceAddress;
	}
	
	/**
	 * set address of service
	 * 
	 * @param serviceAddress
	 */
	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	
	/**
	 * get port number of service
	 * 
	 * @return portNumber
	 */
	public int getPortNumber() {
		return portNumber;
	}
	
	/**
	 * set port number of service
	 * 
	 * @param portNumber
	 */
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
	/**
	 * get directory of service
	 * 
	 * @return serviceDir
	 */
	public String getServiceDir() {
		return serviceDir;
	}
	
	/**
	 * set directory of service
	 * 
	 * @param serviceDir
	 */
	public void setServiceDir(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	
	/**
	 * get Command of service
	 * 
	 * @return serviceCommand
	 */
	public String getServiceCommand() {
		return serviceCommand;
	}
	
	/**
	 * set Command of service
	 * 
	 * @param serviceCommand
	 */
	public void setServiceCommand(String serviceCommand) {
		this.serviceCommand = serviceCommand;
	}
	
	/**
	 * get status of service
	 * 
	 * @return serviceStatus
	 */
	public String getServiceStatus() {
		return serviceStatus;
	}
	
	/**
	 * set status of service
	 * 
	 * @param serviceStatus
	 */
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
}
