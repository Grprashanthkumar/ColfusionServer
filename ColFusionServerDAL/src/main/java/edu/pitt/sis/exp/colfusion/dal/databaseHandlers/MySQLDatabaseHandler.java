/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.databaseHandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.utils.StringUtils;

/**
 * @author Evgeny
 *
 */
public class MySQLDatabaseHandler extends DatabaseHandlerBase {

	Logger logger = LogManager.getLogger(MySQLDatabaseHandler.class.getName());
	
	private static int MYSQL_INDEX_KEY_LENGTH = 100;

	/**
	 * Maximum lengths of index name;
	 */
	private static final int INDEX_NAME_MAX_LENGTH = 64;
	
	private static final String MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";

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
	public MySQLDatabaseHandler(final DatabaseConnectionInfo databaseConnectionInfo) throws Exception {
        super(databaseConnectionInfo);

        loadDriverClass(null, -1);
    }
    
    public MySQLDatabaseHandler(final DatabaseConnectionInfo databaseConnectionInfo, final int sid) throws Exception {
        super(databaseConnectionInfo, sid);

        loadDriverClass(null, -1);
    }
	
	public MySQLDatabaseHandler(final int sid, final String host, final int port, final String user,
			final String password, final String database,
			final DatabaseHanderType databaseHanderType,
			final ExecutionInfoManager executionInfoMgr, final int executionLogId) throws Exception {
		super(sid, host, port, user, password, database, databaseHanderType, executionInfoMgr, executionLogId,
				'`', '\'');
		
		loadDriverClass(executionInfoMgr, executionLogId);
	}

