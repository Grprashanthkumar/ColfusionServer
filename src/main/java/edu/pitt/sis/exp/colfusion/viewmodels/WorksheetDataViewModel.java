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
	
	private ArrayList<String[]> worksheetData;
	
	public WorksheetDataViewModel() {
		setWorksheetData(new ArrayList<String[]>());
	}
	
	public WorksheetDataViewModel(String worksheetName, ArrayList<String[]> worksheetData) {
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
	public ArrayList<String[]> getWorksheetData() {
		return worksheetData;
	}

	/**
	 * @param worksheetData the worksheetData to set
	 */
	public void setWorksheetData(ArrayList<String[]> worksheetData) {
		this.worksheetData = worksheetData;
	}
	
}
