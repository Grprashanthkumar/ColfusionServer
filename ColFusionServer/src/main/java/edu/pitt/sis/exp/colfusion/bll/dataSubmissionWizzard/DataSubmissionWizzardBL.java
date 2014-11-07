package edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.CreateTemplateViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.FilesContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.OneUploadedItemViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.PreviewFileViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;
import edu.pitt.sis.exp.colfusion.dataLoadExecutors.DataLoadExecutor;
import edu.pitt.sis.exp.colfusion.dataLoadExecutors.DataLoadExecutorFactory;
import edu.pitt.sis.exp.colfusion.importers.Importer;
import edu.pitt.sis.exp.colfusion.importers.ImporterFactory;
import edu.pitt.sis.exp.colfusion.importers.ImporterType;
import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.process.ProcessManager;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.responseModels.FileContentInfoReponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseImpl;
import edu.pitt.sis.exp.colfusion.responseModels.OneNumberResponse;
import edu.pitt.sis.exp.colfusion.responseModels.PreviewFileResponse;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;


/**
 * Handles all business logic for the data submission wizard.
 * 
 * @author Evgeny
 *
 */
public class DataSubmissionWizzardBL {
	
	final Logger logger = LogManager.getLogger(DataSubmissionWizzardBL.class.getName());
	
	/**
	 * Stores the uploaded files into disk. Also it performs some actions depending on the file type. e.g. unzips archives.
	 * 
	 * @param sid story id for which the files are uploaded.
	 * @param uploadTimestamp when the files were uploaded.
	 * @param fileType the types of the file (most probably it is file extension, I will need to update this comment).
	 * @param dbType if the uploaded file is database dump file, then this parameters says what database engine.
	 * @param inputStreams the input streams of the files. 
	 * @return the response message which will say if the upload was successful and if not what might be the reason.
	 */
	public AcceptedFilesResponse storeUploadedFiles(final String sid, final String uploadTimestamp, 
    		final String fileType, final String dbType, final Map<String, InputStream> inputStreams) {
				
		String uploadFilesLocation = IOUtils.getAbsolutePathInColfutionRoot(ConfigManager.getInstance().getPropertyByName(PropertyKeys.uploadRawFileLocationKey));
		String uploadFileAbsolutePath = uploadFilesLocation + File.separator + sid; 
		
		AcceptedFilesResponse result = new AcceptedFilesResponse();
		
		try {
		
			for (Map.Entry<String, InputStream> inputStream : inputStreams.entrySet()){
				
				IOUtilsStoredFileInfoModel fileInfo = IOUtils.writeInputStreamToFile(inputStream.getValue(), uploadFileAbsolutePath, inputStream.getKey(), false);
				
				if (fileInfo.isArchive()) {
					ArrayList<IOUtilsStoredFileInfoModel> filesInfo = IOUtils.unarchive(fileInfo.getAbsoluteFileName());
					
					OneUploadedItemViewModel oneItem = new OneUploadedItemViewModel();
					oneItem.getFiles().addAll(filesInfo);
					
					result.getPayload().add(oneItem);
				}
				else {
					ArrayList<IOUtilsStoredFileInfoModel> fileInfoArrayList = new ArrayList<IOUtilsStoredFileInfoModel>();
					fileInfoArrayList.add(fileInfo);
					
					OneUploadedItemViewModel oneItem = new OneUploadedItemViewModel();
					oneItem.getFiles().add(fileInfo);
					
					result.getPayload().add(oneItem);
				}
			}
			
			result.isSuccessful = true;
			result.message = "Files are uploaded successfully";
			
		} catch (IOException e) {
			 
			this.logger.error("StoreUploadedFiles failed!", e);
			
			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		} catch (ArchiveException e) {
			
			this.logger.error("StoreUploadedFiles failed!", e);
			
			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}
		
		return result;		
	}

