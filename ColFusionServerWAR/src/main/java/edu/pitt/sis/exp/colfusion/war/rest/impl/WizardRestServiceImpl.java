package edu.pitt.sis.exp.colfusion.war.rest.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard.DataSubmissionWizzardBL;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.CreateTemplateViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FilesContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.responseModels.FileContentInfoReponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseImpl;
import edu.pitt.sis.exp.colfusion.responseModels.OneNumberResponse;
import edu.pitt.sis.exp.colfusion.responseModels.PreviewFileResponse;
import edu.pitt.sis.exp.colfusion.war.rest.WizardRestService;

@Path("Wizard/")
public class WizardRestServiceImpl extends BaseController implements WizardRestService {
	
	final Logger logger = LogManager.getLogger(WizardRestServiceImpl.class.getName());
	
    @Override
	public Response acceptFileFromWizard(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response acceptFileFromWizard(
    		final String sid, 
    		final String uploadTimestamp, 
    		final String fileType,
    		final String dbType,
    		final List<FormDataBodyPart> files) {
    	
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
	
	@Override
	public Response createTemplate(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response createTemplate(final CreateTemplateViewModel createTemplateViewModel) {
		    	
		GeneralResponseImpl result = new GeneralResponseImpl();
    	result.isSuccessful = true;
    	result.message = "YEAH";
    	
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response getFilesContentInfo(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response getFilesContentInfo(final CreateTemplateViewModel createTemplateViewModel) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		FileContentInfoReponse result = wizardBLL.getFilesContentInfo(createTemplateViewModel);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response getFilesVariablesAndNameRecommendations(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response getFilesVariablesAndNameRecommendations(final List<FileContentInfoViewModel> filesWithSelectedSheets) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		FileContentInfoReponse result = wizardBLL.getFilesVariablesAndRecomendations(filesWithSelectedSheets);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response putDataMatchingStepData(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response putDataMatchingStepData(final FilesContentInfoViewModel dataMatchingStepData) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
					
		GeneralResponseImpl result = wizardBLL.saveVariablesMetadata(dataMatchingStepData);
		
		if (result.isSuccessful) {
			result = wizardBLL.generateKTR(dataMatchingStepData);
		}
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response triggerDataLoad(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response triggerDataLoad(final int sid) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		GeneralResponseImpl result = wizardBLL.triggerDataLoadExecution(sid);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response getDataPreviewFromFile(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response getDataPreviewFromFile(final PreviewFileViewModel previewFileViewModel) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		PreviewFileResponse result = wizardBLL.getDataPreviewFromFile(previewFileViewModel);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
	
	@Override
	public Response estimateDataPreviewFromFile(final String requestH) {
		return makeCORS(Response.ok()); //, requestH);
    }
	
	@Override
	public Response estimateDataPreviewFromFile(final PreviewFileViewModel previewFileViewModel) {
		    	
		DataSubmissionWizzardBL wizardBLL = new DataSubmissionWizzardBL();
		
		OneNumberResponse result = wizardBLL.estimateDataPreviewFromFile(previewFileViewModel);
		
    	return makeCORS(Response.status(200).entity(result)); //.build();
    }
}