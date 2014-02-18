package edu.pitt.sis.exp.colfusion.controllers;

import java.io.InputStream;
import java.util.ArrayList;
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
import edu.pitt.sis.exp.colfusion.models.AcceptedFilesResponseModel;
import edu.pitt.sis.exp.colfusion.models.GeneralResponseModel;
import edu.pitt.sis.exp.colfusion.models.OneUploadedItem;
import edu.pitt.sis.exp.colfusion.viewmodels.CreateTemplateViewModel;


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
    @Produces(MediaType.APPLICATION_JSON)
    public GeneralResponseModel getProvenance() {
    	
    	GeneralResponseModel grm = new GeneralResponseModel(); 
    	
    	grm.isSuccessful = true;
    	grm.message = "MsgBLBL";
    	
        return grm;
    }
		
    /**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("/acceptFileFromWizard")
    public Response acceptFileFromWizardOption(@HeaderParam("Access-Control-Request-Headers") String requestH) {
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
    @Produces(MediaType.APPLICATION_JSON)
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
    	AcceptedFilesResponseModel result = wizardBLL.storeUploadedFiles(sid, uploadTimestamp, fileType, dbType, inputStreams);
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("/createTemplate")
    public Response createTemplateOption(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Creates new KTR file from a template depending on the file format.
     * 
     * @param sid story id for which the template need to be created.
     * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
     * @param fileNames the information about uploaded files.
     * 
     * @return sends back the general response with status and message.
     */
	@Path("createTemplate")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTemplate(CreateTemplateViewModel createTemplateViewModel) {
		    	
    	GeneralResponseModel result = new GeneralResponseModel();
    	result.isSuccessful = true;
    	result.message = "YEAH";
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}