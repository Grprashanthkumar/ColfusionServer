/**
 *
 */
package edu.pitt.sis.exp.colfusion.bll.importers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.importers.csv.CSVImporter;
import edu.pitt.sis.exp.colfusion.bll.importers.excel.ExcelFileHandlerImpl;
import edu.pitt.sis.exp.colfusion.bll.importers.excel.ExcelImporter;

public class ImporterFactory {

	final static Logger logger = LogManager.getLogger(ImporterFactory.class.getName());

	public static Importer getImporter(final ImporterType importerType) throws Exception {
		switch (importerType) {
		case ExcelImporter:
			return new ExcelImporter(new ExcelFileHandlerImpl());

		case CSVImporter:
			return new CSVImporter();

		default:

			logger.error("Importer type not found", importerType);

			//TODO, FIXME: throw proper exception
			throw new Exception("Importer type not found");
		}
	}
}
