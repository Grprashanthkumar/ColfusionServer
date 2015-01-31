package edu.pitt.sis.exp.colfusion.psc.server.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.exp.colfusion.bll.JoinTablesBL;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableResponeModel;

@Path("JoinTablesLocal")
public class JoinTablesLocal {

	@Path("join/{sid1}/{tableName1}/{sid2}/{tableName2}/{similarityThreshold}")
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response joinTablesLocal(@PathParam("sid1") final int sid1, @PathParam("tableName1") final String tableName1, 
			@PathParam("sid2") final int sid2, @PathParam("tableName2") final String tableName2, @PathParam("similarityThreshold") final double similarityThreshold) throws Exception {
		
		JoinTablesBL joinBL = new JoinTablesBL();
		
		TwoJointTablesViewModel joinTablesInfo = new TwoJointTablesViewModel(sid1, tableName1, sid2, tableName2, similarityThreshold, null);
		
		JointTableResponeModel result = joinBL.joinTables(joinTablesInfo);
		 
		String json = result.toJson();
    	return Response.status(200).entity(json).build(); //.build();
	}
}
