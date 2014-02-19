package edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizzard;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;
import edu.pitt.sis.exp.colfusion.responseModels.AcceptedFilesResponse;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;
import edu.pitt.sis.exp.colfusion.viewmodels.OneUploadedItemViewModel;


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
	
	
}
