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
	
	/**
	 * Creates a database handler of specific vendor depending on the database handler type value.
	 * @param host the url of the server.
	 * @param port the port number on which the database is running.
	 * @param user the name of the user to use for the connection.
	 * @param password the password of the user.
	 * @param database the database name which should be used. This parameter can be empty string if the database is doesn't exist yet.
	 * @param databaseHanderType the type of the database (vendor).
	 * @return the vendor specific implementation of the database handler.
	 * @throws Exception
	 */
	public static DatabaseHandlerBase getDatabaseHandler(String host, int port, String user, String password, String database, 
			DatabaseHanderType databaseHanderType) throws Exception {
		switch (databaseHanderType) {
		case MYSQL:
			return new MySQLDatabaseHandler(host, port, user, password, database, databaseHanderType);
			
		default:
			
			logger.error("DatabaseHandler type not found", databaseHanderType);
			
			//TODO, FIXME: throw proper exception
			throw new Exception("DatabaseHandler type not found");
		}
	}
}
