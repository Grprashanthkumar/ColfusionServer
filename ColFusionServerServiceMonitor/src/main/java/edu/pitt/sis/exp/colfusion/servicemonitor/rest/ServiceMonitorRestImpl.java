package edu.pitt.sis.exp.colfusion.servicemonitor.rest;

import java.util.List;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;
import edu.pitt.sis.exp.colfusion.servicemonitor.ServiceMonitor;

/**
 * @author Hao Bai
 *
 */
@Path("ServiceMonitor/")
public class ServiceMonitorRestImpl implements ServiceMonitorRest{

	//private static final Logger logger = LogManager.getLogger(ServiceMonitorRestImpl.class.getName());
	private ServiceMonitor serviceMonitor;
	
	public ServiceMonitorRestImpl() {
		serviceMonitor = new ServiceMonitor();
	}
	
	@Override
	public Response getServicesStatus() throws Exception {
		
		//logger.info("Got request with this payload length: " + twoJointTables.length());
		List<ColfusionServices> resutlList = serviceMonitor.getServicesStatus();
		
		return Response.status(200).entity(resutlList).build();
	}
	
	@Override
	public Response getServiceStatusByID(int serviceID) throws Exception {
		
		String resultStatus = serviceMonitor.getServiceStatusByID(serviceID);
		
		return Response.status(200).entity(resultStatus).build();
	}
	
	@Override
	public Response getServiceStatusByNamePattern(String namePattern) throws Exception {
		
		List<ColfusionServices> resutlList = serviceMonitor.getServiceStatusByNamePattern(namePattern);
		
		return Response.status(200).entity(resutlList).build();
	}
	
	@Override
	public Response addNewService(ColfusionServices newService) throws Exception {
		
		boolean result = serviceMonitor.addNewService(newService);
		
		return Response.status(201).entity(result).build();
	}
	
	@Override
	public Response updateServiceByID(int serviceID) throws Exception {
		
		boolean result = serviceMonitor.updateServiceByID(serviceID);
		
		return Response.status(200).entity(result).build();
	}
	
	@Override
	public Response deleteServiceByID(int serviceID) throws Exception {
		
		boolean result = serviceMonitor.deleteServiceByID(serviceID);
		
		return Response.status(200).entity(result).build();
	}
}
