/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class FileContentInfoViewModel {
	
	private String extension;
	private String fileName;
	private ArrayList<WorksheetViewModel> worksheets;
	
	public FileContentInfoViewModel() {
		setWorksheets(new ArrayList<WorksheetViewModel>());
	}
	
	public FileContentInfoViewModel(String extension, String fileName, ArrayList<WorksheetViewModel> worksheets) {
		setExtension(extension);
		setFileName(fileName);
		setWorksheets(worksheets);
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the worksheets
	 */
	public ArrayList<WorksheetViewModel> getWorksheets() {
		return worksheets;
	}

	/**
	 * @param worksheets the worksheets to set
	 */
	public void setWorksheets(ArrayList<WorksheetViewModel> worksheets) {
		this.worksheets = worksheets;
	}
}
