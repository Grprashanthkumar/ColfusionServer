/**
 *
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FileContentInfoViewModel {

	private String extension;
	private String fileName;
	private String fileAbsoluteName;
	//TODO: FIXME: again using hacks, I think we need to sit down and think about architecture first...
	//Anyway this field will be used for cases when people submit archives, the list of other files which need to be process except the first one will be here in this array
	//Assumption is that the settings for the first file will be applied for all files in the archive.
	private String[] otherFilesAbsoluteNames;
	private ArrayList<WorksheetViewModel> worksheets;

	public FileContentInfoViewModel() {
		setWorksheets(new ArrayList<WorksheetViewModel>());
	}

	public FileContentInfoViewModel(final String extension, final String fileName, final String fileAbsoluteName, final String[] otherFilesAbsoluteNames, final ArrayList<WorksheetViewModel> worksheets) {
		setExtension(extension);
		setFileName(fileName);
		setFileAbsoluteName(fileAbsoluteName);
		setOtherFilesAbsoluteNames(otherFilesAbsoluteNames);
		setWorksheets(worksheets);
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return this.extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(final String extension) {
		this.extension = extension;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the worksheets
	 */
	public ArrayList<WorksheetViewModel> getWorksheets() {
		return this.worksheets;
	}

	/**
	 * @param worksheets the worksheets to set
	 */
	public void setWorksheets(final ArrayList<WorksheetViewModel> worksheets) {
		this.worksheets = worksheets;
	}

	/**
	 * @return the fileAbsoluteName
	 */
	public String getFileAbsoluteName() {
		return this.fileAbsoluteName;
	}

	/**
	 * @param fileAbsoluteName the fileAbsoluteName to set
	 */
	public void setFileAbsoluteName(final String fileAbsoluteName) {
		this.fileAbsoluteName = fileAbsoluteName;
	}

	/**
	 * @return the otherFilesAbsoluteNames
	 */
	public String[] getOtherFilesAbsoluteNames() {
		return this.otherFilesAbsoluteNames;
	}

	/**
	 * @param otherFilesAbsoluteNames the otherFilesAbsoluteNames to set
	 */
	public void setOtherFilesAbsoluteNames(final String[] otherFilesAbsoluteNames) {
		this.otherFilesAbsoluteNames = otherFilesAbsoluteNames;
	}
}
