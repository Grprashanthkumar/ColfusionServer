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
import edu.pitt.sis.exp.colfusion.servicemonitor.ColfusionServicesViewModel;
import edu.pitt.sis.exp.colfusion.servicemonitor.ServiceMonitor;

/**
 * @author Hao Bai
 *
 */
public interface ServiceMonitorRest {
	
	/**
     * Find status of all services.
     * 
     * @return HTTP response.
     */
	@Path("getServicesStatus")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServicesStatus();
	//function parameter: @Context ContextResolver<ServiceMonitor> serviceMonitor
	
	/**
     * Find status of service by id.
     * 
     * @param id of service
     * @return HTTP response.
     */
	@Path("getServiceStatusByID/{id}")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServiceStatusByID(@PathParam("id") String serviceID);
	
	/**
     * Find status of service by its name pattern.
     * 
     * @param pattern of service's name
     * @return HTTP response.
     */
	@Path("getServiceStatusByNamePattern/{pattern}")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getServiceStatusByNamePattern(@PathParam("pattern") String namePattern);
	
	/**
     * add a new service.
     * 
     * @param entity of ColfusionServicesViewModel
     * @return HTTP response.
     */
	@Path("addNewService")
	@POST
	//@Consumes("text/plain")
	//@Produces("text/plain")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response addNewService(ColfusionServicesViewModel newService);

	/**
     * update a service.
     * 
     * @param id of the service
     * @param entity of ColfusionServicesViewModel
     * @return HTTP response.
     */
	@Path("updateServiceByID/{id}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response updateServiceByID(@PathParam("id") String serviceID, ColfusionServicesViewModel newService);
	
	/**
     * delete a service by id.
     * 
     * @param id of the service
     * @return HTTP response.
     */
	@Path("deleteServiceByID/{id}")
	@GET
	//@DELETE
	//@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response deleteServiceByID(@PathParam("id") String serviceID);
	
}
