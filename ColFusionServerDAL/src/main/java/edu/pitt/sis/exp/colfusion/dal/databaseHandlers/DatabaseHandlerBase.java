/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.databaseHandlers;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Cell;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Column;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.ColumnGroup;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Row;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManager;
//import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Table;

/**
 * @author Evgeny
 *
 */
public abstract class DatabaseHandlerBase implements Closeable {
//	public abstract class DatabaseHandlerBase  {	
	private int sid;
	private String host;
    private int port;
    private String user;
    private String password;
    private String database;
    private DatabaseHanderType databaseHanderType;
    
    private final char dbCharToWrapNamesWithSpaces;
    private final char dbCharToWrapStrings;
    
    private Connection connection;
    
    protected ExecutionInfoManager executionInfoMgr; 
    protected int executionLogId;
    
    Logger logger = LogManager.getLogger(DatabaseHandlerBase.class.getName());
    protected DatabaseConnectionInfo databaseConnectionInfo;
    
    public DatabaseHandlerBase(final DatabaseConnectionInfo databaseConnectionInfo,final int sid){
       this.databaseConnectionInfo = databaseConnectionInfo;
       
   	this.dbCharToWrapNamesWithSpaces = ' ';
   	this.dbCharToWrapStrings = ' ';
	setSid(sid);
	setHost(databaseConnectionInfo.getHost());
	setPort(databaseConnectionInfo.getPort());
	setUser(databaseConnectionInfo.getUser());
	setPassword(databaseConnectionInfo.getPassword());
	setDatabase(databaseConnectionInfo.getDatabase());
    }
    public DatabaseHandlerBase(final DatabaseConnectionInfo databaseConnectionInfo){
        this.databaseConnectionInfo = databaseConnectionInfo;
        
    	this.dbCharToWrapNamesWithSpaces = ' ';
    	this.dbCharToWrapStrings = ' ';
// 	setSid(sid);
 	setHost(databaseConnectionInfo.getHost());
 	setPort(databaseConnectionInfo.getPort());
 	setUser(databaseConnectionInfo.getUser());
 	setPassword(databaseConnectionInfo.getPassword());
 	setDatabase(databaseConnectionInfo.getDatabase());
     }
    public DatabaseHandlerBase(final int sid, final String host, final int port, final String user, final String password, final String database, final DatabaseHanderType databaseHanderType,
    		final ExecutionInfoManager executionInfoMgr, final int executionLogId, final char dbCharToWrapNamesWithSpaces, final char dbCharToWrapStrings) {
    	setSid(sid);
    	setHost(host);
    	setPort(port);
    	setUser(user);
    	setPassword(password);
    	setDatabase(database);
    	setDatabaseHanderType(databaseHanderType);
    	
    	this.dbCharToWrapNamesWithSpaces = dbCharToWrapNamesWithSpaces;
    	this.dbCharToWrapStrings = dbCharToWrapStrings;
    	
    	this.executionInfoMgr = executionInfoMgr;
    	this.executionLogId = executionLogId;
    }
    
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(final String host) {
		this.host = host;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(final int port) {
		this.port = port;
	}
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(final String user) {
		this.user = user;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = password;
	}
	/**
	 * @return the database
	 */
	public String getDatabase() {
		return database;
	}
	/**
	 * @param database the database to set
	 */
	public void setDatabase(final String database) {
		this.database = database;
	}
	/**
	 * @return the databaseHanderType
	 */
	public DatabaseHanderType getDatabaseHanderType() {
		return databaseHanderType;
	}
	/**
	 * @param databaseHanderType the databaseHanderType to set
	 */
	public void setDatabaseHanderType(final DatabaseHanderType databaseHanderType) {
		this.databaseHanderType = databaseHanderType;
	}
    
	/**
	 * @return the dbCharToWrapNamesWithSpaces
	 */
	public char getDbCharToWrapNamesWithSpaces() {
		return dbCharToWrapNamesWithSpaces;
	}

	/**
	 * @return the dbCharToWrapStrings
	 */
	public char getDbCharToWrapStrings() {
		return dbCharToWrapStrings;
	}
	
	/**
	 * Initializes the connection.
	 * @return the connection.
	 * @throws Exception 
	 */
	protected void openConnection() throws SQLException {
		try {
			connection = DriverManager.getConnection(getConnectionString(), getUser(), getPassword());
			
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				try {
					executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] openConnection failed. Error: %s", e.toString()));
				} catch (Exception e1) {
					logger.error("getConnection failed in MySQLDatabaseHandler", e1);
				}
			}
			
			logger.error("getConnection failed in MySQLDatabaseHandler", e);
			throw e;
		} 
	}
	
	protected Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			openConnection();
		}
	
		return connection;
	}
	
	/**
	 * Closes the connection.
	 * @throws SQLException 
	 */
