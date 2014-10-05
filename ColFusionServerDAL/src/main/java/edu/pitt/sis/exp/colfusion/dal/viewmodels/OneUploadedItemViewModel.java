/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

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
public class OneUploadedItemViewModel {
	/**
	 * In case of excel file, the files field will have always only one element - the uploaded excel file.
	 * In case of archive, the files field will have all files from the archive.
	 */
	private ArrayList<IOUtilsStoredFileInfoModel> files;
	
	public OneUploadedItemViewModel() {
		setFiles(new ArrayList<IOUtilsStoredFileInfoModel>());
	}
	
	public OneUploadedItemViewModel(final ArrayList<IOUtilsStoredFileInfoModel> files) {
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
	public void setFiles(final ArrayList<IOUtilsStoredFileInfoModel> files) {
		this.files = files;
	}
}
