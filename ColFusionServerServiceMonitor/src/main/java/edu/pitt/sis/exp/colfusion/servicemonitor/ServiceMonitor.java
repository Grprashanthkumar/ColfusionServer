package edu.pitt.sis.exp.colfusion.servicemonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import edu.pitt.sis.exp.colfusion.dal.managers.ServiceManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ServiceManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManager;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;
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
	
	private List<ColfusionServices> serviceList;
	private int timeOut;
	ServiceManager serviceManager;
	UserManager userManager;
	EmailNotifier emailNotifier;
	private int monitorPeriod;
	Timer timer;
	
	private final Logger logger = LogManager.getLogger(ServiceMonitor.class.getName());
	
	/**
	 * Set the default time out as 3 seconds.
	 */
	public ServiceMonitor(){
		timer = new Timer();
		this.serviceList = new ArrayList<ColfusionServices>();
		this.timeOut = Integer.parseInt(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_SERVICE_MONITOR_TIMEOUT));;
		this.monitorPeriod = Integer.parseInt(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_SERVICE_MONITOR_PERIOD));;

		serviceManager = new ServiceManagerImpl();
		userManager = new UserManagerImpl();
		emailNotifier = new EmailNotifier();
	}
	
	/**
	 * @param service list
	 * @param timeout
	 * @param period
	 * @param timer parameter
	 */
	public ServiceMonitor(final List<ColfusionServices> servicelist, final int timeout, final int period, final Timer timerParameter){
		this.serviceList = servicelist;
		this.timeOut = timeout;
		this.monitorPeriod = period;
		this.timer = timerParameter;
	}
	
	/**
	 * set service list
	 * 
	 * @param list of entity ColfusionServices
	 */
	public void setServiceList(final List<ColfusionServices> servicelist){
		this.serviceList = servicelist;
	}
	
	/**
	 * get service list
	 * 
	 * @return list of entity ColfusionServices
	 */
	public List<ColfusionServices> getServiceList(){
		return this.serviceList;
	}
	
	
	/**
	 * set time out
	 * 
	 * @param timeout
	 */
	public void setTimeOut(final int timeout){
		this.timeOut = timeout;
	}
	
	/**
	 * get time out
	 * 
	 * @return timeout
	 */
	public int getTimeOut(){
		return this.timeOut;
	}
	
	
	/**
	 * set monitor period 
	 * 
	 * @param period
	 */
	public void setMonitorPeriod(final int period){
		this.monitorPeriod = period;
	}
	
	/**
	 * get monitor period 
	 * 
	 * @return period
	 */
	public int getMonitorPeriod(){
		return this.monitorPeriod;
	}
	
	/**
	 * get service num in database
	 * 
	 * @return number of all services
	 * @throws Exception
	 */
	public int getServiceNumInDatabase() throws Exception{
		return this.serviceManager.findAll().size();
	}
	
	/**
	 * This function returns all services's status 
	 * with other information in a Service list.
	 * Note: Either the name or the function of this function
	 * is different from getServiceStatus() in Service.java
	 * 
	 * @return list of entity ColfusionServices
	 * @throws Exception
	 */
	public List<ColfusionServicesViewModel> getServicesStatus() throws Exception{
		if(this.getServiceList().isEmpty() == true) {
			return null;
		}
		List<ColfusionServicesViewModel> serviceViewModelList = new ArrayList<ColfusionServicesViewModel>();
		ColfusionServicesViewModel serviceViewModel;
		for(ColfusionServices service : this.getServiceList()) {
			serviceViewModel = new ColfusionServicesViewModel();
			serviceViewModel.setServiceID(service.getServiceID());
			serviceViewModel.setServiceName(service.getServiceName());
			serviceViewModel.setServiceAddress(service.getServiceAddress());
			serviceViewModel.setPortNumber(service.getPortNumber());
			serviceViewModel.setServiceDir(service.getServiceDir());
			serviceViewModel.setServiceCommand(service.getServiceCommand());
			serviceViewModel.setServiceStatus(service.getServiceStatus());
			serviceViewModelList.add(serviceViewModel);
		}
		//return this.getServiceList();
		return serviceViewModelList;
	}
	
	
	/**
	 * get service status by id
	 * 
	 * @param serviceID
	 * @return entity of ColfusionServices
	 */
	public ColfusionServicesViewModel getServiceStatusByID(final int serviceID) {
		ColfusionServices service = null;
		ColfusionServicesViewModel serviceViewModel = new ColfusionServicesViewModel();
		try {
			service = this.serviceManager.findByID(serviceID);
			serviceViewModel = this.convertToViewModel(service);
		} 
		catch (Exception ex) {
			logger.error("In ServiceMonitor.getServiceStatusByID()\n"
					+ ex.toString() + " " + ex.getCause());	
		}
		return serviceViewModel;
	}
	
	/**
	 * get service status by name pattern
	 * 
	 * @param namePattern
	 * @return entity of ColfusionServices
	 */
	public List<ColfusionServices> getServiceStatusByNamePattern(final String namePattern) {
		List<ColfusionServices> resultServiceList = new ArrayList<ColfusionServices>();
		List<ColfusionServices> tempServiceList;
		
		String specialCharRegExp = namePattern;
		Pattern pattern = Pattern.compile(specialCharRegExp);
        Matcher matcher;
        
		try {
			tempServiceList = this.serviceManager.findAll();
			for(ColfusionServices service : tempServiceList) {
				matcher = pattern.matcher(service.getServiceName());
				if(matcher.find()) {
					resultServiceList.add(service);
				}
			}
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitor.getServiceStatusByNamePattern()\n"
					+ ex.toString() + " " + ex.getCause());	
		}
		return resultServiceList;	
	}
	
	/**
	 * add New Service
	 * 
	 * @param entity of ColfusionServicesViewModel
	 * @return boolean result
	 */
	public ColfusionServicesViewModel addNewService(final ColfusionServicesViewModel viewModel) {
		try {
			ColfusionServices newService = this.convertViewModel(viewModel);
			ColfusionServicesViewModel newServiceViewModel = null;
			if( newService != null) {
				this.serviceList.add(newService);
				this.serviceManager.save(newService);
				newServiceViewModel = this.convertToViewModel(newService);
				return newServiceViewModel;
			}
			return null;
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitor.addNewService()\n"
					+ ex.toString() + " " + ex.getCause());	
			return null;
		}
	}
	
	/**
	 * update Service By ID
	 * 
	 * @param serviceID
	 * @param entity of ColfusionServicesViewModel
	 * @return boolean result
	 */
	public boolean updateServiceByID(final int serviceID, final ColfusionServicesViewModel viewModel) {
		try {
			ColfusionServices newService = this.convertViewModel(viewModel);
			
			for(ColfusionServices service : this.serviceList) {
				if(service.getServiceID() == serviceID) {
					newService.setServiceID(serviceID);
					this.serviceList.set(this.serviceList.indexOf(service), newService);
					this.serviceManager.saveOrUpdate(newService);
					return true;
				}
			}
			return false;
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitor.updateServiceByID()\n"
					+ ex.toString() + " " + ex.getCause());	
			return false;
		}
	}
	
	/**
	 * delete Service By ID
	 * 
	 * @param serviceID
	 * @return boolean result
	 */
	public boolean deleteServiceByID(final int serviceID) {
		try {
			for(ColfusionServices service : this.serviceList) {
				if(service.getServiceID() == serviceID) {
					this.serviceList.remove(this.serviceList.indexOf(service));
					this.serviceManager.delete(service);
					return true;
				}
			}
			return false;
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitor.deleteServiceByID()\n"
					+ ex.toString() + " " + ex.getCause());	
			return false;
		}
	}
	
	/**
	 * convert entity of ColfusionServicesViewModel to entity of ColfusionServices
	 * 
	 * @param entity of ColfusionServicesViewModel
	 * @return entity of ColfusionServices
	 */
	private ColfusionServices convertViewModel(final ColfusionServicesViewModel viewModel) {
		ColfusionServices service = new ColfusionServices();
		
		if (viewModel.getServiceID() > 0) {
			service.setServiceID(viewModel.getServiceID());
		}
		
		service.setServiceName(viewModel.getServiceName());
		service.setServiceAddress(viewModel.getServiceAddress());
		service.setPortNumber(viewModel.getPortNumber());
		service.setServiceDir(viewModel.getServiceDir());
		service.setServiceCommand(viewModel.getServiceCommand());
		service.setServiceStatus(viewModel.getServiceStatus());
		
		return service;
	}
	
	/**
	 * convert entity of ColfusionServices to entity of ColfusionServicesViewModel
	 * 
	 * @param entity of ColfusionServices
	 * @return entity of ColfusionServicesViewModel
	 */
	private ColfusionServicesViewModel convertToViewModel(ColfusionServices service) {
		ColfusionServicesViewModel serviceViewModel = new ColfusionServicesViewModel();
		
		serviceViewModel.setServiceID(service.getServiceID());
		serviceViewModel.setServiceName(service.getServiceName());
		serviceViewModel.setServiceAddress(service.getServiceAddress());
		serviceViewModel.setPortNumber(service.getPortNumber());
		serviceViewModel.setServiceDir(service.getServiceDir());
		serviceViewModel.setServiceCommand(service.getServiceCommand());
		serviceViewModel.setServiceStatus(service.getServiceStatus());
		
		return serviceViewModel;
	}
	
	/**
	 * This function starts the process of service monitoring.
	 * If monitoring is started successfully, returns true.
	 * Otherwise, returns false.
	 * 
	 * @return boolean result
	 */
	public boolean startServiceMonitor(){
		try{
			/*executes task as schedule(task, initial delay, delay period)*/
			this.timer.schedule(this, 0, this.monitorPeriod);
			return true;
		}
		catch(Exception exception){
			logger.error("In ServiceMonitor.startServiceMonitor()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
			return false;
		}
	}
	
	/**
	 * This function stops the process of service monitoring.
	 * If monitoring is stopped successfully, returns true.
	 * Otherwise, returns false.
	 * 
	 * @return boolean result
	 */
	public boolean stopServiceMonitor(){
		if(this.timer == null) {
			return true;
		}
		try{
			//this.timer.cancel();
			this.cancel();
			return true;
		}
		catch(Exception exception){
			logger.error("In ServiceMonitor.stopServiceMonitor()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());		
			return false;
		}
	}
	
	/**
	 * Use TelnetClient class in commons-net.jar to test connection of 
	 * a remote/local service providing its IP and Port.
	 * Also set the timeout, with default 3 seconds.
	 * If the service is connected, returns true;
	 * Else, connect() reports connect exceptions, thus, returns false.
	 * 
	 * @param entity of ColfusionServices
	 * @return boolean result
	 */
	public boolean isServiceConnected(final ColfusionServices service){	
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
	 * 
	 * @param entity of ColfusionServices
	 * @return status of service
	 */
	public String updateServiceStatus(final ColfusionServices service){
		if(this.isServiceConnected(service) == true){
			service.setServiceStatus(ServiceStatusEnum.RUNNING.getValue());
		}
		else{
			service.setServiceStatus(ServiceStatusEnum.STOPPED.getValue());
		}
		return service.getServiceStatus();
	}
	
	/**
	 * Main function
	 * */
/*	public static void main(String[] args) {
		if(args.length <= 0) {
		//	System.out.println("The valid input should be: ServiceMonitor.java start/stop");
		//	System.exit(1);
		}
			
		ServiceMonitor serviceMonitor = new ServiceMonitor();
		
		//if(args[1].equals("start")) {
			serviceMonitor.startServiceMonitor();
			System.out.println("Services Monitoring process started!");
			
			//ServiceMonitorServer server = new ServiceMonitorServer(serviceMonitor, 0);
			ServiceMonitorServer server = new ServiceMonitorServer();
			server.run();
		//}
		//else if(args[1].equals("stop")) {
		//	serviceMonitor.stopServiceMonitor();
		//	System.out.println("Services Monitoring process stopped!");
		//}
	}*/
	
	/**
	 * This TimerTask run() function gets services' information
	 * from database, and monitor status of all the six services.
	 * If status of some service is changed, this function also 
	 * update the display information as well as the status data
	 * in database.
	 */
	@Override
	public void run(){
		String currentStatus = null;
		String emailSubject = null;
		String emailText = null;
		try{
			if(this.serviceList.isEmpty() == true) {
				this.serviceList = this.serviceManager.findAll();
			}
			for(ColfusionServices service : this.serviceList){
				currentStatus = this.updateServiceStatus(service);
				this.serviceManager.saveOrUpdate(service);
				this.serviceList.set(this.serviceList.indexOf(service), service);
				//Print out service's name and status
				System.out.println(service.getServiceName()+": "+service.getServiceStatus());
				
				if(currentStatus.equals(ServiceStatusEnum.STOPPED.getValue()) && 
				   currentStatus.equals(service.getServicePreviousStatus()) == false){
					emailSubject = "Service Status changed: " + service.getServiceName();
					emailText = String.format("Service has been stopped!\n"
							+ "  Service id: %d\n"
							+ "  Service Name: %s\n"
							+ "  Service Address: %s\n"
							+ "  Service Port#: %d\n"
							+ "  Service Dir: %s\n"
							+ "  Service Command: %s\n"
							+ "  Service Status: %s",
							service.getServiceID(),
							service.getServiceName(),
							service.getServiceAddress(),
							service.getPortNumber(),
							service.getServiceDir(),
							service.getServiceCommand(),
							service.getServiceStatus());
					for(String userLevel : ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_SERVICE_MONITOR_EMAIL_NOTIFICATION_SMTP_USER_LEVEL).split(",")){
						for(String emailAddress : this.userManager.queryUserEmails(userLevel)){
							emailNotifier.sendMail(emailAddress, emailSubject, emailText);
						}
					}
				}
				if(currentStatus.equals(service.getServicePreviousStatus()) == false) {
					service.setServicePreviousStatus(currentStatus);
				}
				currentStatus = null;
			}
		}
		catch(Exception exception){
			logger.error("In ServiceMonitor.run()\n"
					+ exception.toString() + " " + exception.getMessage() + " " + exception.getCause());
		}
	}
}
