package edu.pitt.sis.exp.colfusion.bll.dataSubmissionWizard;

import java.io.InputStream;
import java.util.List;
import edu.pitt.sis.exp.colfusion.models.GeneralResponseModel;

/**
 * Handles all business logic for the data submisison wizard.
 * 
 * @author Evgeny
 *
 */
public class DataSubmissionWizzard {
	
	/**
	 * Stores the uploaded files into disk. Also it performs some actions depending on the file type. e.g. unzips archives.
	 * 
	 * @param sid story id for which the files are uploaded.
	 * @param uploadTimestamp when the files were uploaded.
	 * @param fileType the types of the file (most probably it is file extension, I will need to update this comment).
	 * @param dbType if the uploaded file is database dump file, then this parameters says what database engine.
	 * @param files the input streams of the files. 
	 * @return
	 */
	public GeneralResponseModel StoreUploadedFiles(String sid, String uploadTimestamp, 
    		String fileType, String dbType, List<InputStream> files) {
				
		
		
		return null;		
	}
}
