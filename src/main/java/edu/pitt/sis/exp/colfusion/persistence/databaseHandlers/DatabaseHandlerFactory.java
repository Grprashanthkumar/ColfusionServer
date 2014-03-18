/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public class DatabaseHandlerFactory {
	final static Logger logger = LogManager.getLogger(DatabaseHandlerFactory.class.getName());
	
	public static DatabaseHandlerBase getDatabaseHandler(String host, int port, String user, String password, String database, 
			DatabaseHanderType databaseHanderType) throws Exception {
		switch (databaseHanderType) {
		case MSSQL:
			return new MySQLDatabaseHandler(host, port, user, password, database, databaseHanderType);
			
		default:
			
			logger.error("DatabaseHandler type not found", databaseHanderType);
			
			//TODO, FIXME: throw proper exception
			throw new Exception("DatabaseHandler type not found");
		}
	}
}
