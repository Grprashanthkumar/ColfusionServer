package edu.pitt.sis.exp.colfusion.importers.excel;

import java.io.Closeable;

public interface ExcelFile extends Closeable {

	public int getNumberOfSheets();

	public ExcelSheet getSheet(int sheetIndex);
}
