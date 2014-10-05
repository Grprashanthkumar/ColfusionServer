/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class WorksheetDataViewModel {

	private String worksheetName;
	
	private ArrayList<WorksheetDataRowViewModel> worksheetData;
	
	public WorksheetDataViewModel() {
		setWorksheetData(new ArrayList<WorksheetDataRowViewModel>());
	}
	
	public WorksheetDataViewModel(final String worksheetName, final ArrayList<WorksheetDataRowViewModel> worksheetData) {
		setWorksheetName(worksheetName);
		setWorksheetData(worksheetData);
	}

	/**
	 * @return the worksheetName
	 */
	public String getWorksheetName() {
		return worksheetName;
	}

	/**
	 * @param worksheetName the worksheetName to set
	 */
	public void setWorksheetName(final String worksheetName) {
		this.worksheetName = worksheetName;
	}

	/**
	 * @return the worksheetData
	 */
	public ArrayList<WorksheetDataRowViewModel> getWorksheetData() {
		return worksheetData;
	}

	/**
	 * @param worksheetData the worksheetData to set
	 */
	public void setWorksheetData(final ArrayList<WorksheetDataRowViewModel> worksheetData) {
		this.worksheetData = worksheetData;
	}
	
}