//	@Override
	@Override
	public void close() {
		if (connection != null) {
			try { 
				connection.close(); 
			} 
			catch (SQLException ignore) {
				logger.error("close failed ", ignore);
			}
		}
	}
	
	/**
	 * Creates a connection string and return it.
	 * @return connection string.
	 */
	public abstract String getConnectionString();
	
	/**
     * Create a database for given name if it doesn't exist yet. In both cases connection is updated to use the database.
     * @return if execution was successful.
	 * @throws SQLException 
	 * @throws Exception 
     */
	public abstract boolean createDatabaseIfNotExist(String databaseName) throws SQLException, Exception;
	
	/**
     * Delete a database for given name if it exists yet. 
     * @return if execution was successful.
	 * @throws SQLException 
	 * @throws Exception 
     */
	public abstract boolean deleteDatabaseIfExists(String databaseName) throws SQLException, Exception;

	/**
	 * Creates a table where the data should be loaded.
	 * @param tableName the name of the table to create.
	 * @param variables the list of columns.
	 * @return if execution was successful.
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public abstract boolean createTableIfNotExist(String tableName, List<String> variables) throws SQLException, Exception;

	/**
	 * Creates indices on a list of columns. Depending on combineColumns parameter one or many indices will be created.
	 * @param columnNames - a list of names of columns on which index (indices) need to be created.
	 * @param combineColumns - if true, only one index will be created where key will be combination of all columns, otherwise a separate index on each column will be created.
	 * @throws SQLException 
	 */
	public abstract void createIndecesIfNotExist(String tableName, String columnNames) throws SQLException;

	// protected abstract String getConnectionString();
    
    public abstract boolean tempTableExist(int sid, String tableName)  throws SQLException;
    
    public abstract void removeTable(int sid, String tableName) throws SQLException;
    
    public abstract void backupOriginalTable(int sid, String tableName) throws SQLException;
    
    public abstract int getColCount(int sid, String tableName) throws SQLException;
    
    public abstract ArrayList<ArrayList<String>> getRows(String tableName, int colCount) throws SQLException;
    
    public abstract void createTable(int sid, String tableName) throws SQLException;
    
    public abstract void createTempTable(String query, int sid, String tableName) throws SQLException;
    
    public abstract void insertIntoTempTable(String query, int sid, String tableName) throws SQLException;
    // addition here
    public abstract void createOriginalTable(String query, int sid, String tableName) throws SQLException;
    
    public abstract void insertIntoTable(String query, int sid, String tableName) throws SQLException;
    
    public abstract void importCsvToTable(String dir, String tableName) throws SQLException;
    
    public abstract void insertIntoTable(int sid, String tableName, ArrayList<ArrayList<String>> rows, ArrayList<String> columnNames) throws SQLException;
	
	private Table runQuery(final String tableName,
			final List<String> columnDbNames, final String sqlWithLimit)
			throws SQLException {
		logger.info(String.format("About to execute this query: %s", sqlWithLimit));
		
		try (Statement statement = getConnection().createStatement()) {
			
			ResultSet resultSet = statement.executeQuery(sqlWithLimit);
			
			Table result = new Table();
			long index = 0;
			while (resultSet.next()) {
				ColumnGroup columnGroup = new ColumnGroup(tableName, sid);
				
				for (String column : columnDbNames) {
					
					Column colfusionColumn = new Column(column, new Cell(resultSet.getString(column)));
					
					columnGroup.getColumns().add(colfusionColumn);
				}
				
				Row row = new Row();
				row.getColumnGroups().add(columnGroup);
				result.getRows().add(row);
				logger.info(String.format("Query result table currently has %d rows.", ++index));
			}
			
			logger.info(String.format("Query '%s' resulted in %d rows", sqlWithLimit, index));
			
			return result;
		} catch (SQLException e) {
			
			logger.error("Something wrong happened when getAll tried to execute sql query.", e);
			throw e;
		}
	}

	protected abstract String wrapSQLIntoLimit(String sqlString, int perPage,
			int pageNumber);

	private String constructSelectFromSQL(final String tableName,
			final List<String> columnDbNames) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT " + this.getDbCharToWrapNamesWithSpaces());
		
		String columnDbNamesCSV = StringUtils.join(columnDbNames, String.format("%c, %c", this.getDbCharToWrapNamesWithSpaces(), this.getDbCharToWrapNamesWithSpaces()));
		
		sql.append(String.format("%s%c FROM %c%s%c", columnDbNamesCSV, 
				this.getDbCharToWrapNamesWithSpaces(), this.getDbCharToWrapNamesWithSpaces(), 
				tableName, this.getDbCharToWrapNamesWithSpaces()));
		
		String sqlString = sql.toString();
		return sqlString;
	}

	protected String wrapInEscapeChars(final String tableName) {
		return String.format("%s%s%s", getDbCharToWrapNamesWithSpaces(), 
				tableName, getDbCharToWrapNamesWithSpaces());
	}

	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(final int sid) {
		this.sid = sid;
	}
	private String constructSelectFromSQL(final RelationKey relationKey,
			final List<String> columnDbNames) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT " + this.getDbCharToWrapNamesWithSpaces());
		
		String columnDbNamesCSV = StringUtils.join(columnDbNames, String.format("%c, %c", this.getDbCharToWrapNamesWithSpaces(), this.getDbCharToWrapNamesWithSpaces()));
		
		sql.append(String.format("%s%c FROM %c%s%c", columnDbNamesCSV, 
				this.getDbCharToWrapNamesWithSpaces(), this.getDbCharToWrapNamesWithSpaces(), 
				relationKey.getDbTableName(), this.getDbCharToWrapNamesWithSpaces()));
		
		String sqlString = sql.toString();
		return sqlString;
	}
	public abstract void createIndecesIfNotExist(RelationKey relationKey, String columnNames) throws SQLException;
	public Table getAll(final RelationKey relationKey) throws SQLException {
		
		List<String> allColumnsInTable = getAllColumnsInTable(relationKey);
		
		return getAll(relationKey, allColumnsInTable);
	}
