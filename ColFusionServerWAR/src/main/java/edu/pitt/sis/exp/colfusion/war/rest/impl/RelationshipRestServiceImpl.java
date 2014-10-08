/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest.impl;

import java.math.BigDecimal;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.RelationshipBL;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;
import edu.pitt.sis.exp.colfusion.responseModels.RelationshipLinksResponse;
import edu.pitt.sis.exp.colfusion.war.rest.RelationshipRestService;

/**
 * @author Evgeny
 *
 */
@Path("Relationship/")
public class RelationshipRestServiceImpl extends BaseController implements RelationshipRestService {
	
	final Logger logger = LogManager.getLogger(RelationshipRestServiceImpl.class.getName());
	
	@Override
	public Response triggerDataMatchingRatiosCalculationsForAllNotCalculated(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response triggerDataMatchingRatiosCalculationsForAllNotCalculated(final int sid, final BigDecimal similarityThreshold) {
    	
		RelationshipBL relationshipBL = new RelationshipBL();
		
		GeneralResponse result = new GeneralResponseGenImpl<>();
		try {
			result = relationshipBL.triggerDataMatchingRatiosCalculationsForAllNotCalculatedBySid(sid, similarityThreshold);
		} catch (Exception e) {
			logger.error(String.format("triggerDataMatchingRatiosCalculationsForAllNotCalculated FAILED for sid %d", sid), e);
			
			result.setMessage("Something went wrong. " + e.getMessage());
			result.setSuccessful(false);
		}
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response dataMatchingRatios(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response dataMatchingRatios(final int relId, final BigDecimal similarityThreshold) {
    	
		RelationshipBL relationshipBL = new RelationshipBL();
		
		RelationshipLinksResponse result = new RelationshipLinksResponse();
		try {
			result = relationshipBL.getDataMatchingRatios(relId, similarityThreshold);
		} catch (Exception e) {
			logger.error(String.format("dataMatchingRatios FAILED for relId %d and similarity threshold %s", relId, similarityThreshold.toPlainString()), e);
			
			result.message = "Something went wrong. " + e.getMessage();
			result.isSuccessful = false;
		}
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}
