package edu.pitt.sis.exp.colfusion.servicemonitor.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;
import edu.pitt.sis.exp.colfusion.servicemonitor.ColfusionServicesViewModel;
import edu.pitt.sis.exp.colfusion.servicemonitor.ServiceMonitorDaemon;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;
import edu.pitt.sis.exp.colfusion.utils.StringUtils;

/**
 * @author Hao Bai
 *
 */
@Path("ServiceMonitor/")
public class ServiceMonitorRestImpl implements ServiceMonitorRest{

	//@Context ServiceMonitor serviceMonitor;
	private static final Logger logger = LogManager.getLogger(ServiceMonitorRestImpl.class.getName());
	
	public ServiceMonitorRestImpl() {
	}  
	
	@Override
	public Response getServicesStatus() {
		
		//logger.info("Got request with this payload length: " + twoJointTables.length());

		List<ColfusionServices> resultList = new ArrayList<ColfusionServices>();
		String jsonResult = "";
		try {
			resultList = ServiceMonitorDaemon.getInstance().getServiceMonitor().getServicesStatus();
			jsonResult = Gsonizer.toJson(resultList, false);
			if (StringUtils.isNullOrEmpty(jsonResult)) {
				jsonResult = "[]";
			}
			return Response.status(200).entity(jsonResult).build();
		} 
		catch (Exception ex) {
			logger.error("In ServiceMonitorRestImpl.getServicesStatus()\n"
					+ ex.toString() + " " + ex.getCause());	
			return Response.status(500).entity(ex.getMessage()).build();
		}	
	}
	
	@Override
	public Response getServiceStatusByID(final String serviceID) {
		String result = "";
		try {
			int ID = Integer.parseInt(serviceID);
			ColfusionServices service = ServiceMonitorDaemon.getInstance().getServiceMonitor().getServiceStatusByID(ID);
			String serviceName = service.getServiceName();
			String serviceStatus = service.getServiceStatus();
			
			result = serviceName + " is " + serviceStatus;
			
			return Response.status(200).entity(result).build();
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitorRestImpl.getServiceStatusByID()\n"
					+ ex.toString() + " " + ex.getCause());	
			return Response.status(500).entity(result).build();
		}	
	}
	
	@Override
	public Response getServiceStatusByNamePattern(final String namePattern) {
		List<ColfusionServices> serviceList = null;
		String jsonResult = "";
		
		try {
			serviceList = ServiceMonitorDaemon.getInstance().getServiceMonitor().getServicesStatus();
			
			List<String> resultList = new ArrayList<String>();
			for(ColfusionServices service : serviceList) {
				if(service.getServiceName().indexOf(namePattern) >= 0) {
					resultList.add(service.getServiceName() + " is " + service.getServiceStatus());
				}
			}
			jsonResult = Gsonizer.toJson(resultList, false);
			return Response.status(200).entity(jsonResult).build();
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitorRestImpl.getServiceStatusByNamePattern()\n"
					+ ex.toString() + " " + ex.getCause());	
			return Response.status(500).entity(serviceList).build();
		}	
	}
	
	@Override
	public Response addNewService(final ColfusionServicesViewModel newService) {

		String result = "";
		try {
			ServiceMonitorDaemon.getInstance().getServiceMonitor().addNewService(newService);
			result = newService.getServiceName() + " is added.";
		
			return Response.status(201).entity(result).build();
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitorRestImpl.addNewService()\n"
					+ ex.toString() + " " + ex.getCause());	
			return Response.status(500).entity(result).build();
		}	
	}
	
	@Override
	public Response updateServiceByID(final String serviceID, final ColfusionServicesViewModel newService) {
		String result = "";
		try {
			int ID = Integer.parseInt(serviceID);
			ColfusionServices service = ServiceMonitorDaemon.getInstance().getServiceMonitor().getServiceStatusByID(ID);
		
			ServiceMonitorDaemon.getInstance().getServiceMonitor().updateServiceByID(ID, newService);
			result = service.getServiceName() + " is updated.";
		
			return Response.status(200).entity(result).build();
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitorRestImpl.updateServiceByID()\n"
					+ ex.toString() + " " + ex.getCause());	
			return Response.status(500).entity(result).build();
		}	
	}
	
	@Override
	public Response deleteServiceByID(final String serviceID) {
		String result = "";
		try {
			int ID = Integer.parseInt(serviceID);
			ColfusionServices service = ServiceMonitorDaemon.getInstance().getServiceMonitor().getServiceStatusByID(ID);
		
			ServiceMonitorDaemon.getInstance().getServiceMonitor().deleteServiceByID(ID);
			//TODO FIXME workaround
			result = String.format("{\"message\": \"%s is deleted\"}", service.getServiceName());
		
			return Response.status(200).entity(result).build();
		}
		catch (Exception ex) {
			logger.error("In ServiceMonitorRestImpl.deleteServiceByID()\n"
					+ ex.toString() + " " + ex.getCause());	
			return Response.status(500).entity(result).build();
		}	
	}
}
