package edu.pitt.sis.exp.colfusion.servicemonitor.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ContextResolver;

import edu.pitt.sis.exp.colfusion.dal.managers.ServiceManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ServiceManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;
import edu.pitt.sis.exp.colfusion.servicemonitor.ServiceMonitor;
import edu.pitt.sis.exp.colfusion.servicemonitor.ServiceMonitorDeamon;

/**
 * @author Hao Bai
 *
 */
@Path("ServiceMonitor/")
public class ServiceMonitorRestImpl implements ServiceMonitorRest{

	//@Context ServiceMonitor serviceMonitor;
	//private static final Logger logger = LogManager.getLogger(ServiceMonitorRestImpl.class.getName());
	
	public ServiceMonitorRestImpl() {
	}
	
	@Override
	public Response getServicesStatus() throws Exception {
		
		//logger.info("Got request with this payload length: " + twoJointTables.length());
		//List<ColfusionServices> resutlList = ServiceMonitor.getServicesStatus();
		//String result = serviceManager.findAll().toString();
		//String result = serviceMonitor.getContext(ServiceMonitorRestImpl.class).getServicesStatus().toString();
		String result = ServiceMonitorDeamon.getServiceMonitorInstance().getServicesStatus().toString();
		
		return Response.status(200).entity(result).build();
	}
	
	@Override
	public Response getServiceStatusByID(String serviceID) throws Exception {
		int ID = Integer.parseInt(serviceID);
		ColfusionServices service = ServiceMonitorDeamon.getServiceMonitorInstance().getServiceStatusByID(ID);
		String serviceName = service.getServiceName();
		String serviceStatus = service.getServiceStatus();
		
		String result = serviceName + " is " + serviceStatus;
		
		return Response.status(200).entity(result).build();
	}
	
	@Override
	public Response getServiceStatusByNamePattern(String namePattern) throws Exception {
		
		List<ColfusionServices> serviceList = ServiceMonitorDeamon.getServiceMonitorInstance().getServicesStatus();
		
		List<String> resultList = new ArrayList<String>();
		for(ColfusionServices service : serviceList) {
			if(service.getServiceName().indexOf(namePattern) >= 0) {
				resultList.add(service.getServiceName() + " is " + service.getServiceStatus());
			}
		}
		
		return Response.status(200).entity(resultList.toString()).build();
	}
	
	@Override
	public Response addNewService(ColfusionServices newService) throws Exception {
		
		ServiceMonitorDeamon.getServiceMonitorInstance().addNewService(newService);
		String result = newService.getServiceName() + " is added.";
		
		return Response.status(201).entity(result).build();
	}
	
	@Override
	public Response updateServiceByID(String serviceID) throws Exception {
		
		int ID = Integer.parseInt(serviceID);
		ColfusionServices service = ServiceMonitorDeamon.getServiceMonitorInstance().getServiceStatusByID(ID);
		
		ServiceMonitorDeamon.getServiceMonitorInstance().updateServiceByID(ID);
		String result = service.getServiceName() + " is updated.";
		
		return Response.status(200).entity(result).build();
	}
	
	@Override
	public Response deleteServiceByID(String serviceID) throws Exception {
		
		int ID = Integer.parseInt(serviceID);
		ColfusionServices service = ServiceMonitorDeamon.getServiceMonitorInstance().getServiceStatusByID(ID);
		
		ServiceMonitorDeamon.getServiceMonitorInstance().deleteServiceByID(ID);
		String result = service.getServiceName() + " is deleted.";
		
		return Response.status(200).entity(result).build();
	}
}
