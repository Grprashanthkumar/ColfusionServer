package edu.pitt.sis.exp.colfusion.utils.models;

/**
 * The class holds some information of the files which were successfully stored on the disk.
 * 
 * @author Evgeny
 *
 */
public class IOUtilsStoredFileInfoModel {

	private String _name = "";
	private String _extension = "";
	private long _lastModified;
	
	/**
	 * Set the extension of the stored file.
	 * 
	 * @param extension of the files to be set.
	 */
	public void setFileExtension(String extension) {
		_extension = extension;
	}
	
	/**
	 * Returns the extension of the file. If files don't have any extension returns empty string.
	 * 
	 * @return the extension of the file. If files don't have any extension returns empty string.
	 */
	public String getFileExtension() {
		return _extension;
	}

	/**
	 * Set the name of the file.
	 * 
	 * @param name of the file.
	 */
	public void setFileName(String name) {
		_name = name;
	}
	
	/**
	 * Returns the name of the file.
	 * 
	 * @return the name of the file.
	 */
	public String getFileName() {
		return _name;
	}

	/**
	 * Set the last modified data in millisecond in Unix epoch.
	 * 
	 * @param lastModified in Unix epoch.
	 */
	public void setLastModified(long lastModified) {
		_lastModified = lastModified;
	}
	
	/**
	 * Returns last modified value in Unix epoch.
	 * 
	 * @return last modified value in Unix epoch.
	 */
	public long setLastModified() {
		return _lastModified;
	}

}
