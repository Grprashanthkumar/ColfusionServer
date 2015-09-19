package edu.pitt.sis.exp.colfusion.importers.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.pitt.sis.exp.colfusion.utils.IOUtils;

public class ExcelFileImpl implements ExcelFile {

	final Logger logger = LogManager.getLogger(ExcelFileImpl.class.getName());

	private final String XML_BASED_EXTENSION = "xlsx";

	private String absoluteFileName;
	private final InputStream is;
	private final Workbook wb;

	public ExcelFileImpl(final String absoluteFileName) throws IOException {
		this.setAbsoluteFileName(absoluteFileName);
		this.is = new FileInputStream(new File(absoluteFileName));

		final boolean xmlBased = this.XML_BASED_EXTENSION.equals(IOUtils.getFileExtension(absoluteFileName).toLowerCase());

		this.wb = xmlBased ?
				new XSSFWorkbook(this.is) :
					new HSSFWorkbook(new POIFSFileSystem(this.is));
	}

	public String getAbsoluteFileName() {
		return this.absoluteFileName;
	}

	private void setAbsoluteFileName(final String absoluteFileName) {
		this.absoluteFileName = absoluteFileName;
	}

	@Override
	public void close() throws IOException {
		this.is.close();
	}

	@Override
	public int getNumberOfSheets() {
		if (this.wb == null) {
			final String message = String.format("Workbook is null, but tried to get number of sheets. "
					+ "Either openFile method was not called before or it didn't work propertly.");
			this.logger.error(message);
			throw new NullPointerException(message);
		}

		return this.wb.getNumberOfSheets();
	}

	@Override
	public ExcelSheet getSheet(final int sheetIndex) {
		return new ExcelSheetImpl(this.wb.getSheetAt(sheetIndex));
	}
}
