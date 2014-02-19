/**
 * 
 */
package edu.pitt.sis.exp.colfusion.controllers;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzardBL;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;

/**
 * @author Evgeny
 *
 */
@Path("Story/")
public class StoryController extends BaseController {
	
	final Logger logger = LogManager.getLogger(StoryController.class.getName());
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("metadata")
    public Response acceptFileFromWizardOption(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Processes the form submitted from wizard step where data file is uploaded.
     * 
     * @param sid story id for which the files are submitted.
     * @return sends back the general response with status and message.
     */
	@Path("metadata/{sid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response acceptFileFromWizard(@PathParam("sid") int sid) {
    	
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		
		storyMgr.findDatasetInfoBySid(sid, true);
		
    	AcceptedFilesResponse result;// = wizardBLL.storeUploadedFiles(sid, uploadTimestamp, fileType, dbType, inputStreams);
    	
    	return makeCORS(Response.status(200).entity(storyMgr)); //.build();
    }
}
