/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.JoinTablesBL;
import edu.pitt.sis.exp.colfusion.bll.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.JointTableResponeModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;

/**
 * @author Evgeny
 *
 */
@Path("SimilarityJoin/")
public class SimilarityJoinRestService {
	final Logger logger = LogManager.getLogger(SimilarityJoinRestService.class.getName());
	

	
	/**
	 * Join two tables with similarity threshold.
	 * 
	 * @return response with story metadata in the payload.
	 */
	@Path("join")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response join(final TwoJointTablesViewModel joinTablesInfo) {
    	
		JoinTablesBL joinBL = new JoinTablesBL();
		
		JointTableResponeModel result = joinBL.joinTables(joinTablesInfo);
		 
		String json = result.toJson();
    	return Response.status(200).entity(json).build(); //.build();
    }
	

	
	/**
	 * Join two tables with similarity threshold.
	 * 
	 * @return response with story metadata in the payload.
	 */
	@Path("joinByRelationships")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response joinByRelationships(final JoinTablesByRelationshipsViewModel joinTablesByRelationshipInfo) {
    	
		JoinTablesBL joinBL = new JoinTablesBL();
		
		JointTableByRelationshipsResponeModel result = joinBL.joinTablesByRelationships(joinTablesByRelationshipInfo);
		 
		String json = result.toJson();
    	return Response.status(200).entity(json).build(); //.build();
    }
}
