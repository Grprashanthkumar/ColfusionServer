/**
 * 
 */
package edu.pitt.sis.exp.colfusion.models;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 * Represent one uploaded item. 
 * In case of excel file, the files field will have always only one element - the uploaded excel file.
 * In case of archive, the files field will have all files from the archive.
 * 
 * @author Evgeny
 *
 */
@XmlRootElement
public class OneUploadedItem {
	/**
	 * In case of excel file, the files field will have always only one element - the uploaded excel file.
	 * In case of archive, the files field will have all files from the archive.
	 */
	private ArrayList<IOUtilsStoredFileInfoModel> files;
	
	public OneUploadedItem() {
		setFiles(new ArrayList<IOUtilsStoredFileInfoModel>());
	}
	
	public OneUploadedItem(ArrayList<IOUtilsStoredFileInfoModel> files) {
		setFiles(files);
	}

	/**
	 * @return the files
	 */
	public ArrayList<IOUtilsStoredFileInfoModel> getFiles() {
		return files;
	}

	/**
	 * @param files the files to set
	 */
	public void setFiles(ArrayList<IOUtilsStoredFileInfoModel> files) {
		this.files = files;
	}
}
