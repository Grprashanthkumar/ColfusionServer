package edu.pitt.sis.exp.colfusion.bll.importers.excel;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Sheet;

public class ExcelSheetImpl implements ExcelSheet {

	private final Sheet sheet;

	public ExcelSheetImpl(final Sheet sheet) {
		this.sheet = sheet;
	}

	@Override
	public int getRowCount() {
		return  this.sheet.getLastRowNum() - this.sheet.getFirstRowNum() + 1;
	}

	@Override
	public String getSheetName() {
		return this.sheet.getSheetName();
	}

	@Override
	public int getHeaderRow() {
		return this.sheet.getFirstRowNum() +1;
	}

	@Override
	public Stream<ExcelCell> readRow(final int rowIndex) {
		final org.apache.poi.ss.usermodel.Row row = this.sheet.getRow(rowIndex);

		if (row == null) {
			return Stream.empty();
		}

		final short lastCell = row.getLastCellNum();

		//TODO: right now reads all columns, need read starting from user specified.
		return IntStream.range(0, lastCell).mapToObj(i -> new ExcelCellImpl(row.getCell(i)));
	}
}
