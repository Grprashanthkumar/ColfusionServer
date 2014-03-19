/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDB;

//TODO: should probably be in a separate package.

/**
 * @author Evgeny
 *
 */
public class LinkedServerHandler {
	
	final Logger logger = LogManager.getLogger(LinkedServerHandler.class.getName());
	
	private StoryTargetDB targetDBConnectionInfo;
	
	protected Connection connection;
	
	public LinkedServerHandler(StoryTargetDB targetDBConnectionInfo) throws SQLException, ClassNotFoundException {
		setTargetDBConnectionInfo(targetDBConnectionInfo);
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			
			logger.error("MySQLDatabaseHandler failed: Could not load MS SQL JDBC driver", e);
			throw e;
		}
		
		openConnection(getConnectionString());
	}

	/**
	 * Creates a connection string and return it.
	 * @return connection string.
	 */
	public String getConnectionString() {
		return String.format("jdbc:sqlserver://%s:%d;DatabaseName=%s", targetDBConnectionInfo.getServerAddress(), targetDBConnectionInfo.getPort(), 
				targetDBConnectionInfo.getDatabaseName());
	}
	
	/**
	 * @return the targetDBConnectionInfo
	 */
	public StoryTargetDB getTargetDBConnectionInfo() {
		return targetDBConnectionInfo;
	}

	/**
	 * @param targetDBConnectionInfo the targetDBConnectionInfo to set
	 */
	public void setTargetDBConnectionInfo(StoryTargetDB targetDBConnectionInfo) {
		this.targetDBConnectionInfo = targetDBConnectionInfo;
	}
	
	/**
	 * Initializes the connection.
	 * @return the connection.
	 * @throws SQLException
	 */
	protected void openConnection(String connectionString) throws SQLException {
		try {
			connection = DriverManager.getConnection(connectionString, targetDBConnectionInfo.getUserName(), targetDBConnectionInfo.getPassword());
			
		} catch (SQLException e) {
			
			logger.error("getConnection failed in MySQLDatabaseHandler", e);
			throw e;
		} 
	}
	
	/**
	 * Closes the connection.
	 * @throws SQLException 
	 */
	public void close() throws SQLException {
		if (connection != null) {
			try { 
				connection.close(); 
			} 
			catch (SQLException ignore) {
				logger.error("close failed ", ignore);
			}
		}
	}
}
