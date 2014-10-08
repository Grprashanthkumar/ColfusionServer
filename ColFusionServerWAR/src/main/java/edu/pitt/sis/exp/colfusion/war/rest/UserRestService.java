package edu.pitt.sis.exp.colfusion.war.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface UserRestService {

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("lookup")
    public abstract Response userLookUp(@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Creates new story in the database.
	 * 
	 * @param userId is the id of the authors of the story.
	 * @return response with story metadata in the payload.
	 */
	@Path("lookup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response userLookUp(@QueryParam("searchTerm") String searchTerm, @QueryParam("limit") int limit);

}