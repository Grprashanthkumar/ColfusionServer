/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public class MySQLDatabaseHandler extends DatabaseHandlerBase {

	Logger logger = LogManager.getLogger(MySQLDatabaseHandler.class.getName());
	
	private String connectionString;
	
	public MySQLDatabaseHandler(String host, int port, String user,
			String password, String database,
			DatabaseHanderType databaseHanderType) throws ClassNotFoundException, SQLException {
		super(host, port, user, password, database, databaseHanderType);
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			logger.error("MySQLDatabaseHandler failed: Could not load MySQL JDBC driver", e);
			throw e;
		}
		
		connectionString = String.format("jdbc:mysql://%s:%d/%s", getHost(), getPort(), getDatabase());
		
		openConnection(connectionString);
	}

	@Override
	public void createDatabaseIfNotExist() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createTableIfNotExist(String tableName, List<String> variables) {
		// TODO Auto-generated method stub
		
	}

}
