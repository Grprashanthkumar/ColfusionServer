package edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xmlbeans.impl.regex.REUtil;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;
import edu.pitt.sis.exp.colfusion.importers.ExcelImporter;
import edu.pitt.sis.exp.colfusion.importers.Importer;
import edu.pitt.sis.exp.colfusion.importers.ImporterFactory;
import edu.pitt.sis.exp.colfusion.importers.ImporterType;
import edu.pitt.sis.exp.colfusion.importers.ktr.KTRManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.responseModels.FileContentInfoReponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponse;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import edu.pitt.sis.exp.colfusion.viewmodels.CreateTemplateViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FileContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FilesContentInfoViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.OneUploadedItemViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.WorksheetViewModel;


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
	public AcceptedFilesResponse storeUploadedFiles(String sid, String uploadTimestamp, 
    		String fileType, String dbType, Map<String, InputStream> inputStreams) {
				
		String uploadFilesLocation = ConfigManager.getInstance().getPropertyByName(PropertyKeys.uploadRawFileLocationKey);
		String uploadFileAbsolutePath = uploadFilesLocation + File.separator + sid; 
		
		AcceptedFilesResponse result = new AcceptedFilesResponse();
		
		try {
		
			for (Map.Entry<String, InputStream> inputStream : inputStreams.entrySet()){
				
				IOUtilsStoredFileInfoModel fileInfo = IOUtils.getInstance().writeInputStreamToFile(inputStream.getValue(), uploadFileAbsolutePath, inputStream.getKey());
				
				if (fileInfo.isArchive()) {
					ArrayList<IOUtilsStoredFileInfoModel> filesInfo = IOUtils.getInstance().unarchive(fileInfo.getAbsoluteFileName());
					
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
			 
			logger.error("StoreUploadedFiles failed!", e);
			
			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		} catch (ArchiveException e) {
			
			logger.error("StoreUploadedFiles failed!", e);
			
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
	public FileContentInfoReponse getFilesContentInfo(CreateTemplateViewModel createTemplateViewModel) {
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
			logger.error("getFilesContentInfo failed!", e);
			
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
	public FileContentInfoReponse getFilesVariablesAndRecomendations(List<FileContentInfoViewModel> filesWithSelectedSheets) {
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
			logger.error("getFilesContentInfo failed!", e);
			
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
	public GeneralResponse saveVariablesMetadata(FilesContentInfoViewModel filesInfo) {
		FileContentInfoReponse result = new FileContentInfoReponse();
		
		try {
			
			DNameInfoManager dNameInfoMgr = new DNameInfoManagerImpl();
			
			for (FileContentInfoViewModel file : filesInfo.getFiles()) {
				
				//TODO: this should be done in some other place, because the same line is used KTRManager when creating ktr files.
				String tableNamePrefix = file.getWorksheets().size() > 1 ? file.getFileName() + " - " : "";
				
				for (WorksheetViewModel worksheet : file.getWorksheets()) {
					dNameInfoMgr.createOrUpdateSheetMetadata(worksheet, tableNamePrefix, filesInfo.getSid(), filesInfo.getUserId());
				}
			}
			
			result.isSuccessful = true;
			result.message = "OK";			
			
		} catch (Exception e) {
			logger.error("getFilesContentInfo failed!", e);
			
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
	public GeneralResponse generateKTR(FilesContentInfoViewModel dataMatchingStepData) {
		
		GeneralResponse result = new GeneralResponse();
		result.isSuccessful = true;
		result.message = "OK";
		
		KTRManager ktrManager = new KTRManager(dataMatchingStepData.getSid());
		
		for(FileContentInfoViewModel file : dataMatchingStepData.getFiles()) {
			try {
				ktrManager.createTemplate(file);
			} catch (Exception e) {
				
				String msg = String.format("create ktr failed for %s file", file.getFileName());
				
				logger.error(msg, e);
				result.message += "\n" + msg;
				
				result.isSuccessful = false;
			}
		}
		
		return result;
	}
	
	public GeneralResponse triggerKTRExecution(int sid) {
		// TODO Auto-generated method stub
		return null;
	}
}
