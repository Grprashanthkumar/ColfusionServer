package edu.pitt.sis.exp.colfusion.war.rest;

import java.util.ArrayList;
import java.util.List;

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

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.CreateTemplateViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FilesContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.FileContentInfoReponse;

public interface WizardRestService {

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("acceptFileFromWizard")
    public abstract Response acceptFileFromWizard(@HeaderParam("Access-Control-Request-Headers") String requestH);

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
    public abstract Response acceptFileFromWizard(@FormDataParam("sid") String sid,
    		@FormDataParam("uploadTimestamp") String uploadTimestamp, 
    		@FormDataParam("fileType") String fileType, 
    		@FormDataParam("dbType") String dbType,
    		@FormDataParam("upload_file") List<FormDataBodyPart> files);

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("createTemplate")
    public abstract Response createTemplate(@HeaderParam("Access-Control-Request-Headers") String requestH);

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
    public abstract Response createTemplate(
			CreateTemplateViewModel createTemplateViewModel);

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("getFilesContentInfo")
    public abstract Response getFilesContentInfo(@HeaderParam("Access-Control-Request-Headers") String requestH);

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
    public abstract Response getFilesContentInfo(
			CreateTemplateViewModel createTemplateViewModel);

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("getFilesVariablesAndNameRecommendations")
    public abstract Response getFilesVariablesAndNameRecommendations(
    		@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Retreive variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
	 * 
	 * @param sid story id for which the template need to be created.
	 * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
	 * @param fileNames the information about uploaded files.
	 * 
	 * @return sends back the general response with status and message.
	 */

	//TODO: this need to be changed. it should accept the source type and source settings as a JSON object, then that object will be passed to source importer which will know how to process it.
	//TODO: change input param type to {@link FilesContentInfoViewModel} first.
	@Path("getFilesVariablesAndNameRecommendations")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response getFilesVariablesAndNameRecommendations(
			List<FileContentInfoViewModel> filesWithSelectedSheets);

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("putDataMatchingStepData")
    public abstract Response putDataMatchingStepData(@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Retreive variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
	 * 
	 * @param sid story id for which the template need to be created.
	 * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
	 * @param fileNames the information about uploaded files.
	 * 
	 * @return sends back the general response with status and message.
	 */

	//TODO: this need to be changed. it should accept the source type and source settings as a JSON object, then that object will be passed to source importer which will know how to process it.
	@Path("putDataMatchingStepData")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response putDataMatchingStepData(
			FilesContentInfoViewModel dataMatchingStepData);

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("triggerDataLoad/{sid}")
    public abstract Response triggerDataLoad(@HeaderParam("Access-Control-Request-Headers") String requestH);

	/**
	 * Retreive variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
	 * 
	 * @param sid story id for which the template need to be created.
	 * @param fileMode the mode specifying how several files should be processed. Could Append or Separately.
	 * @param fileNames the information about uploaded files.
	 * 
	 * @return sends back the general response with status and message.
	 */

	//TODO: this need to be changed. it should accept the source type and source settings as a JSON object, then that object will be passed to source importer which will know how to process it.
	@Path("triggerDataLoad/{sid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	public abstract Response triggerDataLoad(@PathParam("sid") int sid);

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("getDataPreviewFromFile")
    public abstract Response getDataPreviewFromFile(@HeaderParam("Access-Control-Request-Headers") String requestH);

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
	public abstract Response getDataPreviewFromFile(
			PreviewFileViewModel previewFileViewModel);

	/**
	 * Because we do cross domain AJAX calls, we need to use CORS. Actually it worked for me from simple form, but didn't work from file upload.
	 * @param requestH
	 * @return
	 */
	@OPTIONS
    @Path("estimateDataPreviewFromFile")
    public abstract Response estimateDataPreviewFromFile(@HeaderParam("Access-Control-Request-Headers") String requestH);

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
	public abstract Response estimateDataPreviewFromFile(
			PreviewFileViewModel previewFileViewModel);

}