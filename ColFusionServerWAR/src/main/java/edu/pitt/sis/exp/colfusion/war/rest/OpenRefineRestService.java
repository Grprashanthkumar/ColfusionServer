package edu.pitt.sis.exp.colfusion.war.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface OpenRefineRestService {

	@OPTIONS
    @Path("createNewProject/{sid}/{tableName}")
    public Response createNewProject(@HeaderParam("Access-Control-Request-Headers") String requestH);

	@Path("createNewProject/{sid}/{tableName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)    
	public Response createNewProject(@PathParam("sid") int sid, @PathParam("tableName") String tableName)
			throws ClassNotFoundException, Exception;

	@OPTIONS
    @Path("saveChanges/{openRefinProjectId}/{colfusionUserId}")
    public Response saveChanges(@HeaderParam("Access-Control-Request-Headers") String requestH);

	@Path("saveChanges/{openRefinProjectId}/{colfusionUserId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveChanges(@PathParam("openRefinProjectId") String openRefinProjectId,
    		@PathParam("colfusionUserId") String colfusionUserId) throws ClassNotFoundException, Exception;

}