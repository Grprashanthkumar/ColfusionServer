/**
 * 
 */
package edu.pitt.sis.exp.colfusion.controllers;

import java.math.BigDecimal;

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

import edu.pitt.sis.exp.colfusion.bll.RelationshipBL;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;

/**
 * @author Evgeny
 *
 */
@Path("Relationship/")
public class RelationshipController extends BaseController {
	
	final Logger logger = LogManager.getLogger(RelationshipController.class.getName());
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("triggerDataMatching/{sid}/{similarityThreshold}")
    public Response triggerDataMatchingRatiosCalculationsForAllNotCalculated(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Starts background process to mine for relationships for a given story by sid.
     * If the mining is in progress, then new process will not be started and the response with status set as running will be returned.
     * 
     * @param sid is the id of the story for which to perform mining.
     * @return response mining status.
     */
	@Path("triggerDataMatching/{sid}/{similarityThreshold}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response triggerDataMatchingRatiosCalculationsForAllNotCalculated(@PathParam("sid") final int sid, @PathParam("similarityThreshold") final BigDecimal similarityThreshold) {
    	
		RelationshipBL relationshipBL = new RelationshipBL();
		
		GeneralResponse result = new GeneralResponseGenImpl<>();
		try {
			result = relationshipBL.triggerDataMatchingRatiosCalculationsForAllNotCalculatedBySid(sid, similarityThreshold);
		} catch (Exception e) {
			logger.error(String.format("triggerDataMatchingRatiosCalculationsForAllNotCalculated FAILED for sid %d", sid));
			
			result.setMessage("Something went wrong. " + e.getMessage());
			result.setSuccessful(false);
		}
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}
