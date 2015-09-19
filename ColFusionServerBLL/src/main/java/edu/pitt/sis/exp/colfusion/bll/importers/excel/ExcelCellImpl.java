package edu.pitt.sis.exp.colfusion.bll.importers.excel;

import java.io.Serializable;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

public class ExcelCellImpl implements ExcelCell {

	private Serializable value;
	private String dataType;
	private boolean isInError = false;;

	public ExcelCellImpl(final Cell cell) {
		extractVariableCell(cell);
	}

	@Override
	public Serializable getValue() {
		return this.value;
	}

	@Override
	public String getDataType() {
		return this.dataType;
	}

	@Override
	public boolean getIsInError() {
		return this.isInError;
	}

	/**
	 * Some code have been borrows from ExcelImporter.java from OpenRefine.
	 *
	 * @param sourceCell
	 * @return
	 */
	private void extractVariableCell(final org.apache.poi.ss.usermodel.Cell cell) {
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}
		if (cellType == Cell.CELL_TYPE_ERROR ||
				cellType == Cell.CELL_TYPE_BLANK) {
			this.isInError = true;
		}

		if (cellType == Cell.CELL_TYPE_BOOLEAN) {
			this.value = cell.getBooleanCellValue();
			//TODO: the types should be enums, not hardwired strings.
			this.dataType = "boolean";
		} else if (cellType == Cell.CELL_TYPE_NUMERIC) {
			final double d = cell.getNumericCellValue();

			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				this.value = HSSFDateUtil.getJavaDate(d);
				this.dataType = "date";
			} else {
				this.value = d;
				this.dataType = "number";
			}
		} else {
			this.value = cell.getStringCellValue();
			this.dataType = "text";
		}
	}

}
