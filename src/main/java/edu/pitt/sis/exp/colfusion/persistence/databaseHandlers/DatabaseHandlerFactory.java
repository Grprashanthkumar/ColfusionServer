/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoDb;

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
	public static DatabaseHandlerBase getDatabaseHandler(final String host, final int port, final String user, final String password, final String database, 
			final DatabaseHanderType databaseHanderType, final ExecutionInfoManager executionInfoMgr, final int executionLogId) throws Exception {
		switch (databaseHanderType) {
		case MYSQL:
			return new MySQLDatabaseHandler(host, port, user, password, database, databaseHanderType, executionInfoMgr, executionLogId);
			
		default:
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] getDatabaseHandler failed: DatabaseHandler type not found for %s", databaseHanderType.getValue()));
			}
			
			logger.error("DatabaseHandler type not found", databaseHanderType);
			
			//TODO, FIXME: throw proper exception
			throw new Exception("DatabaseHandler type not found");
		}
	}
	
	public static DatabaseHandlerBase getDatabaseHandler(final ColfusionSourceinfoDb storyDbInfo) throws Exception {
		return DatabaseHandlerFactory.getDatabaseHandler(storyDbInfo.getServerAddress(), storyDbInfo.getPort(), 
				storyDbInfo.getUserName(), storyDbInfo.getPassword(), storyDbInfo.getSourceDatabase(), DatabaseHanderType.fromString(storyDbInfo.getDriver()), 
				null, -1);
	}
}
