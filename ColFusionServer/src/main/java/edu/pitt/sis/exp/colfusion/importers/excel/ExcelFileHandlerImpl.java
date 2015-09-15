package edu.pitt.sis.exp.colfusion.importers.excel;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExcelFileHandlerImpl implements ExcelFileHandler {
	final Logger logger = LogManager.getLogger(ExcelFileHandlerImpl.class.getName());

	@Override
	public ExcelFile openFile(final String absoluteFileName) throws IOException {
		return new ExcelFileImpl(absoluteFileName);
	}
}