	/**
	 * Load MySQL driver class ({@value #MYSQL_DRIVER_CLASS})
	 * @param executionInfoMgr
	 * @param executionLogId
	 * @throws Exception
	 */
	private void loadDriverClass(final ExecutionInfoManager executionInfoMgr, final int executionLogId) throws Exception {
    	try {
			Class.forName(MYSQL_DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] MySQLDatabaseHandler failed: Could not load MySQL JDBC driver. Error: %s", 
					 e.toString()));
			}
			
			logger.error("MySQLDatabaseHandler failed: Could not load MySQL JDBC driver", e);
			throw e;
		}
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
			statement = getConnection().createStatement();
			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			sql = String.format("CREATE DATABASE IF NOT EXISTS `%s`", databaseName);
			
			statement.executeUpdate(sql);
			
			close();
			
			setDatabase(databaseName);
			
			openConnection();
			
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
	public void createTableIfNotExist(final String tableName, final List<String> variables) throws Exception {
		LinkedHashMap<String, String> columnNameToType = new LinkedHashMap<String, String>();
		
		for(String variable : variables) {
			columnNameToType.put(variable, "TEXT");
		}
		
		createTableIfNotExistInternal(tableName, columnNameToType);
	}

	@Override
	public void createTableIfNotExist(final RelationKey tableTo,
			final ResultSetMetaData tableMetadata) throws Exception {
		//The order in key insertion is important.
		LinkedHashMap<String, String> columnNameToType = new LinkedHashMap<String, String>();
		
		for (int i = 1; i <= tableMetadata.getColumnCount(); i++) {
			
			String columnType = tableMetadata.getColumnTypeName(i);
			
			String columnTypeAndPrecision = ""; 
			
			//Original type was TEXT, but the metadata returned VARCHAR (65535)
			//However using VARCHAR (65535) throws an error
			if (columnType.equals("VARCHAR") && tableMetadata.getPrecision(i) > 1000) {
				columnTypeAndPrecision = "TEXT";
			}
			else {
				columnTypeAndPrecision = tableMetadata.getPrecision(i) == 0 ? columnType : 
					String.format("%s (%s)", columnType, tableMetadata.getPrecision(i));
			}
			
			columnNameToType.put(tableMetadata.getColumnLabel(i), columnTypeAndPrecision);
		}
		
		createTableIfNotExistInternal(tableTo.getDbTableName(), columnNameToType);
	}
	
	private void createTableIfNotExistInternal(final String tableName, final LinkedHashMap<String, String> columnNameToType) throws Exception {
		String sql = constructCreateTableQuery(tableName, columnNameToType);
		
		try {
			executeUpdate(sql);
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] createTableIfNotExist failed for table %s when executing this query %s. Error: %s", 
					tableName, sql, e.toString()));
			}
			
			logger.error(String.format("createTableIfNotExist failed for %s. Error Message:", tableName, e.toString()));
			throw e;
		}
	}

	/**
	 * Construct CREATE TABLE IF EXISTS for given table name and map of column names and types.
	 * 
	 * @param tableName
	 * 			the name of the table that should be created.
	 * @param columnNameToType
	 * 			map of column names to their types. The order is important.
	 * @return string that contain SQL create table if exists statement.
	 */
	private String constructCreateTableQuery(final String tableName,
			final LinkedHashMap<String, String> columnNameToType) {
		//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
		StringBuilder sqlBuilder = new StringBuilder();
		
		sqlBuilder.append(String.format("CREATE TABLE IF NOT EXISTS `%s` (", tableName));
		
		int numberOfColumns = columnNameToType.size();
		int index = 0;
		
		for (Entry<String, String> entry : columnNameToType.entrySet()) {
			
			if (index++ == numberOfColumns - 1) { // The difference is in the last character: comma or parenthesis.
				sqlBuilder.append(String.format("%s %s)", wrapName(entry.getKey()), entry.getValue()));
			}
			else {
				sqlBuilder.append(String.format("%s %s,", wrapName(entry.getKey()), entry.getValue()));
			}
		}
		
		return sqlBuilder.toString();
	}
	
	@Override
	public boolean deleteDatabaseIfExists(final String databaseName) throws Exception {
		String sql = "";
		
		try (Statement statement = getConnection().createStatement();) {
			
			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			sql = String.format("DROP DATABASE IF EXISTS `%s`", databaseName);
			
			statement.executeUpdate(sql);
			
			close();
			
			setDatabase("");
			
			openConnection();
			
			return true;
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] deleteDatabaseIfNotExist failed for database %s when executing this query %s. Error: %s", 
					databaseName, sql, e.toString()));
			}
			
			logger.error(String.format("createDatabaseIfNotExist FAILED for %s", databaseName));
			throw e;
		}
	}

	@Override
	protected String wrapSQLIntoLimit(final String sqlString, final int perPage,
			final int pageNumber) {
		int startPoint = (pageNumber - 1) * perPage;
		
		return String.format("%s LIMIT %d, %d", sqlString, startPoint, perPage);
	}

    @Override
    public boolean tempTableExist(final int sid, final String tableName)
            throws SQLException {
        logger.info(String.format("Getting if temp table exists for sid %d and tablename %s", sid, tableName));

        try (Connection connection = getConnection()) {

            String sql = String.format("SHOW TABLES LIKE 'temp_%s'", tableName);

            try (Statement statement = connection.createStatement()) {

                ResultSet rs = statement.executeQuery(sql);
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }

            } catch (SQLException e) {
                logger.error(String.format("Getting if temp table exists for sid %d and tablename %s FAILED", sid,
                        tableName), e);

                throw e;
            }
        } catch (SQLException e) {
            logger.info(String.format("FAILED to getting if temp table exists for sid %d and tablename %s", sid,
                    tableName));
            throw e;
        }
    }

    @Override
    public void removeTable(final String tableName)
            throws SQLException {
        logger.info(String.format("Removing table for sid %d and tablename %s", getSid(), tableName));

        String sql = String.format("DROP TABLE IF EXISTS %s", wrapName(tableName));
        
        try {
        	executeUpdate(sql);
        }
        catch (SQLException e) {
        	logger.error(String.format("Removing table for sid %d and tablename %s FAILED", getSid(), tableName), e);

            throw e;
        }
    }

    @Override
    public int getColCount(final int sid, final String tableName) throws SQLException {
        logger.info(String.format("Getting column count for sid %d", sid));

        try (Connection connection = getConnection()) {

            String sql = "select count(*) from information_schema.columns where table_schema= ? and table_name= ?";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, "colfusion_filetodb_" + sid);
                statement.setString(2, tableName);

                int colCount = 0;
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    colCount = Integer.parseInt(rs.getString(1));
                }
                return colCount;

            } catch (SQLException e) {
                logger.error(String.format("Getting column count for sid %d FAILED", sid), e);

                throw e;
            }
        } catch (SQLException e) {
            logger.info(String.format("FAILED to Getting column count for sid %d", sid));
            throw e;
        }
    }
	    
    @Override
    public ArrayList<ArrayList<String>> getRows(final String tableName, final int colCount) throws SQLException {
        logger.info(String.format("Getting rows for table %s", tableName));
        ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
        try (Connection connection = getConnection()) {

            String sql = String.format("select * from %s", tableName);

            try (Statement statement = connection.createStatement()) {

                ResultSet rs1 = statement.executeQuery(sql);
                while (rs1.next()) {
                    int colIndex = 1;
                    ArrayList<String> temp = new ArrayList<String>();
                    while (colIndex <= colCount) {
                        temp.add(rs1.getString(colIndex));
                        colIndex++;
                    }
                    rows.add(temp);
                }
                return rows;

            } catch (SQLException e) {
                logger.error(String.format("Getting rows for table %s FAILED", tableName), e);

                throw e;
            }
        } catch (SQLException e) {
            logger.info(String.format("FAILED to getting rows for table %s", tableName));
            throw e;
        }
    }
    
    @Override
    public void createTableFromTable(final String tableNameToCreate, final String tableNameFromWhichToCreate)
            throws SQLException {
    	String message = String.format("Creating table %s from %s for sid %d", tableNameToCreate, tableNameFromWhichToCreate, getSid());
        logger.info(message);

        String sql = String.format("CREATE TABLE %s SELECT * FROM %s", wrapName(tableNameToCreate), wrapName(tableNameFromWhichToCreate));
        
        try {
            executeUpdate(sql);	
        } catch (SQLException e) {
            logger.error("FAILED " + message, e);

            throw e;
        }	       
    }
    
    @Override
    public void insertIntoTable(final String query, final int sid, final String tableName)
            throws SQLException {

        logger.info(String.format("Inserting into table temp_%s for sid %d", tableName, sid));

        try (Connection connection = getConnection()) {

            try (Statement statement = connection.createStatement()) {

                statement.executeUpdate(query);

            } catch (SQLException e) {
                logger.error(String.format("Inserting into table temp_%s for sid %d FAILED", tableName, sid), e);

                throw e;
            }
        } catch (SQLException e) {
            logger.info(String.format("FAILED to Inserting into table temp_%s for sid %d", tableName, sid));
            throw e;
        }
    }
	 
    @Override
    public void importCsvToTable(final String dir, final String tableName) throws SQLException {
        String message = String.format("Importing from %s to table %s", dir, tableName);
        logger.info(message);

        String sql = String.format("LOAD DATA LOCAL INFILE '%s' into table %s  fields terminated by ','  optionally enclosed by '\"' escaped by '\"' lines terminated by '\\r\\n'", 
        		dir, wrapName(tableName));
        
        try {
        	executeUpdate(sql);
        } catch (SQLException e) {
            logger.error("FAILED " + message, e);

            throw e;
        }
    }

    @Override
	public int getCount(final RelationKey relationKey) throws SQLException {
		String sql = String.format("SELECT COUNT(*) as ct from %s", wrapName(relationKey.getDbTableName()));
		
		try (Statement statement = getConnection().createStatement()) {
			
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				return rs.getInt("ct");				
			}
			
			return 0;
		} catch (SQLException e) {
			logger.error(String.format("getAllColumnsInTable FAILED on table '%s'", relationKey.getDbTableName()), e);
			
			throw e;
		}
	}
    
    /**
	 * Generates/makes an index name based on table name and column name. The index name is not unique.
	 * @param tableName
	 * @param columnName
	 * @return generated index name
	 */
    private String makeIndexName(final RelationKey relationKey, final String columnName) {
		String indexName = String.format("Index_%s_%s_%s", this.getDatabase(), relationKey.getDbTableName(), columnName);
		
		String indexShortName = StringUtils.makeShortUnique(indexName, INDEX_NAME_MAX_LENGTH);
		
		logger.info(String.format("Generated index name %s", indexShortName));
		return indexShortName;
	}
	    
    @Override
	public List<String> getAllColumnsInTable(final RelationKey relationKey) throws SQLException {
		logger.info(String.format("Getting all columns in table '%s'", relationKey.getDbTableName()));
		
		String sql = "SELECT `COLUMN_NAME` as columnName FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA`= ? AND `TABLE_NAME`= ?";
		
		try (java.sql.PreparedStatement statement = getConnection().prepareStatement(sql)) {
			statement.setString(1, this.getDatabase());
			statement.setString(2, relationKey.getDbTableName());
			
			ResultSet rs = statement.executeQuery();
			List<String> result = new ArrayList<String>(); 
			while (rs.next()) {
				result.add(rs.getString("columnName"));				
			}
			
			logger.info(String.format("Got %d columns in table '%s'", result.size(), relationKey.getDbTableName()));
			
			return result;
		} catch (SQLException e) {
			logger.error(String.format("getAllColumnsInTable FAILED on table '%s'", relationKey.getDbTableName()), e);
			
			throw e;
		}
	}
	    
    /**
	 * Check whether index exist or not by comparing provided index name with existing indeces in the database.
	 * @param indexName
	 * @return
	 * @throws SQLException 
	 */
    private boolean doesIndexExist(final RelationKey relationKey, final String indexName) throws SQLException {
		
		logger.info(String.format("Checking if an index exists with name %s for table %s", indexName, relationKey.getDbTableName()));
		
		String sql = "SELECT COUNT(*) as cnt FROM INFORMATION_SCHEMA.STATISTICS WHERE table_name = ? and index_name = ?";
		
		try (java.sql.PreparedStatement statement = getConnection().prepareStatement(sql)) {
			statement.setString(1, relationKey.getDbTableName());
			statement.setString(2, indexName);
			
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return rs.getInt("cnt") != 0;				
			}
			
			return false;
		} catch (SQLException e) {
			logger.error(String.format("doesIndexExist FAILED on table %s and index name %s", relationKey.getDbTableName(), indexName), e);
			
			throw e;
		}
	}
	    
    @Override
	public void createIndecesIfNotExist(final RelationKey relationKey, final String columnName) throws SQLException {
		String indexName = makeIndexName(relationKey, columnName);
		
		if (doesIndexExist(relationKey, indexName)) {
			return;
		}
		
		String sql = String.format("ALTER TABLE %s ADD INDEX %s (%s(%d));", 
				wrapName(relationKey.getDbTableName()), wrapName(indexName), wrapName(columnName), MYSQL_INDEX_KEY_LENGTH);
		
		try {
			executeUpdate(sql);	
		} catch (SQLException e) {
			
			logger.error(String.format("createIndecesIfNotExist FAILED for table %s and column name %s and index name %s", relationKey.getDbTableName(), columnName, indexName), e);
			throw e;
		}
	}

	@Override
	public String makeInsertPreparedSQL(final RelationKey tableName, final ResultSetMetaData metaData) throws SQLException {
		StringBuilder result = new StringBuilder("INSERT INTO ").append(wrapName(tableName.getDbTableName())).append(" VALUES (");
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			if (i < metaData.getColumnCount()) {
				result.append("?, ");
			}
			else {
				result.append("?");
			}
		}
		
		result.append(")");
		
		return result.toString();
	}
}
