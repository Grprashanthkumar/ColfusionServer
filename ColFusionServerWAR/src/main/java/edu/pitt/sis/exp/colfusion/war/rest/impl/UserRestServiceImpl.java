/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.UserBL;
import edu.pitt.sis.exp.colfusion.responseModels.AuthorsResponse;
import edu.pitt.sis.exp.colfusion.war.rest.UserRestService;

/**
 * @author Evgeny
 *
 */
@Path("User/")
public class UserRestServiceImpl extends BaseController implements UserRestService {

	final Logger logger = LogManager.getLogger(UserRestServiceImpl.class.getName());
	
	@Override
	public Response userLookUp(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response userLookUp(final String searchTerm, final int limit) {
    	
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
