package edu.pitt.sis.exp.colfusion.war.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import edu.pitt.sis.exp.colfusion.bll.BasicTableBL;
import edu.pitt.sis.exp.colfusion.responseModels.StoryListResponseModel;


@Api(value = "Test", description = "Operations on stories")
@Path("Test/")
public class TestResourceImpl extends BaseController {
	
	@Path("all/")
    @GET
    @ApiOperation(
    		value = "Finds story list.",
    		notes = "getAllStoryList note",
    		response = StoryListResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "StoryList not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAllStoryList() {
		BasicTableBL basicBL = new BasicTableBL();
		StoryListResponseModel result = basicBL.getAllStoryList();
		String json = result.toJson();
		return this.makeCORS(Response.status(200).entity(json));
	}
}
