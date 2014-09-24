/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public class ImporterFactory {

	final static Logger logger = LogManager.getLogger(ImporterFactory.class.getName());
	
	public static Importer getImporter(ImporterType importerType) throws Exception {
		switch (importerType) {
		case ExcelImporter:
			return new ExcelImporter();
			
		case CSVImporter:
			return new CSVImporter();
			
		default:
			
			logger.error("Importer type not found", importerType);
			
			//TODO, FIXME: throw proper exception
			throw new Exception("Importer type not found");
		}
	}
}
