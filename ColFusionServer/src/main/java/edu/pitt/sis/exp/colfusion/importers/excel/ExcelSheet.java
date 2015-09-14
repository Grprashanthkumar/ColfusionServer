package edu.pitt.sis.exp.colfusion.importers.excel;

import java.util.stream.Stream;

public interface ExcelSheet {
	public int getRowCount();

	public String getSheetName();

	public int getHeaderRow();

	public Stream<ExcelCell> readRow(int rowIndex);
}
