/**
 * 
 */
package edu.pitt.sis.exp.colfusion.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.pitt.sis.exp.colfusion.bll.StoryBL;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataResponse;

/**
 * @author Evgeny
 *
 */
@Path("Story/")
public class StoryController extends BaseController {
	
	final Logger logger = LogManager.getLogger(StoryController.class.getName());
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("metadata/{sid}")
    public Response getStoryMetadata(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Finds metadata for the story with provided sid.
     * 
     * @param sid story id for which the metadata should be fetched.
     * @return response with story metadata in the payload.
     */
	@Path("metadata/{sid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStoryMetadata(@PathParam("sid") int sid) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataResponse result = storyBL.getStoryMetadata(sid);
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}
