package edu.pitt.sis.exp.colfusion.war.rest.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzardBL;
import edu.pitt.sis.exp.colfusion.bll.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.FileContentInfoReponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponseImpl;
import edu.pitt.sis.exp.colfusion.bll.responseModels.OneNumberResponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.PreviewFileResponse;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.CreateTemplateViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FilesContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;

@Path("Wizard/")
public class WizardRestService {

	final Logger logger = LogManager.getLogger(WizardRestService.class.getName());

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
	public Response acceptFileFromWizard(@FormDataParam("sid") final String sid,
			@FormDataParam("uploadTimestamp") final String uploadTimestamp,
			@FormDataParam("fileType") final String fileType,
			@FormDataParam("dbType") final String dbType,
			@FormDataParam("upload_file") final List<FormDataBodyPart> files) {

		// Extract files names and files from the input parameter.
		final Map<String, InputStream> inputStreams = new HashMap<String, InputStream>();
		for (final FormDataBodyPart file : files) {
			inputStreams.put(file.getContentDisposition().getFileName(), file.getValueAs(InputStream.class));
		}

		//Store the files
		final DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		final AcceptedFilesResponse result = wizardBLL.storeUploadedFiles(sid, inputStreams);

		return Response.status(200).entity(result).build(); //.build();
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
	public Response createTemplate(final CreateTemplateViewModel createTemplateViewModel) {

		final GeneralResponseImpl result = new GeneralResponseImpl();
		result.isSuccessful = true;
		result.message = "YEAH";

		return Response.status(200).entity(result).build(); //.build();
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
	public Response getFilesContentInfo(final CreateTemplateViewModel createTemplateViewModel) {

		final DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();

		final FileContentInfoReponse result = wizardBLL.getFilesContentInfo(createTemplateViewModel);

		return Response.status(200).entity(result).build(); //.build();
	}

	/**
	 * Retrieve variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
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
	public Response getFilesVariablesAndNameRecommendations(final List<FileContentInfoViewModel> filesWithSelectedSheets) {

		final DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();

		final FileContentInfoReponse result = wizardBLL.getFilesVariablesAndRecomendations(filesWithSelectedSheets);

		return Response.status(200).entity(result).build(); //.build();
	}

	/**
	 * Retrieve variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
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
	public Response putDataMatchingStepData(final FilesContentInfoViewModel dataMatchingStepData) {

		final DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();

		GeneralResponseImpl result = wizardBLL.saveVariablesMetadata(dataMatchingStepData);

		if (result.isSuccessful) {
			result = wizardBLL.generateKTR(dataMatchingStepData);
		}

		return Response.status(200).entity(result).build(); //.build();
	}

	/**
	 * Retrieve variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
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
	public Response triggerDataLoad(@PathParam("sid") final int sid) {

		final DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();

		final GeneralResponseImpl result = wizardBLL.triggerDataLoadExecution(sid);

		return Response.status(200).entity(result).build(); //.build();
	}

	/**
	 * Retrieve variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
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
	public Response getDataPreviewFromFile(final PreviewFileViewModel previewFileViewModel) {

		final DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();

		final PreviewFileResponse result = wizardBLL.getDataPreviewFromFile(previewFileViewModel);

		return Response.status(200).entity(result).build(); //.build();
	}

	/**
	 * Retrieve variable names from each file and try to guess data type for each of them. Also contains recommended variables names.
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
	public Response estimateDataPreviewFromFile(final PreviewFileViewModel previewFileViewModel) {

		final DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();

		final OneNumberResponse result = wizardBLL.estimateDataPreviewFromFile(previewFileViewModel);

		return Response.status(200).entity(result).build(); //.build();
	}
}