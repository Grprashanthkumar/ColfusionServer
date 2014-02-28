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

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;
import edu.pitt.sis.exp.colfusion.importers.ExcelImporter;
import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.responseModels.FileContentInfoReponse;
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
				
				//TODO move types of extensions in to enum
				if (fileModel.getFileExtension().equals("csv")) {
					WorksheetViewModel worksheet = new WorksheetViewModel();
					worksheet.setSheetName("Worksheet");
					worksheet.setHeaderRow(1);
		            worksheet.setStartColumn("A");
					oneFileContentInfo.getWorksheets().add(worksheet);
				}
				else if (fileModel.getFileExtension().equals("xls") || fileModel.getFileExtension().equals("xlsx")) {
					ExcelImporter excelImporter = new ExcelImporter();
					oneFileContentInfo.getWorksheets().addAll(excelImporter.getSheetsFromExcel(fileModel));
				}
				
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

	

	public FileContentInfoReponse getFilesVariablesAndRecomendations(List<FileContentInfoViewModel> filesWithSelectedSheets) {
		FileContentInfoReponse result = new FileContentInfoReponse();
		
		try {
			
			ExcelImporter excelImporter = new ExcelImporter();
			
			for (FileContentInfoViewModel oneFile : filesWithSelectedSheets) {
				HashMap<String, ArrayList<DatasetVariableViewModel>> variablesForAllSelectedSheetsInFile = excelImporter.readHeaderRow(oneFile);
				
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
	public FileContentInfoReponse saveVariablesMetadata(FilesContentInfoViewModel filesInfo) {
		FileContentInfoReponse result = new FileContentInfoReponse();
		
		try {
			
			DNameInfoManager dNameInfoMgr = new DNameInfoManagerImpl();
			
			for (FileContentInfoViewModel file : filesInfo.getFiles()) {
				
				String tableNamePrefix = file.getWorksheets().size() > 1 ? file.getFileName() + " - " : "";
				
				for (WorksheetViewModel worksheet : file.getWorksheets()) {
					dNameInfoMgr.createOrUpdateSheetMetadata(worksheet, tableNamePrefix, filesInfo.getSid(), filesInfo.getUserId());
				}
			}
			
			result.setPayload(null);
			
			result.isSuccessful = true;
			result.message = "OK";			
			
		} catch (Exception e) {
			logger.error("getFilesContentInfo failed!", e);
			
			result.isSuccessful = false;
			result.message = "There seems to be an error, try later.";
		}
		
		return result;
	}
}
