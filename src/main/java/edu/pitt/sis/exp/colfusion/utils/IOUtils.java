package edu.pitt.sis.exp.colfusion.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 * The class which combines together all utilities functions related to input/output.
 * 
 * @author Evgeny
 *
 */
public class IOUtils {
	
	final Logger logger = LogManager.getLogger(IOUtils.class.getName());
	
	private static IOUtils instance = null;
	
	protected IOUtils() {
		
	}
	
	public static IOUtils getInstance() {
		if(instance == null) {
	         instance = new IOUtils();
	    }
	  
		return instance;
	}
	
	/**
	 * Save uploaded file from input stream to the specified location on the disk.
	 * 
	 * @param uploadedInputStream the stream with the file.
	 * @param uploadedFileDirLocation the location of the directory where the file should be saved. If intermediate directories do not exist, they will be created.
	 * @param uploadedFileName the file name to be created.
	 * @throws IOException if file wasn't written to the disk successfully.
	 */
	public IOUtilsStoredFileInfoModel writeInputStreamToFile(InputStream uploadedInputStream, String dirLocation, String fileName) throws IOException {
 
		try {
			File targetDir = new File(dirLocation);
			targetDir.mkdirs();
						
			String fileAbsoluteName = dirLocation + File.separator + fileName;
			
			int read = 0;
			byte[] bytes = new byte[1024];
			
			File fileToSave = new File(fileAbsoluteName);
			
			// If user submits the same file again, rename old file to include the time it was uploaded/written to the disk
			if (fileToSave.exists()) {
				String newName = fileAbsoluteName + "_version_" + fileToSave.lastModified();
				fileToSave.renameTo(new File(newName));
			}
			
			OutputStream out = new FileOutputStream(fileToSave);
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			
			out.flush();
			out.close();
			
			IOUtilsStoredFileInfoModel fileInfo = new IOUtilsStoredFileInfoModel();
			
			fileInfo.setFileName(fileToSave.getName());
			fileInfo.setFileExtension(FilenameUtils.getExtension(fileToSave.getAbsolutePath()));
			fileInfo.setLastModified(fileToSave.lastModified());
			
			return fileInfo;
			
		} catch (IOException e) {
 
			logger.error("Write uploaded file to file failed!", e);
			
			throw e;
		}
	}
}
