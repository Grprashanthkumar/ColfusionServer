/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import edu.pitt.sis.exp.colfusion.responseModels.DnameResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.GetColumnMetadataEditHistoryResponse;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.LicensesResponseModel;
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
public class StoryRestService  {
	
	final Logger logger = LogManager.getLogger(StoryRestService.class.getName());
	
	
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
    	
    	return Response.status(200).entity(result).build(); //.build();
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
    	
    	return Response.status(200).entity(result).build(); //.build();
    }
	
	/**
	 * Updates story/dataset metadata.
	 * 
	 * @param sid story id for which the metadata should be fetched.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/{sid: [0-9]+}")
    @POST
    @ApiOperation(
    		value = "Update StoryMetadata.",
    		notes = "For valid response try integer IDs with value <= 5 or > 10. Other values will generated exceptions",
    		response = StoryMetadataResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid ID supplied"),
			@ApiResponse(code = 404, message = "StoryMetadata not found") })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response updateStoryMetadata(final StoryMetadataViewModel metadata) {
    	
		System.out.println("updateStoryMetadata function");
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataResponse result = storyBL.updateStoryMetadata(metadata);
    	
    	return Response.status(200).entity(result).build(); //.build();
    }
	

	
	/**
	 * Finds metadata for the story with provided sid.
	 * 
	 * @param sid story id for which the metadata should be fetched.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/{sid: [0-9]+}/history/{historyItem}")
    @GET
    @ApiOperation(
    		value = "Get StoryMetadataHistory ",
    		notes = " ",
    		response = StoryMetadataHistoryResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "StoryMetadataHistory not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getStoryMetadataHistory(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid, @ApiParam(value = "historyItem", required = true) @PathParam("historyItem") final String historyItem) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataHistoryResponse result = storyBL.getStoryMetadataHistory(sid, historyItem);
		
    	
    	return Response.status(200).entity(result).build(); //.build();
    }
	

	
	@Path("{sid}/{tableName}/metadata/columns")
	@GET
	@ApiOperation(
    		value = "Get ColumnMetadata, according to sid, tableName",
    		notes = " ",
    		response = ColumnMetadataResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "ColumnMetadata not found") })
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response getColumnMetadata(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid, @ApiParam(value = "tableName", required = true)  @PathParam("tableName") final String tableName){
		 StoryBL storyBL= new StoryBL();
		 
		 ColumnMetadataResponse result= storyBL.getColumnMetaData(sid, tableName);
		
		 return Response.status(200).entity(result).build();
	}
	


	@Path("metadata/columns/addEditHistory/{cid}/{userid}/{editAttribute}/{reason}/{editValue}")
	@GET
	@ApiOperation(
    		value = "Add ColumnMetadata ",
    		notes = " This action needs cid and userid, editAttribute, reason, editValue",
    		response = AddColumnMetadataEditHistoryResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "ColumnMetadata not found") })
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response addColumnMetadataEditHistory(@ApiParam(value = "cid", required = true) @PathParam("cid") final int cid, @ApiParam(value = "userid", required = true) @PathParam("userid") final int userid,
			@ApiParam(value = "editAttribute", required = true) @PathParam("editAttribute") final String editAttribute, @ApiParam(value = "reason", required = true) @PathParam("reason") final String reason, @ApiParam(value = "editValue", required = true) @PathParam("editValue") final String editValue){
		String changedreason = reason.replace("*!~~!*", "/");
		String changededitValue = editValue.replace("*!~~!*", "/");
		StoryBL storyBL = new StoryBL();
		AddColumnMetadataEditHistoryResponse result = storyBL.addColumnMetaEditHistory(cid,userid,editAttribute,changedreason,changededitValue);
		 return Response.status(200).entity(result).build();
	}
	

	
	@Path("metadata/columns/getEditHistory/{cid}/{showAttribute}")
	@GET
	@ApiOperation(
    		value = "Finds ColumnMetadata, according to cid and editAttribute",
    		notes = "",
    		response = GetColumnMetadataEditHistoryResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "ColumnMetadata not found") })
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response getColumnMetadataEditHistory(@ApiParam(value = "cid", required = true) @PathParam("cid") final int cid,
			@ApiParam(value = "editAttribute", required = true) @PathParam("showAttribute") final String editAttribute){ 
		StoryBL storyBL = new StoryBL();
		GetColumnMetadataEditHistoryResponse result = storyBL.getColumnMetaEditHistory(cid,editAttribute);
		
	
		 return Response.status(200).entity(result).build();
	}
	

	
	
	@Path("{sid}/{tableName}/tableInfo")
    @GET
    @ApiOperation(
    		value = "Finds story data, according to sid and tableNmae",
    		notes = "",
    		response = BasicTableResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "story not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response tableInfo(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid, @ApiParam(value = "tableName", required = true) @PathParam("tableName") final String tableName) {
    	
		BasicTableBL basicBL=new BasicTableBL();
		BasicTableResponseModel result= basicBL.getTableInfo(sid, tableName);
		return Response.status(200).entity(result).build();
    }
	

		
	@Path("{sid}/{tableName}/tableData/{perPage}/{pageNumber}")
    @GET
    @ApiOperation(
    		value = "Finds story data, according to sid and tableNmae, perPage, pageNumebr",
    		notes = "",
    		response = JointTableByRelationshipsResponeModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "story not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getTableDataBySidAndName(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid,
			@ApiParam(value = "tableName", required = true) @PathParam("tableName") final String tableName, @ApiParam(value = "perPage", required = true) @PathParam("perPage") final int perPage, @ApiParam(value = "pageNumber", required = true) @PathParam("pageNumber") final int pageNumber) {
		
		BasicTableBL basicBL=new BasicTableBL();
		JointTableByRelationshipsResponeModel result = basicBL.getTableDataBySidAndName(sid, tableName, perPage, pageNumber);
		
		String json = result.toJson();
		
		return Response.status(200).entity(json).build();
    }
	
	@Path("metadata/license")
	@GET
	 @ApiOperation(
	    		value = "Finds license",
	    		notes = "",
	    		response = LicensesResponseModel.class)
		@ApiResponses(value = {
				@ApiResponse(code = 404, message = "license not found") })
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLicense(){
		StoryBL storyBL = new StoryBL();
		LicensesResponseModel  result = storyBL.getLicense();
		//System.out.println(result.getPayload().toString());
		return Response.status(200).entity(result).build();
	}
	


	@Path("{sid}/StoryStatus")
    @GET
    @ApiOperation(
    		value = "Finds StoryStatus, according to sid",
    		notes = "",
    		response = StoryStatusResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "StoryStatus not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getStoryStatus(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid) {
		BasicTableBL basicBL = new BasicTableBL();
		StoryStatusResponseModel result = basicBL.getStoryStatus(sid);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
	}


	
	
	@Path("{sid}/MineRelationships/{perPage}/{pageNumber}")
    @GET
    @ApiOperation(
    		value = "Finds Relationship list, according to perPage,pageNumber",
    		notes = "",
    		response = RelationshipsResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Relationship not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getMineRelationships(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid, @ApiParam(value = "perPage", required = true) @PathParam("perPage") final int perPage, @ApiParam(value = "pageNumber", required = true) @PathParam("pageNumber") final int pageNumber) {
		BasicTableBL basicBL = new BasicTableBL();
		RelationshipsResponseModel result = basicBL.getRelationships(sid, perPage, pageNumber);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
		
	}

	/**
	 * Finds attachment list
	 * 
	 * @param sid
	 * @return response with attachment list in the payload.
	 */
	@Path("{sid}/AttachmentList")
    @GET
    @ApiOperation(
    		value = "Finds attachment list, according to sid",
    		notes = "",
    		response = AttachmentListResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "attachment List not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getAttachmentList(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid) {
		BasicTableBL basicBL = new BasicTableBL();
		AttachmentListResponseModel result = basicBL.getAttachmentList(sid);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
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
		return Response.status(200).entity(json).build();
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
		return Response.status(200).entity(json).build();
	}
	
	
	/**
	 * Finds story list
	 * @param sid
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
	@Path("sid/{sid}/")
    @GET
    @ApiOperation(
    		value = "Finds story list, according to sid",
    		notes = "",
    		response = StoryListResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "StoryList not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getStoryList(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid) {
		BasicTableBL basicBL = new BasicTableBL();
		StoryListResponseModel result = basicBL.getStoryListBySid(sid);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
	}
	
	/**
	 * Finds dnameInfo list
	 * @param sid
	 * @return DnameViewModel list in the payload.
	 * DnameViewModel:
	 * Integer cid;
	 * Integer sid;
	 * String dname_chosen;
	 * tring dname_value_type;
	 * String dname_value_unit;
	 * String dname_value_format;
	 * String dname_value_description;
	 * private String dname_original_name;
	 * private String isConstant;
	 * private String constant_value;
	 * private String missing_value;
	 */
	@Path("dname/sid/{sid}/")
	@GET
    @ApiOperation(
    		value = "Finds Dname list, according to sid",
    		notes = "",
    		response = DnameResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "DnameList not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getDnameListBySid(@ApiParam(value = "sid", required = true) @PathParam("sid") final int sid) {
		BasicTableBL basicBL = new BasicTableBL();
		DnameResponseModel result = basicBL.getDnameListBySid(sid);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
	}
	
	/**
	 * Finds dnameInfo list
	 * @param cid
	 * @return DnameViewModel list in the payload.
	 * DnameViewModel:
	 * Integer cid;
	 * Integer sid;
	 * String dname_chosen;
	 * tring dname_value_type;
	 * String dname_value_unit;
	 * String dname_value_format;
	 * String dname_value_description;
	 * private String dname_original_name;
	 * private String isConstant;
	 * private String constant_value;
	 * private String missing_value;
	 */
	@Path("dname/cid/{cid}/")
	@GET
    @ApiOperation(
    		value = "Finds Dname list, according to cid",
    		notes = "",
    		response = DnameResponseModel.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "DnameList not found") })
    @Produces(MediaType.APPLICATION_JSON)
	public Response getDnameListByCid(@ApiParam(value = "cid", required = true) @PathParam("cid") final int cid) {
		BasicTableBL basicBL = new BasicTableBL();
		DnameResponseModel result = basicBL.getDnameListByCid(cid);
		String json = result.toJson();
		return Response.status(200).entity(json).build();
	}
}