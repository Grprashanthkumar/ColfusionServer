package edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.models.GeneralResponseModel;


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
		
		for (Map.Entry<String, InputStream> inputStream : inputStreams.entrySet()){
			
			writeToFile(inputStream.getValue(), uploadFileAbsolutePath, inputStream.getKey());
		}
		
		GeneralResponseModel result = new GeneralResponseModel();
		result.Status = "OK";
		result.Message = "no errors";
		
		return result;		
	}
	
	/**
	 * Save uploaded file from input stream to the specified location on the disk.
	 * 
	 * @param uploadedInputStream the stream with the file.
	 * @param uploadedFileDirLocation the location of the directory where the file should be saved. If intermediate directories do not exist, they will be created.
	 * @param uploadedFileName the file name to be created.
	 */
	private void writeToFile(InputStream uploadedInputStream, String uploadedFileDirLocation, String uploadedFileName) {
 
		try {
			File targetDir = new File(uploadedFileDirLocation);
			targetDir.mkdirs();
						
			String fileAbsoluteName = uploadedFileDirLocation + File.separator + uploadedFileName;
			
			int read = 0;
			byte[] bytes = new byte[1024];
			
			OutputStream out = new FileOutputStream(new File(fileAbsoluteName));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
 
			logger.error("Write uploaded file to file failed!", e);
		}
 
	}
}
