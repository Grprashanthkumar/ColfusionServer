/**
 * 
 */
package edu.pitt.sis.exp.colfusion.models;

import java.util.ArrayList;

import edu.pitt.sis.exp.colfusion.utils.models.IOUtilsStoredFileInfoModel;

/**
 * Represent one uploaded item. 
 * In case of excel file, the files field will have always only one element - the uploaded excel file.
 * In case of archive, the files field will have all files from the archive.
 * 
 * @author Evgeny
 *
 */
public class OneUploadedItem {
	/**
	 * In case of excel file, the files field will have always only one element - the uploaded excel file.
	 * In case of archive, the files field will have all files from the archive.
	 */
	public ArrayList<IOUtilsStoredFileInfoModel> files;
	
	public OneUploadedItem() {
		files = new ArrayList<IOUtilsStoredFileInfoModel>();
	}
}
