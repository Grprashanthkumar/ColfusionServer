/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class WorksheetViewModel {
	private String sheetName;
	private int headerRow;
	private String startColumn;
	private int numberOfRows;
	
	public WorksheetViewModel() {
		
	}
	
	public WorksheetViewModel(String sheetName, int headerRow, String startColumn, int numberOfRows) {
		setSheetName(sheetName);
		setHeaderRow(headerRow);
		setStartColumn(startColumn);
		setNumberOfRows(numberOfRows);
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * @return the headerRow
	 */
	public int getHeaderRow() {
		return headerRow;
	}

	/**
	 * @param headerRow the headerRow to set
	 */
	public void setHeaderRow(int headerRow) {
		this.headerRow = headerRow;
	}

	/**
	 * @return the startColumn
	 */
	public String getStartColumn() {
		return startColumn;
	}

	/**
	 * @param startColumn the startColumn to set
	 */
	public void setStartColumn(String startColumn) {
		this.startColumn = startColumn;
	}

	/**
	 * @return the numberOfRows
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * @param numberOfRows the numberOfRows to set
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
}