	/**
	 * Gets sheets/tables information from submitted files.
	 * @param createTemplateViewModel is the model which describes uploaded files.
	 * @return the response which has file content info in the payload field.
	 */
	public FileContentInfoReponse getFilesContentInfo(final CreateTemplateViewModel createTemplateViewModel) {
		FileContentInfoReponse result = new FileContentInfoReponse();
		
		try {
			
			for (OneUploadedItemViewModel oneItem : createTemplateViewModel.getFileName()) {
				if (oneItem.getFiles().isEmpty()) {
					//TODO:might need to handle it differently
					continue;
				}
				
				FileContentInfoViewModel oneFileContentInfo = new FileContentInfoViewModel();
				
				IOUtilsStoredFileInfoModel fileModel = oneItem.getFiles().get(0);
				
				oneFileContentInfo.setExtension(fileModel.getFileExtension());
				oneFileContentInfo.setFileName(fileModel.getFileName());
				oneFileContentInfo.setFileAbsoluteName(fileModel.getAbsoluteFileName());
				
				String[] otherFiles = new String[oneItem.getFiles().size() - 1];
				for (int i = 1; i < oneItem.getFiles().size(); i++) {
					otherFiles[i - 1] = oneItem.getFiles().get(i).getAbsoluteFileName();
				}
				
				oneFileContentInfo.setOtherFilesAbsoluteNames(otherFiles);
				
				ImporterType importerType = ImporterType.getImporterType(fileModel.getFileExtension());
			
				Importer importer = ImporterFactory.getImporter(importerType);
				oneFileContentInfo.getWorksheets().addAll(importer.getTables(fileModel));
				
				result.getPayload().add(oneFileContentInfo);
				result.isSuccessful = true;
				result.message = "OK";
			}
			
		} catch (Exception e) {
			this.logger.error("getFilesContentInfo failed!", e);
			
			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}
		
		return result;
	}

	
	/**
	 * Gets variables from selected sheets/tables.
	 * 
	 * @param filesWithSelectedSheets info about files with selected sheets/tables.
	 * @return response which has in payload into about variables from selected sheets/tables.
	 */
	public FileContentInfoReponse getFilesVariablesAndRecomendations(final List<FileContentInfoViewModel> filesWithSelectedSheets) {
		FileContentInfoReponse result = new FileContentInfoReponse();
		
		try {
					
			for (FileContentInfoViewModel oneFile : filesWithSelectedSheets) {
				
				ImporterType importerType = ImporterType.getImporterType(oneFile.getExtension());
				
				Importer importer = ImporterFactory.getImporter(importerType);
				
				HashMap<String, ArrayList<DatasetVariableViewModel>> variablesForAllSelectedSheetsInFile = importer.readVariables(oneFile);
				
				for (WorksheetViewModel worksheet : oneFile.getWorksheets()) {
					if (variablesForAllSelectedSheetsInFile.containsKey(worksheet.getSheetName())) {
						worksheet.setVariables(variablesForAllSelectedSheetsInFile.get(worksheet.getSheetName()));
					}			
				}		
			}
			
			result.setPayload((ArrayList<FileContentInfoViewModel>)filesWithSelectedSheets);
			
			result.isSuccessful = true;
			result.message = "OK";			
			
		} catch (Exception e) {
			this.logger.error("getFilesContentInfo failed!", e);
			
			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}
		
		return result;
	}

	/**
	 * Accepts information about several files includes several worksheets in any of them. Gets the metadata of each variable and stores in the 
	 * database in the dnameinfo table and columnTable info. 
	 * @param dataMatchingStepData {@link List} list of files info {@link FileContentInfoViewModel}
	 * @return {@link FileContentInfoReponse} with success or error message no payload.
	 */
	public GeneralResponseImpl saveVariablesMetadata(final FilesContentInfoViewModel filesInfo) {
		FileContentInfoReponse result = new FileContentInfoReponse();
		
		try {
			
			DNameInfoManager dNameInfoMgr = new DNameInfoManagerImpl();
			
			for (FileContentInfoViewModel file : filesInfo.getFiles()) {
				
				for (WorksheetViewModel worksheet : file.getWorksheets()) {
					dNameInfoMgr.createOrUpdateSheetMetadata(worksheet, filesInfo.getSid(), filesInfo.getUserId());
				}
			}
			
			result.isSuccessful = true;
			result.message = "OK";			
			
		} catch (Exception e) {
			this.logger.error("getFilesContentInfo failed!", e);
			
			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}
		
		return result;
	}

