/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.databaseHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManager;

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
	private static MetadataDbHandler metadataDbHandler;
    public MySQLDatabaseHandler(DatabaseConnectionInfo databaseConnectionInfo) throws ClassNotFoundException {
        super(databaseConnectionInfo);// TODO Auto-generated constructor stub

        Class.forName("com.mysql.jdbc.Driver");
    }
    
    public MySQLDatabaseHandler(DatabaseConnectionInfo databaseConnectionInfo,int sid) throws ClassNotFoundException {
        super(databaseConnectionInfo,sid);// TODO Auto-generated constructor stub

        Class.forName("com.mysql.jdbc.Driver");
    }
	public MySQLDatabaseHandler(final int sid, final String host, final int port, final String user,
			final String password, final String database,
			final DatabaseHanderType databaseHanderType,
			final ExecutionInfoManager executionInfoMgr, final int executionLogId) throws Exception {
		super(sid, host, port, user, password, database, databaseHanderType, executionInfoMgr, executionLogId,
				'`', '\'');
		
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
	}

	@Override
	public String getConnectionString() {
		return String.format("jdbc:mysql://%s:%d/%s", getHost(), getPort(), getDatabase());
	}
 //   @Override
 //   protected String getConnectionString() {
 //       return String.format("jdbc:mysql://%s:%d/%s", databaseConnectionInfo.getHost(),
 //               databaseConnectionInfo.getPort(), databaseConnectionInfo.getDatabase());
  //  }
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
	public boolean createTableIfNotExist(final String tableName, final List<String> variables) throws Exception {
		Statement statement = null;
		
		String sql = "";
		
		try {
			statement = getConnection().createStatement();
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
			statement = getConnection().createStatement();
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
		
		try (Statement statement = getConnection().createStatement()) {
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
		
		try (java.sql.PreparedStatement statement = getConnection().prepareStatement(sql)) {
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
		String indexName = String.format("Index_%s_%s_%s", this.getDatabase(), tableName, columnName);
		
		logger.info(String.format("Generated index name %s", indexName));
		return indexName;
	}

	@Override
	public List<String> getAllColumnsInTable(final String tableName) throws SQLException {
		logger.info(String.format("Getting all columns in table '%s'", tableName));
		
		String sql = "SELECT `COLUMN_NAME` as columnName FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA`= ? AND `TABLE_NAME`= ?";
		
		try (java.sql.PreparedStatement statement = getConnection().prepareStatement(sql)) {
			statement.setString(1, this.getDatabase());
			statement.setString(2, tableName);
			
			ResultSet rs = statement.executeQuery();
			List<String> result = new ArrayList<String>(); 
			while (rs.next()) {
				result.add(rs.getString("columnName"));				
			}
			
			logger.info(String.format("Got %d columns in table '%s'", result.size(), tableName));
			
			return result;
		} catch (SQLException e) {
			logger.error(String.format("getAllColumnsInTable FAILED on table '%s'", tableName), e);
			
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
	public int getCount(final String tableName) throws SQLException {
		String sql = String.format("SELECT COUNT(*) as ct from %s", wrapInEscapeChars(tableName));
		
		try (Statement statement = getConnection().createStatement()) {
			
			ResultSet rs = statement.executeQuery(sql);
			
			while (rs.next()) {
				return rs.getInt("ct");				
			}
			
			return 0;
		} catch (SQLException e) {
			logger.error(String.format("getAllColumnsInTable FAILED on table '%s'", tableName), e);
			
			throw e;
		}
	}
	//...
	 @Override
	    protected Connection getConnection()
	            throws SQLException {
	        String connectionString = getConnectionString();

	        try {
	            return DriverManager.getConnection(connectionString, this.getUser(),
	                    this.getPassword());
	        } catch (SQLException e) {
	            String message = String.format("Could not open connection for connection string %s", connectionString);
	            logger.error(message);

	            throw e;
	        }
	    }
/*
	    @Override
	    protected String getConnectionString2() {
	        return String.format("jdbc:mysql://%s:%d/%s", databaseConnectionInfo.getHost(),
	                databaseConnectionInfo.getPort(), databaseConnectionInfo.getDatabase());
	    }
*/
	    @Override
	    public boolean tempTableExist(int sid, String tableName)
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
	    public void removeTable(int sid, String tableName)
	            throws SQLException {
	        logger.info(String.format("Removing table for sid %d and tablename %s", sid, tableName));

	        try (Connection connection = getConnection()) {

	            String sql = String.format("DROP TABLE %s", tableName);

	            try (Statement statement = connection.createStatement()) {

	                statement.executeUpdate(sql);

	            } catch (SQLException e) {
	                logger.error(String.format("Removing table for sid %d and tablename %s FAILED", sid, tableName), e);

	                throw e;
	            }
	        } catch (SQLException e) {
	            logger.info(String.format("FAILED to removing table for sid %d and tablename %s", sid, tableName));
	            throw e;
	        }
	    }

	    @Override
	    public void backupOriginalTable(int sid, String tableName)
	            throws SQLException {

	        logger.info(String.format("Backing up table for sid %d and tablename %s", sid, tableName));

	        try (Connection connection = getConnection()) {

	            String sql = String.format("CREATE TABLE %s SELECT * FROM %s", "temp_" + tableName, tableName);

	            try (Statement statement = connection.createStatement()) {

	                statement.executeUpdate(sql);

	            } catch (SQLException e) {
	                logger.error(String.format("Backing up table for sid %d and tablename %s FAILED", sid, tableName), e);

	                throw e;
	            }
	        } catch (SQLException e) {
	            logger.info(String.format("FAILED to Backing up table for sid %d and tablename %s", sid, tableName));
	            throw e;
	        }
	    }

	    @Override
	    public int getColCount(int sid, String tableName) throws SQLException {
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
	    public ArrayList<ArrayList<String>> getRows(String tableName, int colCount) throws SQLException {
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
	    public void createTable(int sid, String tableName)
	            throws SQLException {
	        logger.info(String.format("Creating table from temp_table for sid %d and tablename %s", sid, tableName));

	        try (Connection connection = getConnection()) {

	            String sql = String.format("CREATE TABLE %s SELECT * FROM %s", tableName, "temp_" + tableName);

	            try (Statement statement = connection.createStatement()) {

	                statement.executeUpdate(sql);

	            } catch (SQLException e) {
	                logger.error(String.format("Creating table from temp_table for sid %d and tablename %s FAILED", sid, tableName), e);

	                throw e;
	            }
	        } catch (SQLException e) {
	            logger.info(String.format("FAILED to Creating table from temp_table for sid %d and tablename %s", sid, tableName));
	            throw e;
	        }
	    }
	    
	    @Override
	    public void createTempTable(String query, int sid, String tableName)
	            throws SQLException {

	        logger.info(String.format("Creating temp table for sid %d and table %s", sid, tableName));

	        try (Connection connection = getConnection()) {

	            try (Statement statement = connection.createStatement()) {

	                statement.executeUpdate(query);

	            } catch (SQLException e) {
	                logger.error(String.format("Creating temp table for sid %d and table %s FAILED", sid, tableName), e);

	                throw e;
	            }
	        } catch (SQLException e) {
	            logger.info(String.format("FAILED to Creating temp table for sid %d and table %s", sid, tableName));
	            throw e;
	        }
	    }
	    
	    @Override
	    public void insertIntoTempTable(String query, int sid, String tableName)
	            throws SQLException {

	        logger.info(String.format("Inserting into temp table temp_%s for sid %d", tableName, sid));

	        try (Connection connection = getConnection()) {

	            try (Statement statement = connection.createStatement()) {

	                statement.executeUpdate(query);

	            } catch (SQLException e) {
	                logger.error(String.format("Inserting into temp table temp_%s for sid %d FAILED", tableName, sid), e);

	                throw e;
	            }
	        } catch (SQLException e) {
	            logger.info(String.format("FAILED to Inserting into temp table temp_%s for sid %d", tableName, sid));
	            throw e;
	        }
	    }
	    
	    // addition here
	    @Override
	    public void createOriginalTable( String query,  int sid,  String tableName)
	            throws SQLException {

	        logger.info(String.format("Creating table for sid %d and table %s", sid, tableName));

	        try (Connection connection = getConnection()) {

	            try (Statement statement = connection.createStatement()) {

	                statement.executeUpdate(query);

	            } catch (SQLException e) {
	                logger.error(String.format("Creating table for sid %d and table %s FAILED", sid, tableName), e);

	                throw e;
	            }
	        } catch (SQLException e) {
	            logger.info(String.format("FAILED to Creating table for sid %d and table %s", sid, tableName));
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
	    public void insertIntoTable(int sid, String tableName, ArrayList<ArrayList<String>> rows, ArrayList<String> columnNames)
	            throws SQLException {

	        logger.info(String.format("New!!!!!!!!!!!!!!!Inserting into table %s for sid %d", tableName, sid));

	        try (Connection connection = getConnection()) {

	            String sql = getSql(tableName, columnNames);

	            try (PreparedStatement statement = connection.prepareStatement(sql)) {
	                final int batchSize = 1000;
	                int count = 0;
	                
	                for(ArrayList<String> row : rows) {
	                    int index = 1;
	                    for(String cell : row) {
	                        statement.setString(index++, cell);
	                    }
	                    statement.addBatch();

	                    if(++count % batchSize == 0) {
	                        statement.executeBatch();
	                    }
	                }
	                statement.executeBatch();

	            } catch (SQLException e) {
	                logger.error(String.format("Inserting into table %s for sid %d FAILED", tableName, sid), e);

	                throw e;
	            }
	        } catch (SQLException e) {
	            logger.info(String.format("FAILED to Inserting into table %s for sid %d", tableName, sid));
	            throw e;
	        }
	    }
	    @Override
	    public void importCsvToTable(final String dir, final String tableName) throws SQLException {
	        
	        logger.info(String.format("Importing from %s to table %s", dir, tableName));

	        try (Connection connection = getConnection()) {

	            String sql = String.format("LOAD DATA LOCAL INFILE '%s' into table %s  fields terminated by ','  optionally enclosed by '\"' escaped by '\"' lines terminated by '\\r\\n'", dir, tableName);

	            try (Statement statement = connection.createStatement()) {
	            System.out.println("****************");
	            System.out.println(sql);
	            System.out.println("****************");
	                statement.executeUpdate(sql);
	                
	            } catch (SQLException e) {
	                logger.error(String.format("Importing from %s to table %s FAILED", dir, tableName), e);

	                throw e;
	            }
	        } catch (SQLException e) {
	            logger.info(String.format("FAILED to Importing from %s to table %s", dir, tableName));
	            throw e;
	        }
	    }
	    
	    private String getSql(final String tableName, final ArrayList<String> columnNames) {
	        String sql = "insert into " + tableName + " values (";
	        String values = "";
	        int size = columnNames.size();

	        for(int i = 0; i < size; i++) {
	            if(i != size - 1) {
	                values += "?, ";
	            } else {
	                values += "?)";
	            }
	        }
	        
	        sql += values;
	        
	        return sql;
	    }

	//...
	    @Override
		public int getCount(final RelationKey relationKey) throws SQLException {
			String sql = String.format("SELECT COUNT(*) as ct from %s", wrapInEscapeChars(relationKey));
			
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
	    private String makeIndexName(final RelationKey relationKey, final String columnName) {
			String indexName = String.format("Index_%s_%s_%s", this.getDatabase(), relationKey.getDbTableName(), columnName);
			
			logger.info(String.format("Generated index name %s", indexName));
			return indexName;
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
			String sql = "";
			
			String indexName = makeIndexName(relationKey, columnName);
			
			if (doesIndexExist(relationKey, indexName)) {
				return;
			}
			
			try (Statement statement = getConnection().createStatement()) {
				//TODO: escape query, SQL injection is possible. See if it is possible to use prepared statement.
				sql = String.format("ALTER TABLE `%s` ADD INDEX `%s` (`%s`(%d));", relationKey.getDbTableName(), indexName, columnName, MYSQL_INDEX_KEY_LENGTH);
				
				statement.executeUpdate(sql);			
			} catch (SQLException e) {
				
				logger.error(String.format("createIndecesIfNotExist FAILED for table %s and column name %s and index name %s", relationKey.getDbTableName(), columnName, indexName), e);
				throw e;
			}
		}
}
