package edu.pitt.sis.exp.colfusion.war.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import edu.pitt.sis.exp.colfusion.bll.SearchBL;
import edu.pitt.sis.exp.colfusion.bll.responseModels.FacetedSearchResponseModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.LocationListResponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.RowsResponseModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.StoryTableResponse;



@Path("Search/")
public class SearchRestService {

	
	/**
	 * Finds datasets, according to faceted conditions
	 * 
	 * @param title
	 * @return response with data sets list in the payload.
	 */
	@Path("Faceted/title/{title}")
    @GET
    @ApiOperation(
    		value = "Finds datasets, according to faceted conditions",
    		notes = "",
    		response = FacetedSearchResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "data sets List not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getFacetedSearch(@ApiParam(value = "title", required = true) @PathParam("title") final String title) {
		SearchBL searchBL = new SearchBL();
		LocationListResponse result = searchBL.getFacetedList(title);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
	}
	
	
	/**
	 * Finds dataset details, according to faceted conditions
	 * 
	 * @param sid
	 * @return response with dataset details the payload.
	 */
	@Path("Faceted/location/sid/{sid}")
    @GET
    @ApiOperation(
    		value = "Finds location rows details, according to sid",
    		notes = "",
    		response = FacetedSearchResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "dataset not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getFacetedSearchLocation(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid) {
		SearchBL searchBL = new SearchBL();
		RowsResponseModel result = searchBL.getLocationRows(sid);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
	}
	
	/**
	 * Finds dataset table
	 * 
	 * @param sid
	 * @return response with dataset table the payload.
	 */
	@Path("Faceted/dataset/{sid}")
    @GET
    @ApiOperation(
    		value = "Finds dataset table, according to sid",
    		notes = "",
    		response = FacetedSearchResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "table not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getStoryRows(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid) {
		SearchBL searchBL = new SearchBL();
		StoryTableResponse result = searchBL.getStoryRowsBySid(sid);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
	}
	
	
	/**
	 * Finds location index table
	 * 
	 * @return response with location list.
	 */
	@Path("Faceted/locationList")
    @GET
    @ApiOperation(
    		value = "Finds dataset table, according to sid",
    		notes = "",
    		response = FacetedSearchResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "location index not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllLocationList() {
		SearchBL searchBL = new SearchBL();
		LocationListResponse result = searchBL.getLocationList();
		String json = result.toJson();
		return Response.status(200).entity(json).build();
	}
}
