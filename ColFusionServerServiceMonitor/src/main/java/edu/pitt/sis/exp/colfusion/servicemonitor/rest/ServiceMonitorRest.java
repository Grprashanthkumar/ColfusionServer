package edu.pitt.sis.exp.colfusion.servicemonitor.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;

/**
 * @author Hao Bai
 *
 */
public interface ServiceMonitorRest {
	
	@Path("getServicesStatus")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServicesStatus() throws Exception;
	
	@Path("getServiceStatusByID")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServiceStatusByID(int serviceID) throws Exception;
	
	@Path("getServiceStatusByNamePattern")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServiceStatusByNamePattern(String namePattern) throws Exception;
	
	@Path("addNewService")
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response addNewService(ColfusionServices newService) throws Exception;

	@Path("updateServiceByID")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response updateServiceByID(int serviceID) throws Exception;
	
	@Path("deleteServiceByID")
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response deleteServiceByID(int serviceID) throws Exception;
	
}
