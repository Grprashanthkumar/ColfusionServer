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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzardBL;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.responseModels.FileContentInfoReponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseImpl;
import edu.pitt.sis.exp.colfusion.responseModels.OneNumberResponse;
import edu.pitt.sis.exp.colfusion.responseModels.PreviewFileResponse;
import edu.pitt.sis.exp.colfusion.viewmodels.CreateTemplateViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FilesContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.PreviewFileViewModel;

@Path("Wizard/")
public class WizardController extends BaseController {
	
	final Logger logger = LogManager.getLogger(WizardController.class.getName());
	
	/**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "application/json" media type.
     *
     * @return JSON serialization of the model.
     */ 
	@Path("test")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public GeneralResponseImpl getProvenance() {
    	
    	GeneralResponseImpl grm = new GeneralResponseImpl(); 
    	
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
    @Path("acceptFileFromWizard")
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
    	DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
    	AcceptedFilesResponse result = wizardBLL.storeUploadedFiles(sid, uploadTimestamp, fileType, dbType, inputStreams);
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("createTemplate")
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTemplate(CreateTemplateViewModel createTemplateViewModel) {
		    	
		GeneralResponseImpl result = new GeneralResponseImpl();
    	result.isSuccessful = true;
    	result.message = "YEAH";
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("getFilesContentInfo")
    public Response getFilesContentInfo(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * For each file gets sheet names, extension and filename. For the unarchived fiels, return info about the first file only.
     * 
     * @param createTemplateViewModel is the view model of type {@link CreateTemplateViewModel} which has info about each uploaded file.
     * 
     * @return the {@link FileContentInfoReponse} which has payload as {@link ArrayList} of {@link FileContentInfoViewModel} objects
     * which has info about each file.
     */
	@Path("getFilesContentInfo")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilesContentInfo(CreateTemplateViewModel createTemplateViewModel) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		FileContentInfoReponse result = wizardBLL.getFilesContentInfo(createTemplateViewModel);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("getFilesVariablesAndNameRecommendations")
    public Response getFilesVariablesAndNameRecommendations(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Retreive variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
     * 
     * @param sid story id for which the template need to be created.
     * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
     * @param fileNames the information about uploaded files.
     * 
     * @return sends back the general response with status and message.
     */
	@Path("getFilesVariablesAndNameRecommendations")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	//TODO: this need to be changed. it should accept the source type and source settings as a JSON object, then that object will be passed to source importer which will know how to process it.
    //TODO: change input param type to {@link FilesContentInfoViewModel} first.
	public Response getFilesVariablesAndNameRecommendations(List<FileContentInfoViewModel> filesWithSelectedSheets) {
		    	
	DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
	FileContentInfoReponse result = wizardBLL.getFilesVariablesAndRecomendations(filesWithSelectedSheets);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("putDataMatchingStepData")
    public Response putDataMatchingStepDataOption(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Retreive variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
     * 
     * @param sid story id for which the template need to be created.
     * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
     * @param fileNames the information about uploaded files.
     * 
     * @return sends back the general response with status and message.
     */
	@Path("putDataMatchingStepData")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	//TODO: this need to be changed. it should accept the source type and source settings as a JSON object, then that object will be passed to source importer which will know how to process it.
    public Response putDataMatchingStepData(FilesContentInfoViewModel dataMatchingStepData) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
					
		GeneralResponseImpl result = wizardBLL.saveVariablesMetadata(dataMatchingStepData);
		
		if (result.isSuccessful) {
			result = wizardBLL.generateKTR(dataMatchingStepData);
		}
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("triggerDataLoad/{sid}")
    public Response triggerDataLoad(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Retreive variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
     * 
     * @param sid story id for which the template need to be created.
     * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
     * @param fileNames the information about uploaded files.
     * 
     * @return sends back the general response with status and message.
     */
	@Path("triggerDataLoad/{sid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	//TODO: this need to be changed. it should accept the source type and source settings as a JSON object, then that object will be passed to source importer which will know how to process it.
    public Response triggerDataLoad(@PathParam("sid") int sid) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		GeneralResponseImpl result = wizardBLL.triggerDataLoadExecution(sid);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("getDataPreviewFromFile")
    public Response getDataPreviewFromFile(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Retreive variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
     * 
     * @param sid story id for which the template need to be created.
     * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
     * @param fileNames the information about uploaded files.
     * 
     * @return sends back the general response with status and message.
     */
	@Path("getDataPreviewFromFile")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response getDataPreviewFromFile(PreviewFileViewModel previewFileViewModel) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		PreviewFileResponse result = wizardBLL.getDataPreviewFromFile(previewFileViewModel);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	/**
     * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
     * @param requestH
     * @return
     */
	@OPTIONS
    @Path("estimateDataPreviewFromFile")
    public Response estimateDataPreviewFromFile(@HeaderParam("Access-Control-Request-Headers") String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	/**
     * Retreive variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
     * 
     * @param sid story id for which the template need to be created.
     * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
     * @param fileNames the information about uploaded files.
     * 
     * @return sends back the general response with status and message.
     */
	@Path("estimateDataPreviewFromFile")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public Response estimateDataPreviewFromFile(PreviewFileViewModel previewFileViewModel) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		OneNumberResponse result = wizardBLL.estimateDataPreviewFromFile(previewFileViewModel);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}