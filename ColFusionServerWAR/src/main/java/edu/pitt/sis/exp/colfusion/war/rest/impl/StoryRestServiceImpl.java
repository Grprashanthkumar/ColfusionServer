/**
 * 
 */
package edu.pitt.sis.exp.colfusion.war.rest.impl;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.BasicTableBL;
import edu.pitt.sis.exp.colfusion.bll.StoryBL;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.AddColumnMetadataEditHistoryResponse;
import edu.pitt.sis.exp.colfusion.responseModels.BasicTableResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.ColumnMetadataResponse;
import edu.pitt.sis.exp.colfusion.responseModels.GetColumnMetadataEditHistoryResponse;
import edu.pitt.sis.exp.colfusion.responseModels.GetLicenseResponse;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataHistoryResponse;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataResponse;
import edu.pitt.sis.exp.colfusion.war.rest.StoryRestService;

/**
 * @author Evgeny
 *
 */
@Path("Story/")
public class StoryRestServiceImpl extends BaseController implements StoryRestService {
	
	final Logger logger = LogManager.getLogger(StoryRestServiceImpl.class.getName());
	
	@Override
	public Response newStoryMetadata(final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response newStoryMetadata(final int userId) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataResponse result = storyBL.createStory(userId);
    	
    	return this.makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response getStoryMetadata(final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response getStoryMetadata(final int sid) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataResponse result = storyBL.getStoryMetadata(sid);
    	
    	return this.makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response updateStoryMetadata(final StoryMetadataViewModel metadata) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataResponse result = storyBL.updateStoryMetadata(metadata);
    	
    	return this.makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response getStoryMetadataHistory(final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response getStoryMetadataHistory(final int sid, final String historyItem) {
    	
		StoryBL storyBL = new StoryBL();
		
		StoryMetadataHistoryResponse result = storyBL.getStoryMetadataHistory(sid, historyItem);
		
    	
    	return this.makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response getColumnMetadata(final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response getColumnMetadata(final int sid, final String tableName){
		 StoryBL storyBL= new StoryBL();
		 
		 ColumnMetadataResponse result= storyBL.getColumnMetaData(sid, tableName);
		
		 return this.makeCORS(Response.status(200).entity(result));
	}
	
	@Override
	public Response addColumnMetadataEditHistory(final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response addColumnMetadataEditHistory(final int cid, final int userid, final String editAttribute, final String reason, final String editValue){
		String changedreason = reason.replace("*!~~!*", "/");
		String changededitValue = editValue.replace("*!~~!*", "/");
		StoryBL storyBL = new StoryBL();
		AddColumnMetadataEditHistoryResponse result = storyBL.addColumnMetaEditHistory(cid,userid,editAttribute,changedreason,changededitValue);
		 return this.makeCORS(Response.status(200).entity(result));
	}
	
	@Override
	public Response getColumnMetadataEditHistory(final String requestH) {
		return this.makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response getColumnMetadataEditHistory(final int cid, final String editAttribute){ 
		StoryBL storyBL = new StoryBL();
		GetColumnMetadataEditHistoryResponse result = storyBL.getColumnMetaEditHistory(cid,editAttribute);
		
	
		 return this.makeCORS(Response.status(200).entity(result));
	}
	
	@Override
	public Response tableInfo(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	
	@Override
	public Response tableInfo(final int sid, final String tableName) {
    	
		BasicTableBL basicBL=new BasicTableBL();
		BasicTableResponseModel result= basicBL.getTableInfo(sid, tableName);
		return this.makeCORS(Response.status(200).entity(result));
    }
	
	@Override
	public Response getTableDataBySidAndName(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
		
	@Override
	public Response getTableDataBySidAndName(final int sid, final String tableName, 
    		final int perPage, final int pageNumber) {
		
		BasicTableBL basicBL=new BasicTableBL();
		JointTableByRelationshipsResponeModel result = basicBL.getTableDataBySidAndName(sid, tableName, perPage, pageNumber);
		
		String json = result.toJson();
		
		return this.makeCORS(Response.status(200).entity(json));
    }
	@Override
	public Response getLicense(){
		StoryBL storyBL = new StoryBL();
		GetLicenseResponse  result = storyBL.getLicense();
		System.out.println("StoryControler error licese");
		return this.makeCORS(Response.status(200).entity(result));
}
}