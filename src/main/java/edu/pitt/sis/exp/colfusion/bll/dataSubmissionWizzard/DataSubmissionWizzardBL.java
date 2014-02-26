package edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.responseModels.FileContentInfoReponse;
import edu.pitt.sis.exp.colfusion.responseModels.FilesVariablesAndRecomendationsResponse;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import edu.pitt.sis.exp.colfusion.viewmodels.CreateTemplateViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.FileContentInfoViewModel;
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
				
				//TODO move types of extensions in to enum
				if (fileModel.getFileExtension().equals("csv")) {
					WorksheetViewModel worksheet = new WorksheetViewModel();
					worksheet.setSheetName("Worksheet");
					worksheet.setHeaderRow(1);
		            worksheet.setStartColumn("A");
					oneFileContentInfo.getWorksheets().add(worksheet);
				}
				else if (fileModel.getFileExtension().equals("xls") || fileModel.getFileExtension().equals("xlsx")) {
					oneFileContentInfo.getWorksheets().addAll(getSheetsFromExcel(fileModel));
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

	private Collection<WorksheetViewModel> getSheetsFromExcel(IOUtilsStoredFileInfoModel fileModel) throws IOException, IllegalArgumentException, POIXMLException {
		try {
	    	ArrayList<WorksheetViewModel> result = new ArrayList<>();
			
	        File file = new File(fileModel.getAbsoluteFileName());
	        InputStream is = new FileInputStream(file);
	        boolean xmlBased = "xlsx".equals(fileModel.getFileExtension());
	        try {
	            Workbook wb = xmlBased ?
	                    new XSSFWorkbook(is) :
	                        new HSSFWorkbook(new POIFSFileSystem(is));
	
	            int sheetCount = wb.getNumberOfSheets();
	            
	            for (int i = 0; i < sheetCount; i++) {
	                Sheet sheet = wb.getSheetAt(i);
	                int rows = sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
	
	                WorksheetViewModel worksheet = new WorksheetViewModel();
	                worksheet.setSheetName(sheet.getSheetName());
	                worksheet.setNumberOfRows(rows);
	                worksheet.setHeaderRow(sheet.getFirstRowNum() +1);
	                worksheet.setStartColumn("A");
	                result.add(worksheet); 
	            }
	        } finally {
	            is.close();
	        }
	        
	        return result;
                           
        } catch (IOException e) {
            logger.error("Error getting sheet data for Excel file", e);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("Error getting sheet data for Excel file (only Excel 97 & later supported)", e);
            throw e;
        } catch (POIXMLException e) {
            logger.error("Error getting sheet data for Excel file - invalid XML", e);
            throw e;
        }
		
	}

	public FilesVariablesAndRecomendationsResponse getFilesVariablesAndRecomendations(List<FileContentInfoViewModel> filesWithSelectedSheets) {
		FilesVariablesAndRecomendationsResponse result = new FilesVariablesAndRecomendationsResponse();
		
		try {
			
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
