/**
 * 
 */
package edu.pitt.sis.exp.colfusion.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.UserBL;
import edu.pitt.sis.exp.colfusion.responseModels.AuthorsResponse;

/**
 * @author Evgeny
 *
 */
@Path("User/")
public class UserController extends BaseController {

	final Logger logger = LogManager.getLogger(UserController.class.getName());
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("lookup")
    public Response userLookUpOption(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Creates new story in the database.
     * 
     * @param userId is the id of the authors of the story.
     * @return response with story metadata in the payload.
     */
	@Path("lookup")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response userLookUp(@QueryParam("searchTerm") String searchTerm, @QueryParam("limit") int limit) {
    	
		AuthorsResponse result = new AuthorsResponse();
		
		try {
			
			UserBL userBL = new UserBL();
			
			result = userBL.lookUpAuthors(searchTerm, limit);
			
		} catch (Exception e) {
			result.isSuccessful = false;
			result.message = "Could not perform look up.";
			logger.error("userLookUp failed", e);
		}
		    	
		return makeCORS(Response.status(200).entity(result)); //.build();
    }
}
