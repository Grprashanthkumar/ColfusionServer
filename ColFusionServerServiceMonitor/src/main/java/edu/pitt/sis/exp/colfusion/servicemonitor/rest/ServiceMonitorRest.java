package edu.pitt.sis.exp.colfusion.servicemonitor.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;
import edu.pitt.sis.exp.colfusion.servicemonitor.ServiceMonitor;

/**
 * @author Hao Bai
 *
 */
public interface ServiceMonitorRest {
	
	@Path("getServicesStatus")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServicesStatus() throws Exception;
	//function parameter: @Context ContextResolver<ServiceMonitor> serviceMonitor
	
	@Path("getServiceStatusByID/id={id}")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServiceStatusByID(@PathParam("id") String serviceID) throws Exception;
	
	@Path("getServiceStatusByNamePattern/namePattern={pattern}")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServiceStatusByNamePattern(@PathParam("pattern") String namePattern) throws Exception;
	
	@Path("addNewService")
	@GET
	//@POST
	//@Consumes("text/plain")
	//@Produces("text/plain")
	//@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response addNewService(ColfusionServices newService) throws Exception;

	@Path("updateServiceByID/id={id}")
	@GET
	//@PUT
	//@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response updateServiceByID(@PathParam("id") String serviceID) throws Exception;
	
	@Path("deleteServiceByID/id={id}")
	@GET
	//@DELETE
	//@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response deleteServiceByID(@PathParam("id") String serviceID) throws Exception;
	
}