	/**
	 * Creates a KTR file for each file. 
	 * @param dataMatchingStepData
	 * @return
	 */
	public GeneralResponseImpl generateKTR(final FilesContentInfoViewModel dataMatchingStepData) {
		
		GeneralResponseImpl result = new GeneralResponseImpl();
		result.isSuccessful = true;
		result.message = "OK";
		
		KTRManager ktrManager = new KTRManager(dataMatchingStepData.getSid());
		
		for(FileContentInfoViewModel file : dataMatchingStepData.getFiles()) {
			try {
				ktrManager.createTemplate(file);
			} catch (Exception e) {
				
				String msg = String.format("create ktr failed for %s file", file.getFileName());
				
				this.logger.error(msg, e);
				result.message += "\n" + msg;
				
				result.isSuccessful = false;
			}
		}
		
		return result;
	}
	
	/**
	 * Triggers background execution of the ETL job: e.g. either running KTR execution or importing data from database dump file.
	 * @param sid
	 * @return
	 */
	public GeneralResponseImpl triggerDataLoadExecution(final int sid) {
		GeneralResponseImpl result = new GeneralResponseImpl();
		
		result.isSuccessful = true;
		result.message = "OK";
		
		//TODO: make catch more granular
		try {
			
			SourceInfoManager storyMng = new SourceInfoManagerImpl();
			
			DataSourceTypes sourceType = storyMng.getStorySourceType(sid);
			
			DataLoadExecutor executor = DataLoadExecutorFactory.getDataLoadExecutor(sourceType);
			
			executor.setSid(sid);
			
			ProcessManager.getInstance().queueProcess(executor);
			
		} catch (Exception e) {
			result.isSuccessful = false;
			result.message = "Couldn't trigger KTR execution";
			
			this.logger.error("triggerDataLoadExecution failed: Couldn't trigger KTR execution for " + sid);
		}
		
		
		return result;
	}

	/**
	 * Read a range of rows from all sheets/tables in given file. The rage is specified by the number of rows to read and the page number.
	 * 
	 * @param previewFileViewModel has info about the data file and row rages.
	 * @return the {@link PreviewFileResponse} response in which payload is the data read from the file.
	 */
	public PreviewFileResponse getDataPreviewFromFile(final PreviewFileViewModel previewFileViewModel) {
		
		PreviewFileResponse result = new PreviewFileResponse();
		result.isSuccessful = false;
		
		ImporterType importerType = ImporterType.getImporterType(FilenameUtils.getExtension(previewFileViewModel.getFileAbsoluteName()));
		Importer importer = null;
		try {
			importer = ImporterFactory.getImporter(importerType);
		} catch (Exception e) {
			this.logger.error(String.format("getDataPreviewFromFiles failed for %s", previewFileViewModel.toString()), e);
			
			result.message = String.format("Couldn't read data from %s file.", previewFileViewModel.getFileName());
			
			return result;
		}
		
		if (importer == null) {
			this.logger.error(String.format("getDataPreviewFromFiles failed for %s. The imported is null.", previewFileViewModel.toString()));
			
			result.message = String.format("Couldn't read data from %s file.", previewFileViewModel.getFileName());
			
			return result;
		}
		
		try {
			previewFileViewModel.setWorksheetsData(importer.readWorksheetData(previewFileViewModel));
		} catch (FileNotFoundException e) {
			this.logger.error(String.format("getDataPreviewFromFiles failed for %s. File not found.", previewFileViewModel.toString()), e);
			
			result.message = String.format("Couldn't read data from %s file. Could not find the file to read from.", previewFileViewModel.getFileName());
			
			return result;
		} catch (IOException e) {
			this.logger.error(String.format("getDataPreviewFromFiles failed for %s. File not found.", previewFileViewModel.toString()), e);
			
			result.message = String.format("Couldn't read data from %s file.", previewFileViewModel.getFileName());
			
			return result;
		}
		
		result.isSuccessful = true;
		result.setPayload(previewFileViewModel);
		
		return result;
	}

	
	public OneNumberResponse estimateDataPreviewFromFile(final PreviewFileViewModel previewFileViewModel) {
		
		OneNumberResponse result = new OneNumberResponse();
		
		result.isSuccessful = true;
		result.message = "OK";
		
		//TODO: implement
		result.setPayload(1000);
		
		return result;
	}
}
