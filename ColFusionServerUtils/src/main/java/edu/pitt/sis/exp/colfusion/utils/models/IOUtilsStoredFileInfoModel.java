package edu.pitt.sis.exp.colfusion.utils.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


//TODO: merge with FileContentInfoViewModel model
/**
 * The class holds some information of the files which were successfully stored on the disk.
 *
 */
@XmlRootElement
public class IOUtilsStoredFileInfoModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String _name = "";
	private String _absoluteName = "";
	private String _extension = "";
	private long _lastModified;

	/**
	 * Set the extension of the stored file.
	 *
	 * @param extension of the files to be set.
	 */
	public void setFileExtension(final String extension) {
		this._extension = extension;
	}

	/**
	 * Returns the extension of the file. If files don't have any extension returns empty string.
	 *
	 * @return the extension of the file. If files don't have any extension returns empty string.
	 */
	public String getFileExtension() {
		return this._extension;
	}

	/**
	 * Set the name of the file with extension.
	 *
	 * @param name of the file with extension.
	 */
	public void setFileName(final String name) {
		this._name = name;
	}

	/**
	 * Returns the name of the file with extension.
	 *
	 * @return the name of the file with extension.
	 */
	public String getFileName() {
		return this._name;
	}

	/**
	 * Set the absolute name of the file which include absolute path and the file name with extension
	 *
	 * @param name of the file.
	 */
	public void setAbsoluteFileName(final String absoluteName) {
		this._absoluteName = absoluteName;
	}

	/**
	 * Returns the absolute name of the file which include absolute path and the file name with extension
	 *
	 * @return the absolute name of the file which include absolute path and the file name with extension
	 */
	public String getAbsoluteFileName() {
		return this._absoluteName;
	}

	/**
	 * Set the last modified data in millisecond in Unix epoch.
	 *
	 * @param lastModified in Unix epoch.
	 */
	public void setLastModified(final long lastModified) {
		this._lastModified = lastModified;
	}

	/**
	 * Returns last modified value in Unix epoch.
	 *
	 * @return last modified value in Unix epoch.
	 */
	public long getLastModified() {
		return this._lastModified;
	}

	/**
	 * Check if the file is an archive.
	 *
	 * @return true if the file is an archive and false otherwise.
	 */
	public boolean isArchive() {
		//TODO: check for other types of archives
		if (this._extension.equals("zip")) {
			return true;
		}

		return false;
	}

}
