/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public class MySQLDatabaseHandler extends DatabaseHandlerBase {

	Logger logger = LogManager.getLogger(MySQLDatabaseHandler.class.getName());
	
	/**
	 * Creates a MySQL database handler. Also the connection is initialized at this time. So always wrap it in try/catch/finally and call close in finally.
	 * @param host the url of the server.
	 * @param port the port number on which the database is running.
	 * @param user the name of the user to use for the connection.
	 * @param password the password of the user.
	 * @param database the database name which should be used. This parameter can be empty string if the database is doesn't exist yet.
	 * @param databaseHanderType the type of the database (vendor).
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
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
		
		openConnection(getConnectionString());
	}

	@Override
	public String getConnectionString() {
		return String.format("jdbc:mysql://%s:%d/%s", getHost(), getPort(), getDatabase());
	}
	
	@Override
	public boolean createDatabaseIfNotExist(String databaseName) throws SQLException {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			String sql = String.format("CREATE DATABASE IF NOT EXISTS `%s`", databaseName);
			
			statement.executeUpdate(sql);
			
			close();
			
			setDatabase(databaseName);
			
			openConnection(getConnectionString());
			
			return true;
		} catch (SQLException e) {
			logger.error(String.format("createDatabaseIfNotExist failed for %s", databaseName));
			throw e;
		}
		finally {
			if (statement != null) {
				try { 
					statement.close(); 
				} 
				catch (SQLException ignore) {				
				}
			}
		}
	}

	@Override
	public boolean createTableIfNotExist(String tableName, List<String> variables) throws SQLException {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			
			StringBuilder sqlBuilder = new StringBuilder();
			
			sqlBuilder.append(String.format("CREATE TABLE IF NOT EXISTS `%s` (", tableName));
			
			for (int i = 0; i < variables.size(); i++) {
				//FIXME: for now all columns are TEXT, actually we could use the info provided by user about each column
				
				if (i == variables.size() - 1) { // The difference is in the last character: comma or parenthesis.
					sqlBuilder.append(String.format("`%s` TEXT)", variables.get(i)));
				}
				else {
					sqlBuilder.append(String.format("`%s` TEXT,", variables.get(i)));
				}
			}
			
			statement.executeUpdate(sqlBuilder.toString());
			
			return true;
		} catch (SQLException e) {
			logger.error(String.format("createTableIfNotExist failed for %s", tableName));
			throw e;
		}
		finally {
			if (statement != null) {
				try { 
					statement.close(); 
				} 
				catch (SQLException ignore) {				
				}
			}
		}
	}

	@Override
	public boolean deleteDatabaseIfNotExist(String databaseName) throws SQLException {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			String sql = String.format("DROP DATABASE IF EXISTS `%s`", databaseName);
			
			statement.executeUpdate(sql);
			
			close();
			
			setDatabase("");
			
			openConnection(getConnectionString());
			
			return true;
		} catch (SQLException e) {
			logger.error(String.format("createDatabaseIfNotExist failed for %s", databaseName));
			throw e;
		}
		finally {
			if (statement != null) {
				try { 
					statement.close(); 
				} 
				catch (SQLException ignore) {				
				}
			}
		}	
	}
}
