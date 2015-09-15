package edu.pitt.sis.exp.colfusion.importers.excel;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface ExcelFileHandler {

	public ExcelFile openFile(String absoluteFileName) throws FileNotFoundException, IOException;
}
