/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class PreviewFileViewModel {
	private String fileAbsoluteName;
	private String fileName;
	private int previewRowsPerPage;
	private int previewPage;
	
	private ArrayList<WorksheetDataViewModel> worksheetsData;
	
	public PreviewFileViewModel() {
		setWorksheetsData(new ArrayList<WorksheetDataViewModel>());
	}
	
	public PreviewFileViewModel(String fileAbsoluteName, String fileName, int previewRowsPerPage, int previewPage,
			ArrayList<WorksheetDataViewModel> worksheetsData) {
		setFileAbsoluteName(fileAbsoluteName);
		setFileName(fileName);
		setPreviewRowsPerPage(previewRowsPerPage);
		setPreviewPage(previewPage);
		setWorksheetsData(worksheetsData);
	}
	
	/**
	 * @return the fileAbsoluteName
	 */
	public String getFileAbsoluteName() {
		return fileAbsoluteName;
	}
	/**
	 * @param fileAbsoluteName the fileAbsoluteName to set
	 */
	public void setFileAbsoluteName(String fileAbsoluteName) {
		this.fileAbsoluteName = fileAbsoluteName;
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
	 * @return the rowsPerPage
	 */
	public int getPreviewRowsPerPage() {
		return previewRowsPerPage;
	}
	/**
	 * @param rowsPerPage the rowsPerPage to set
	 */
	public void setPreviewRowsPerPage(int previewRowsPerPage) {
		this.previewRowsPerPage = previewRowsPerPage;
	}
	/**
	 * @return the page
	 */
	public int getPreviewPage() {
		return previewPage;
	}
	/**
	 * @param page the page to set
	 */
	public void setPreviewPage(int previewPage) {
		this.previewPage = previewPage;
	}

	/**
	 * @return the worksheetsData
	 */
	public ArrayList<WorksheetDataViewModel> getWorksheetsData() {
		return worksheetsData;
	}

	/**
	 * @param worksheetsData the worksheetsData to set
	 */
	public void setWorksheetsData(ArrayList<WorksheetDataViewModel> worksheetsData) {
		this.worksheetsData = worksheetsData;
	}
	
	@Override
	public String toString() {
		//return String.format("fileAbsoluteName: %s, fileName: %s, previewRowsPerPage: %d, previewPage: %d, number of worksheets: %d", 
		//		fileAbsoluteName, fileName, previewRowsPerPage, previewPage,worksheetsData.size());
		return ReflectionToStringBuilder.toString(this);
	}
}
