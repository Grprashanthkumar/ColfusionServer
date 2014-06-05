/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.managers.ExecutionInfoManager;

/**
 * @author Evgeny
 *
 */
public class MySQLDatabaseHandler extends DatabaseHandlerBase {

	Logger logger = LogManager.getLogger(MySQLDatabaseHandler.class.getName());
	
	private static int MYSQL_INDEX_KEY_LENGTH = 100;
	
	/**
	 * Creates a MySQL database handler. Also the connection is initialized at this time. So always wrap it in try/catch/finally and call close in finally.
	 * @param host the url of the server.
	 * @param port the port number on which the database is running.
	 * @param user the name of the user to use for the connection.
	 * @param password the password of the user.
	 * @param database the database name which should be used. This parameter can be empty string if the database is doesn't exist yet.
	 * @param databaseHanderType the type of the database (vendor).
	 * @throws Exception 
	 */
	public MySQLDatabaseHandler(final String host, final int port, final String user,
			final String password, final String database,
			final DatabaseHanderType databaseHanderType,
			final ExecutionInfoManager executionInfoMgr, final int executionLogId) throws Exception {
		super(host, port, user, password, database, databaseHanderType, executionInfoMgr, executionLogId);
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] MySQLDatabaseHandler failed: Could not load MySQL JDBC driver. Error: %s", 
					 e.toString()));
			}
			
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
	public boolean createDatabaseIfNotExist(final String databaseName) throws Exception {
		Statement statement = null;
		
		String sql = "";
		
		try {
			statement = connection.createStatement();
			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			sql = String.format("CREATE DATABASE IF NOT EXISTS `%s`", databaseName);
			
			statement.executeUpdate(sql);
			
			close();
			
			setDatabase(databaseName);
			
			openConnection(getConnectionString());
			
			return true;
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] createTableIfNotExist failed for database %s when executing this query %s. Error: %s", 
					databaseName, sql, e.toString()));
			}
			
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
	public boolean createTableIfNotExist(final String tableName, final List<String> variables) throws Exception {
		Statement statement = null;
		
		String sql = "";
		
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
			
			sql = sqlBuilder.toString();
			sql = sql.replace("``", "`"); //Because column names might have been already wrapped into `` before.
			
			statement.executeUpdate(sql);
			
			return true;
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] createTableIfNotExist failed for table %s when executing this query %s. Error: %s", 
					tableName, sql, e.toString()));
			}
			
			logger.error(String.format("createTableIfNotExist failed for %s. Error Message:", tableName, e.toString()));
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
	public boolean deleteDatabaseIfExists(final String databaseName) throws Exception {
		Statement statement = null;
		
		String sql = "";
		
		try {
			statement = connection.createStatement();
			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			sql = String.format("DROP DATABASE IF EXISTS `%s`", databaseName);
			
			statement.executeUpdate(sql);
			
			close();
			
			setDatabase("");
			
			openConnection(getConnectionString());
			
			return true;
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] deleteDatabaseIfNotExist failed for database %s when executing this query %s. Error: %s", 
					databaseName, sql, e.toString()));
			}
			
			logger.error(String.format("createDatabaseIfNotExist FAILED for %s", databaseName));
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
	public void createIndecesIfNotExist(final String tableName, final String columnName) throws SQLException {
		String sql = "";
		
		String indexName = makeIndexName(tableName, columnName);
		
		if (doesIndexExist(tableName, indexName)) {
			return;
		}
		
		try (Statement statement = connection.createStatement()) {
			//TODO: escape query, SQL injection is possible. See if it is possible to use prepared statement.
			sql = String.format("ALTER TABLE `%s` ADD INDEX `%s` (`%s`(%d));", tableName, indexName, columnName, MYSQL_INDEX_KEY_LENGTH);
			
			statement.executeUpdate(sql);			
		} catch (SQLException e) {
			
			logger.error(String.format("createIndecesIfNotExist FAILED for table %s and column name %s and index name %s", tableName, columnName, indexName), e);
			throw e;
		}
	}

	/**
	 * Checkes whether index exist or not by comparing provided index name with existing indeces in the database.
	 * @param indexName
	 * @return
	 * @throws SQLException 
	 */
	private boolean doesIndexExist(final String tableName, final String indexName) throws SQLException {
		
		logger.info(String.format("Checking if an index exists with name %s for table %s", indexName, tableName));
		
		String sql = "SELECT COUNT(*) as cnt FROM INFORMATION_SCHEMA.STATISTICS WHERE table_name = ? and index_name = ?";
		
		try (java.sql.PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, tableName);
			statement.setString(2, indexName);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return rs.getInt("cnt") != 0;				
			}
			
			return false;
		} catch (SQLException e) {
			logger.error(String.format("doesIndexExist FAILED on table %s and index name %s", tableName, indexName), e);
			
			throw e;
		}
	}

	/**
	 * Generates/makes an index name based on table name and column name. The index name is not unique.
	 * @param tableName
	 * @param columnName
	 * @return generated index name
	 */
	private String makeIndexName(final String tableName, final String columnName) {
		String indexName = String.format("Index_%s_%s", tableName, columnName);
		
		logger.info(String.format("Generated index name %s", indexName));
		return indexName;
	}
}
