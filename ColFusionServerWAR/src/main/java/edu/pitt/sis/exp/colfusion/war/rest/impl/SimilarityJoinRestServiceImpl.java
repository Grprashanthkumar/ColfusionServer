/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.JoinTablesBL;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableResponeModel;
import edu.pitt.sis.exp.colfusion.war.rest.SimilarityJoinRestService;

/**
 * @author Evgeny
 *
 */
@Path("SimilarityJoin/")
public class SimilarityJoinRestServiceImpl extends BaseController implements SimilarityJoinRestService {
	final Logger logger = LogManager.getLogger(SimilarityJoinRestServiceImpl.class.getName());
	
	@Override
	public Response join(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response join(final TwoJointTablesViewModel joinTablesInfo) {
    	
		JoinTablesBL joinBL = new JoinTablesBL();
		
		JointTableResponeModel result = joinBL.joinTables(joinTablesInfo);
		 
		String json = result.toJson();
    	return makeCORS(Response.status(200).entity(json)); //.build();
    }
	
	@Override
	public Response joinByRelationships(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response joinByRelationships(final JoinTablesByRelationshipsViewModel joinTablesByRelationshipInfo) {
    	
		JoinTablesBL joinBL = new JoinTablesBL();
		
		JointTableByRelationshipsResponeModel result = joinBL.joinTablesByRelationships(joinTablesByRelationshipInfo);
		 
		String json = result.toJson();
    	return makeCORS(Response.status(200).entity(json)); //.build();
    }
}
