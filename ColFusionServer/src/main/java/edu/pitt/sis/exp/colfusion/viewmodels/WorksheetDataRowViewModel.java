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
public class WorksheetDataRowViewModel {
	private ArrayList<String> worksheetDataRow;
	
	public WorksheetDataRowViewModel() {
		worksheetDataRow = new ArrayList<>();
	}
	
	public WorksheetDataRowViewModel(ArrayList<String> worksheetDataRow) {
		setWorksheetDataRow(worksheetDataRow);
	}

	/**
	 * @return the worksheetDataRow
	 */
	public ArrayList<String> getWorksheetDataRow() {
		return worksheetDataRow;
	}

	/**
	 * @param worksheetDataRow the worksheetDataRow to set
	 */
	public void setWorksheetDataRow(ArrayList<String> worksheetDataRow) {
		this.worksheetDataRow = worksheetDataRow;
	}
}
