package edu.pitt.sis.exp.colfusion.war.rest;

import java.math.BigDecimal;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface RelationshipRestService {

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("triggerDataMatching/{sid}/{similarityThreshold}")
    public abstract Response triggerDataMatchingRatiosCalculationsForAllNotCalculated(
			@HeaderParam("Access-Control-Request-Headers") String requestH);

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
    public abstract Response triggerDataMatchingRatiosCalculationsForAllNotCalculated(
    		@PathParam("sid") int sid, @PathParam("similarityThreshold") BigDecimal similarityThreshold);

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("{relId}/dataMatchingRatios/{similarityThreshold}")
    public abstract Response dataMatchingRatios(@HeaderParam("Access-Control-Request-Headers") String requestH);

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
    public abstract Response dataMatchingRatios(@PathParam("relId") int relId,
    		@PathParam("similarityThreshold") BigDecimal similarityThreshold);

}