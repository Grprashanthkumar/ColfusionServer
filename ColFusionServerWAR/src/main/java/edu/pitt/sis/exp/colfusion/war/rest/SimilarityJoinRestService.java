package edu.pitt.sis.exp.colfusion.war.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.TwoJointTablesViewModel;

public interface SimilarityJoinRestService {

	@OPTIONS
    @Path("join")
    public abstract Response join(@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Join two tables with similarity threshold.
	 * 
	 * @return response with story metadata in the payload.
	 */
	@Path("join")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response join(TwoJointTablesViewModel joinTablesInfo);

	@OPTIONS
    @Path("joinByRelationships")
    public abstract Response joinByRelationships(@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Join two tables with similarity threshold.
	 * 
	 * @return response with story metadata in the payload.
	 */
	@Path("joinByRelationships")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response joinByRelationships(
			JoinTablesByRelationshipsViewModel joinTablesByRelationshipInfo);

}