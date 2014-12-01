package edu.pitt.sis.exp.colfusion.servicemonitor;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

/**
 * @author Hao Bai
 *
 */
@XmlRootElement
public class ServiceMonitorViewModel {
	@Expose private int serviceId;
	@Expose private String serviceName;
	@Expose private String serviceAddress;
	@Expose private int portNumber;
	@Expose private String serviceDir;
	@Expose private String serviceCommand;
	@Expose private String serviceStatus;
	
	public int getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getServiceAddress() {
		return serviceAddress;
	}
	
	public void setServiceAddress(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	
	public int getPortNumber() {
		return portNumber;
	}
	
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	
	public String getServiceDir() {
		return serviceDir;
	}
	
	public void setServiceDir(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	
	public String getServiceCommand() {
		return serviceCommand;
	}
	
	public void setServiceCommand(String serviceCommand) {
		this.serviceCommand = serviceCommand;
	}
	
	public String getServiceStatus() {
		return serviceStatus;
	}
	
	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
}
