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

import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataViewModel;

public interface StoryRestService {

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("metadata/new/{userId}")
    public abstract Response newStoryMetadata(@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Creates new story in the database.
	 * 
	 * @param userId is the id of the authors of the story.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/new/{userId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response newStoryMetadata(@PathParam("userId") int userId);

	@OPTIONS
    @Path("metadata/{sid: [0-9]+}")
    public abstract Response getStoryMetadata(@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Finds metadata for the story with provided sid.
	 * 
	 * @param sid story id for which the metadata should be fetched.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/{sid: [0-9]+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response getStoryMetadata(@PathParam("sid") int sid);

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
    public abstract Response updateStoryMetadata(StoryMetadataViewModel metadata);

	@OPTIONS
    @Path("metadata/{sid: [0-9]+}/history/{historyItem}")
    public abstract Response getStoryMetadataHistory(@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Finds metadata for the story with provided sid.
	 * 
	 * @param sid story id for which the metadata should be fetched.
	 * @return response with story metadata in the payload.
	 */
	@Path("metadata/{sid: [0-9]+}/history/{historyItem}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response getStoryMetadataHistory(@PathParam("sid") int sid, @PathParam("historyItem") String historyItem);

	@OPTIONS
    @Path("{sid}/{tableName}/metadata/columns")
    public abstract Response getColumnMetadata(@HeaderParam("Access-Control-Request-Headers") String requestH);

	@Path("{sid}/{tableName}/metadata/columns")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getColumnMetadata(@PathParam("sid") int sid, @PathParam("tableName") String tableName);

	@OPTIONS
    @Path("metadata/columns/addEditHistory/{cid}/{userid}/{editAttribute}/{reason}/{editValue}")
    public abstract Response addColumnMetadataEditHistory(@HeaderParam("Access-Control-Request-Headers") String requestH);

	@Path("metadata/columns/addEditHistory/{cid}/{userid}/{editAttribute}/{reason}/{editValue}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response addColumnMetadataEditHistory(@PathParam("cid") int cid, @PathParam("userid") int userid,
			@PathParam("editAttribute") String editAttribute, @PathParam("reason") String reason, @PathParam("editValue") String editValue);

	@OPTIONS
    @Path("metadata/columns/getEditHistory/{cid}/{editAttribute}")
    public abstract Response getColumnMetadataEditHistory(@HeaderParam("Access-Control-Request-Headers") String requestH);

	@Path("metadata/columns/getEditHistory/{cid}/{showAttribute}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getColumnMetadataEditHistory(@PathParam("cid") int cid,
			@PathParam("showAttribute") String editAttribute);

	@OPTIONS
    @Path("{sid}/{tableName}/tableInfo")
    public abstract Response tableInfo(@HeaderParam("Access-Control-Request-Headers") String requestH);

	@Path("{sid}/{tableName}/tableInfo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response tableInfo(@PathParam("sid") int sid, @PathParam("tableName") String tableName);

	@OPTIONS
    @Path("{sid}/{tableName}/tableData/{perPage}/{pageNumber}")
    public abstract Response getTableDataBySidAndName(@HeaderParam("Access-Control-Request-Headers") String requestH);

	@Path("{sid}/{tableName}/tableData/{perPage}/{pageNumber}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response getTableDataBySidAndName(@PathParam("sid") int sid,
    		@PathParam("tableName") String tableName, @PathParam("perPage") int perPage, @PathParam("pageNumber") int pageNumber);

	@Path("metadata/license")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public abstract Response getLicense();
	
	@OPTIONS
    @Path("{sid}/StoryStatus")
    public abstract Response getStoryStatus(@HeaderParam("Access-Control-Request-Headers") final String requestH);
	
	
	@Path("{sid}/StoryStatus")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response getStoryStatus(@PathParam("sid") final int sid);
	
	@OPTIONS
    @Path("{sid}/MineRelationships/{perPage}/{pageNumber}")
    public abstract Response getMineRelationships(@HeaderParam("Access-Control-Request-Headers") final String requestH);
	
	
	@Path("{sid}/MineRelationships/{perPage}/{pageNumber}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public abstract Response getMineRelationships(@PathParam("sid") final int sid, @PathParam("perPage") int perPage, @PathParam("pageNumber") int pageNumber);
}