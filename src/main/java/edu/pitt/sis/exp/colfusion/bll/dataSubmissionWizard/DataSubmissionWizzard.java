package edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizard;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.models.GeneralResponseModel;
import edu.pitt.sis.exp.colfusion.utils.IOUtils;
import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;


/**
 * Handles all business logic for the data submission wizard.
 * 
 * @author Evgeny
 *
 */
public class DataSubmissionWizzard {
	
	final Logger logger = LogManager.getLogger(DataSubmissionWizzard.class.getName());
	
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
	public GeneralResponseModel StoreUploadedFiles(String sid, String uploadTimestamp, 
    		String fileType, String dbType, Map<String, InputStream> inputStreams) {
				
		String uploadFilesLocation = ConfigManager.getInstance().getPropertyByName(ConfigManager.uploadRawFileLocationKey);
		String uploadFileAbsolutePath = uploadFilesLocation + File.separator + sid; 
		
		try {
		
			for (Map.Entry<String, InputStream> inputStream : inputStreams.entrySet()){
				
				IOUtilsStoredFileInfoModel fileInfo = IOUtils.getInstance().writeInputStreamToFile(inputStream.getValue(), uploadFileAbsolutePath, inputStream.getKey());
			}
		} catch (Exception e) {
			 
			logger.error("StoreUploadedFiles failed!", e);
		}
		
		
		GeneralResponseModel result = new GeneralResponseModel();
		result.Status = "OK";
		result.Message = "no errors";
		
		return result;		
	}
	
	
}
