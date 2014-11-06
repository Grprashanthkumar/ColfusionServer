/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import edu.pitt.sis.exp.colfusion.bll.BasicTableBL;
import edu.pitt.sis.exp.colfusion.bll.StoryBL;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.AddColumnMetadataEditHistoryResponse;
import edu.pitt.sis.exp.colfusion.responseModels.AttachmentListResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.BasicTableResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.ColumnMetadataResponse;
import edu.pitt.sis.exp.colfusion.responseModels.GetColumnMetadataEditHistoryResponse;
import edu.pitt.sis.exp.colfusion.responseModels.LicensesResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.RelationshipsResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.StoryListResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataHistoryResponse;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataResponse;
import edu.pitt.sis.exp.colfusion.responseModels.StoryStatusResponseModel;

/**
 * @author Evgeny
 *
 */
@Api(value = "/Story", description = "Operations on stories")
@Path("Story/")
public class StoryRestService extends BaseController  {
	
	final Logger logger = LogManager.getLogger(StoryRestService.class.getName());
	
	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
	public Response newStoryMetadata(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	/**
	 * Creates new story in the database.
	 * 
	 * @param userId is the id of the authors of the story.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/new/{userId}")
    @GET
    @ApiOperation(
    		value = "Creates new story in the database.",
    		notes = "newStoryMetadata note",
    		response = StoryMetadataResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid ID supplied"),
			@ApiResponse(code = 404, message = "StoryMetadata not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response newStoryMetadata(@ApiParam(value = "the id of the authors of story", required = true) @PathParam("userId") final int userId) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataResponse result = storyBL.createStory(userId);
    	
    	return this.makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@OPTIONS
    @Path("metadata/{sid: [0-9]+}")
	public Response getStoryMetadata(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	/**
	 * Finds metadata for the story with provided sid.
	 * 
	 * @param sid story id for which the metadata should be fetched.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/{sid: [0-9]+}")
    @GET
    @ApiOperation(
    		value = "Finds metadata for the story with provided sid.",
    		notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions",
    		response = StoryMetadataResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid ID supplied"),
			@ApiResponse(code = 404, message = "StoryMetadata not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getStoryMetadata(@ApiParam(value = "story id for which the metadata should be fetched", required = true) @PathParam("sid") final int sid) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataResponse result = storyBL.getStoryMetadata(sid);
    	
    	return this.makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	/**
	 * Updates story/dataset metadata.
	 * 
	 * @param sid story id for which the metadata should be fetched.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/{sid: [0-9]+}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response updateStoryMetadata(final StoryMetadataViewModel metadata) {
    	
		System.out.println("updateStoryMetadata function");
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataResponse result = storyBL.updateStoryMetadata(metadata);
    	
    	return this.makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@OPTIONS
    @Path("metadata/{sid: [0-9]+}/history/{historyItem}")
	public Response getStoryMetadataHistory(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	/**
	 * Finds metadata for the story with provided sid.
	 * 
	 * @param sid story id for which the metadata should be fetched.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/{sid: [0-9]+}/history/{historyItem}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getStoryMetadataHistory(@PathParam("sid") final int sid, @PathParam("historyItem") final String historyItem) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataHistoryResponse result = storyBL.getStoryMetadataHistory(sid, historyItem);
		
    	
    	return this.makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@OPTIONS
    @Path("{sid}/{tableName}/metadata/columns")
	public Response getColumnMetadata(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Path("{sid}/{tableName}/metadata/columns")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response getColumnMetadata(@PathParam("sid") final int sid, @PathParam("tableName") final String tableName){
		 StoryBL storyBL= new StoryBL();
		 
		 ColumnMetadataResponse result= storyBL.getColumnMetaData(sid, tableName);
		
		 return this.makeCORS(Response.status(200).entity(result));
	}
	
	@OPTIONS
    @Path("metadata/columns/addEditHistory/{cid}/{userid}/{editAttribute}/{reason}/{editValue}")
	public Response addColumnMetadataEditHistory(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Path("metadata/columns/addEditHistory/{cid}/{userid}/{editAttribute}/{reason}/{editValue}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response addColumnMetadataEditHistory(@PathParam("cid") final int cid, @PathParam("userid") final int userid,
			@PathParam("editAttribute") final String editAttribute, @PathParam("reason") final String reason, @PathParam("editValue") final String editValue){
		String changedreason = reason.replace("*!~~!*", "/");
		String changededitValue = editValue.replace("*!~~!*", "/");
		StoryBL storyBL = new StoryBL();
		AddColumnMetadataEditHistoryResponse result = storyBL.addColumnMetaEditHistory(cid,userid,editAttribute,changedreason,changededitValue);
		 return this.makeCORS(Response.status(200).entity(result));
	}
	
	@OPTIONS
    @Path("metadata/columns/getEditHistory/{cid}/{editAttribute}")
	public Response getColumnMetadataEditHistory(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Path("metadata/columns/getEditHistory/{cid}/{showAttribute}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response getColumnMetadataEditHistory(@PathParam("cid") final int cid,
			@PathParam("showAttribute") final String editAttribute){ 
		StoryBL storyBL = new StoryBL();
		GetColumnMetadataEditHistoryResponse result = storyBL.getColumnMetaEditHistory(cid,editAttribute);
		
	
		 return this.makeCORS(Response.status(200).entity(result));
	}
	
	@OPTIONS
    @Path("{sid}/{tableName}/tableInfo")
	public Response tableInfo(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	
	@Path("{sid}/{tableName}/tableInfo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response tableInfo(@PathParam("sid") final int sid, @PathParam("tableName") final String tableName) {
    	
		BasicTableBL basicBL=new BasicTableBL();
		BasicTableResponseModel result= basicBL.getTableInfo(sid, tableName);
		return this.makeCORS(Response.status(200).entity(result));
    }
	
	@OPTIONS
    @Path("{sid}/{tableName}/tableData/{perPage}/{pageNumber}")
	public Response getTableDataBySidAndName(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
		
	@Path("{sid}/{tableName}/tableData/{perPage}/{pageNumber}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getTableDataBySidAndName(@PathParam("sid") final int sid,
    		@PathParam("tableName") final String tableName, @PathParam("perPage") final int perPage, @PathParam("pageNumber") final int pageNumber) {
		
		BasicTableBL basicBL=new BasicTableBL();
		JointTableByRelationshipsResponeModel result = basicBL.getTableDataBySidAndName(sid, tableName, perPage, pageNumber);
		
		String json = result.toJson();
		
		return this.makeCORS(Response.status(200).entity(json));
    }
	
	@Path("metadata/license")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLicense(){
		StoryBL storyBL = new StoryBL();
		LicensesResponseModel  result = storyBL.getLicense();
		//System.out.println(result.getPayload().toString());
		return this.makeCORS(Response.status(200).entity(result));
	}
	
	@OPTIONS
    @Path("{sid}/StoryStatus")
	public Response getStoryStatus(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
	}

	@Path("{sid}/StoryStatus")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getStoryStatus(@PathParam("sid") final int sid) {
		BasicTableBL basicBL = new BasicTableBL();
		StoryStatusResponseModel result = basicBL.getStoryStatus(sid);
		String json = result.toJson();
		return this.makeCORS(Response.status(200).entity(json));
	}

	@OPTIONS
    @Path("{sid}/MineRelationships/{perPage}/{pageNumber}")
	public Response getMineRelationships(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
	}
	
	
	@Path("{sid}/MineRelationships/{perPage}/{pageNumber}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getMineRelationships(@PathParam("sid") final int sid, @PathParam("perPage") final int perPage, @PathParam("pageNumber") final int pageNumber) {
		BasicTableBL basicBL = new BasicTableBL();
		RelationshipsResponseModel result = basicBL.getRelationships(sid, perPage, pageNumber);
		String json = result.toJson();
		return this.makeCORS(Response.status(200).entity(json));
		
	}

	/**
	 * Finds attachment list
	 * 
	 * @param sid
	 * @return response with attachment list in the payload.
	 */
	@OPTIONS
    @Path("{sid}/AttachmentList")
	public Response getAttachmentList(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
	}

	@Path("{sid}/AttachmentList")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAttachmentList(@PathParam("sid") final int sid) {
		BasicTableBL basicBL = new BasicTableBL();
		AttachmentListResponseModel result = basicBL.getAttachmentList(sid);
		String json = result.toJson();
		return this.makeCORS(Response.status(200).entity(json));
	}

	/**
	 * Finds story list
	 * 
	 * @param pageNo, perPage
	 * @response MediaType.APPLICATION_JSON
	 * @return storyListViewModel with story list in the payload.
	 * storyListViewModel:  int sid;
	 *						String title;
	 *						UserViewModel user;
	 *						String path;
	 *						Date entryDate;
	 *						Date lastUpdated;
	 *						String status;
	 *						String rawDataPath;
	 *						String sourceType;
	 *						LicenseViewModel license;
	 */
	@OPTIONS
    @Path("all/{pageNo}/{perPage}")
	public Response getStoryList(@HeaderParam("Access-Control-Request-Headers") final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
	}

	@Path("all/{pageNo}/{perPage}")
    @GET
    @ApiOperation(
    		value = "Finds story list.",
    		notes = "getAllStoryList note",
    		response = StoryListResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid pageNo, perPage supplied"),
			@ApiResponse(code = 404, message = "StoryList not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getStoryList(@ApiParam(value = "number of the page", required = true) @PathParam("pageNo") final int pageNo, @ApiParam(value = "story lists amount on per page", required = true) @PathParam("perPage") final int perPage) {
		BasicTableBL basicBL = new BasicTableBL();
		StoryListResponseModel result = basicBL.getStoryList(pageNo,perPage);
		String json = result.toJson();
		return this.makeCORS(Response.status(200).entity(json));
	}

	
	/**
	 * Finds story list
	 * 
	 * @return storyListViewModel with story list in the payload.
	 * storyListViewModel:  int sid;
	 *						String title;
	 *						UserViewModel user;
	 *						String path;
	 *						Date entryDate;
	 *						Date lastUpdated;
	 *						String status;
	 *						String rawDataPath;
	 *						String sourceType;
	 *						LicenseViewModel license;
	 */
	
	@OPTIONS
    @Path("all/")
	public Response getAllStoryList(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
	}

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