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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzard;
import edu.pitt.sis.exp.colfusion.models.GeneralResponseModel;


@Path("Wizard/")
public class WizardController {
	
	final Logger logger = LogManager.getLogger(WizardController.class.getName());
	
	private Response makeCORS(ResponseBuilder req) {
	   ResponseBuilder rb = req.header("Access-Control-Allow-Origin", "*")
	      .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

	   //if (!"".equals(returnMethod)) {
	      rb.header("Access-Control-Allow-Headers", "Content-Type, Accept"); //returnMethod);
	   //}

	   return rb.build();
	}
	
	/**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON serialization of the model.
     */ 
	@Path("test")
    @GET
    @Produces("application/json")
    public GeneralResponseModel getProvenance() {
    	
    	GeneralResponseModel grm = new GeneralResponseModel(); 
    	
    	grm.IsSuccessful = true;
    	grm.Message = "MsgBLBL";
    	
        return grm;
    }
		
    /**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("/acceptFileFromWizard")
    public Response corsMyResource(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Processes the form submitted from wizard step where data file is uploaded.
     * 
     * @param sid story id for which the files are submitted.
     * @return sends back the general response with status and message.
     */
	@Path("acceptFileFromWizard")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response acceptFileFromWizard(
    		@FormDataParam("sid") String sid, 
    		@FormDataParam("uploadTimestamp") String uploadTimestamp, 
    		@FormDataParam("fileType") String fileType,
    		@FormDataParam("dbType") String dbType,
    		@FormDataParam("upload_file") List<FormDataBodyPart> files) {
    	
		// Extract files names and files from the input parameter.
		Map<String, InputStream> inputStreams = new HashMap<String, InputStream>();
		for (FormDataBodyPart file : files) {						
			inputStreams.put(file.getContentDisposition().getFileName(), file.getValueAs(InputStream.class));
		}
		
    	//Store the files
    	DataSubmissionWizzard wizardBLL = new DataSubmissionWizzard();
    	GeneralResponseModel result = wizardBLL.storeUploadedFiles(sid, uploadTimestamp, fileType, dbType, inputStreams);
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}






