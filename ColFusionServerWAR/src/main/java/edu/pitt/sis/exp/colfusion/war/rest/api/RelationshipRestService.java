/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest.api;

import java.math.BigDecimal;

import javax.ws.rs.GET;
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
import edu.pitt.sis.exp.colfusion.responseModels.RelationshipLinksResponse;

/**
 * @author Evgeny
 *
 */
@Path("Relationship/")
public class RelationshipRestService {
	
	final Logger logger = LogManager.getLogger(RelationshipRestService.class.getName());
	

	
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
			logger.error(String.format("triggerDataMatchingRatiosCalculationsForAllNotCalculated FAILED for sid %d", sid), e);
			
			result.setMessage("Something went wrong. " + e.getMessage());
			result.setSuccessful(false);
		}
    	
    	return Response.status(200).entity(result).build(); //.build();
    }
	

	
	/**
	 * Starts background process to mine for relationships for a given story by sid.
	 * If the mining is in progress, then new process will not be started and the response with status set as running will be returned.
	 * 
	 * @param sid is the id of the story for which to perform mining.
	 * @return response mining status.
	 */
	@Path("{relId}/dataMatchingRatios/{similarityThreshold}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response dataMatchingRatios(@PathParam("relId") final int relId,
    		@PathParam("similarityThreshold") final BigDecimal similarityThreshold) {
    	
		RelationshipBL relationshipBL = new RelationshipBL();
		
		RelationshipLinksResponse result = new RelationshipLinksResponse();
		try {
			result = relationshipBL.getDataMatchingRatios(relId, similarityThreshold);
		} catch (Exception e) {
			logger.error(String.format("dataMatchingRatios FAILED for relId %d and similarity threshold %s", relId, similarityThreshold.toPlainString()), e);
			
			result.message = "Something went wrong. " + e.getMessage();
			result.isSuccessful = false;
		}
    	
    	return Response.status(200).entity(result).build(); //.build();
    }
}
