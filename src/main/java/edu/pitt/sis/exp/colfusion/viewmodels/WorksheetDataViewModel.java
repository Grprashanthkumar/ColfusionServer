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
public class WorksheetDataViewModel {

	private String worksheetName;
	
	private ArrayList<WorksheetDataRowViewModel> worksheetData;
	
	public WorksheetDataViewModel() {
		setWorksheetData(new ArrayList<WorksheetDataRowViewModel>());
	}
	
	public WorksheetDataViewModel(String worksheetName, ArrayList<WorksheetDataRowViewModel> worksheetData) {
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
	public void setWorksheetName(String worksheetName) {
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
	public void setWorksheetData(ArrayList<WorksheetDataRowViewModel> worksheetData) {
		this.worksheetData = worksheetData;
	}
	
}