public Table getAll(final RelationKey relationKey, final int perPage, final int pageNumber) throws SQLException {
		
		List<String> allColumnsInTable = getAllColumnsInTable(relationKey);
		
		return getAll(relationKey, allColumnsInTable, perPage, pageNumber);
	}
public Table getAll(final RelationKey relationKey, final List<String> columnDbNames) throws SQLException {
	String sqlString = constructSelectFromSQL(relationKey, columnDbNames);
	
	return runQuery(relationKey, columnDbNames, sqlString);
}
public Table getAll(final RelationKey relationKey, final List<String> columnDbNames, final int perPage, final int pageNumber) throws SQLException {
	String sqlString = constructSelectFromSQL(relationKey, columnDbNames);
	
	String sqlWithLimit = wrapSQLIntoLimit(sqlString, perPage, pageNumber);
	
	return runQuery(relationKey, columnDbNames, sqlWithLimit);
}
public abstract List<String> getAllColumnsInTable(RelationKey relationKey) throws SQLException;
public abstract int getCount(RelationKey relationKey) throws SQLException;
protected String wrapInEscapeChars(final RelationKey relationKey) {
	return String.format("%s%s%s", getDbCharToWrapNamesWithSpaces(), 
			relationKey.getDbTableName(), getDbCharToWrapNamesWithSpaces());
}
private Table runQuery(final RelationKey relationKey,
		final List<String> columnDbNames, final String sqlWithLimit)
		throws SQLException {
	logger.info(String.format("About to execute this query: %s", sqlWithLimit));
	
	try (Statement statement = getConnection().createStatement()) {
		
		ResultSet resultSet = statement.executeQuery(sqlWithLimit);
		
		Table result = new Table();
		long index = 0;
		while (resultSet.next()) {
			ColumnGroup columnGroup = new ColumnGroup(relationKey.getTableName(), sid);
			
			for (String column : columnDbNames) {
				
				Column colfusionColumn = new Column(column, new Cell(resultSet.getString(column)));
				
				columnGroup.getColumns().add(colfusionColumn);
			}
			
			Row row = new Row();
			row.getColumnGroups().add(columnGroup);
			result.getRows().add(row);
			logger.info(String.format("Query result table currently has %d rows.", ++index));
		}
		
		logger.info(String.format("Query '%s' resulted in %d rows", sqlWithLimit, index));
		
		return result;
	} catch (SQLException e) {
		
		logger.error("Something wrong happened when getAll tried to execute sql query.", e);
		throw e;
	}
}

}