/**
 *
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import edu.pitt.sis.exp.colfusion.utils.StringUtils;

/**
 *
 */
@XmlRootElement
public class WorksheetViewModel {
	private String sheetName;
	private int headerRow;
	private String startColumn;
	private int numberOfRows;
	private int indexInTheFile;
	private ArrayList<DatasetVariableViewModel> variables;

	/**
	 * The unique (among sheets/tables in one file) short (not exceeding database table name limit) database save name of this sheet/table.
	 * Clients don't need to set this.
	 *
	 * TODO this should be done somewhere else
	 */
	@XmlTransient
	private String uniqueShortName = "";

	public WorksheetViewModel() {
		this.variables = new ArrayList<>();
	}

	public WorksheetViewModel(final String sheetName, final int headerRow,
			final String startColumn, final int numberOfRows,
			final int indexInTheFile, final ArrayList<DatasetVariableViewModel> variables) {
		setSheetName(sheetName);
		setHeaderRow(headerRow);
		setStartColumn(startColumn);
		setNumberOfRows(numberOfRows);
		setIndexInTheFile(indexInTheFile);
		setVariables(variables);
	}

	/**
	 * @return the sheetName
	 */
	public String getSheetName() {
		return this.sheetName;
	}

	/**
	 * @param sheetName the sheetName to set
	 */
	public void setSheetName(final String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * One based row index.
	 *
	 * @return the headerRow
	 */
	public int getHeaderRow() {
		return this.headerRow;
	}

	/**
	 * @param headerRow the headerRow to set
	 */
	public void setHeaderRow(final int headerRow) {
		this.headerRow = headerRow;
	}

	/**
	 * @return the startColumn
	 */
	public String getStartColumn() {
		return this.startColumn;
	}

	/**
	 * @param startColumn the startColumn to set
	 */
	public void setStartColumn(final String startColumn) {
		this.startColumn = startColumn;
	}

	/**
	 * @return the numberOfRows
	 */
	public int getNumberOfRows() {
		return this.numberOfRows;
	}

	/**
	 * @param numberOfRows the numberOfRows to set
	 */
	public void setNumberOfRows(final int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	/**
	 * @return the indexInTheFile
	 */
	public int getIndexInTheFile() {
		return this.indexInTheFile;
	}

	/**
	 * @param indexInTheFile the indexInTheFile to set
	 */
	public void setIndexInTheFile(final int indexInTheFile) {
		this.indexInTheFile = indexInTheFile;
	}

	/**
	 * @return the variables
	 */
	public ArrayList<DatasetVariableViewModel> getVariables() {
		return this.variables;
	}

	/**
	 * @param variables the variables to set
	 */
	public void setVariables(final ArrayList<DatasetVariableViewModel> variables) {
		this.variables = variables;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	//	public void setUniqueShortName(final String value) {
	//		uniqueShortName = value;
	//	}

	public String getUniqueShortName() {
		if (StringUtils.isSpecified(this.uniqueShortName)) {
			return this.uniqueShortName;
		}

		if (StringUtils.isNullOrEmpty(this.uniqueShortName)) {
			//TODO FIXME: this is just a precocious
			this.uniqueShortName = StringUtils.makeShortUnique(StringUtils.replaceSpaces(this.sheetName));
		}

		return this.uniqueShortName;
	}
}
