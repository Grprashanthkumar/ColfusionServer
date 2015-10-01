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
		} catch (final ClassNotFoundException e) {

			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] MySQLDatabaseHandler failed: Could not load MySQL JDBC driver. Error: %s",
						e.toString()));
			}

			this.logger.error("MySQLDatabaseHandler failed: Could not load MySQL JDBC driver", e);
			throw e;
		}
	}

	@Override
	public String getConnectionString() {
		return String.format("jdbc:mysql://%s:%d/%s", getHost(), getPort(), getDatabase());
	}

	@Override
	public boolean createDatabaseIfNotExist(final String databaseName) throws Exception {
		String sql = "";

		try (Statement statement = getConnection().createStatement()){

			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			sql = String.format("CREATE DATABASE IF NOT EXISTS %s", wrapName(databaseName));

			statement.executeUpdate(sql);

			close();

			setDatabase(databaseName);

			openConnection();

			return true;
		} catch (final SQLException e) {

			if (this.executionInfoMgr != null) {
				this.executionInfoMgr.appendLog(this.executionLogId, String.format("[ERROR] createTableIfNotExist failed for database %s when executing this query %s. Error: %s",
						databaseName, sql, e.toString()));
			}

			this.logger.error(String.format("createDatabaseIfNotExist failed for %s", databaseName));
			throw e;
		}
	}

	@Override
	public void createTableIfNotExist(final RelationKey tableInfo, final List<String> variables) throws Exception {
		final LinkedHashMap<String, String> columnNameToType = new LinkedHashMap<String, String>();

		for(final String variable : variables) {
			columnNameToType.put(variable, "TEXT");
		}

		createTableIfNotExistInternal(tableInfo, columnNameToType);
	}

	@Override
	public void createTableIfNotExist(final RelationKey tableTo,
			final ResultSetMetaData tableMetadata) throws Exception {
		//The order in key insertion is important.
		final LinkedHashMap<String, String> columnNameToType = new LinkedHashMap<String, String>();

		for (int i = 1; i <= tableMetadata.getColumnCount(); i++) {

			final String columnType = tableMetadata.getColumnTypeName(i);

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

		createTableIfNotExistInternal(tableTo, columnNameToType);
	}

	private void createTableIfNotExistInternal(final RelationKey tableInfo, final LinkedHashMap<String, String> columnNameToType) throws Exception {
		final String sql = constructCreateTableQuery(tableInfo, columnNameToType);

		try {
			executeUpdate(sql);
		} catch (final SQLException e) {

			if (this.executionInfoMgr != null) {
				this.executionInfoMgr.appendLog(this.executionLogId, String.format("[ERROR] createTableIfNotExist failed for table %s when executing this query %s. Error: %s",
						tableInfo.getDbTableName(), sql, e.toString()));
			}

			this.logger.error(String.format("createTableIfNotExist failed for %s. Error Message:", tableInfo.getDbTableName(), e.toString()));
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
	private String constructCreateTableQuery(final RelationKey tableInfo,
			final LinkedHashMap<String, String> columnNameToType) {
		//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
		final StringBuilder sqlBuilder = new StringBuilder();

		sqlBuilder.append(String.format("CREATE TABLE IF NOT EXISTS `%s` (", tableInfo.getDbTableName()));

		final int numberOfColumns = columnNameToType.size();
		int index = 0;

		for (final Entry<String, String> entry : columnNameToType.entrySet()) {

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

		try {
			final Statement statement = getConnection().createStatement();

			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			sql = String.format("DROP DATABASE IF EXISTS `%s`", databaseName);

			statement.executeUpdate(sql);

			close();

			setDatabase("");

			openConnection();

			return true;
		} catch (final SQLException e) {

			if (this.executionInfoMgr != null) {
				this.executionInfoMgr.appendLog(this.executionLogId, String.format("[ERROR] deleteDatabaseIfNotExist failed for database %s when executing this query %s. Error: %s",
						databaseName, sql, e.toString()));
			}

			this.logger.error(String.format("createDatabaseIfNotExist FAILED for %s", databaseName));
			throw e;
		}
	}

	@Override
	protected String wrapSQLIntoLimit(final String sqlString, final int perPage,
			final int pageNumber) {
		final int startPoint = (pageNumber - 1) * perPage;

		return String.format("%s LIMIT %d, %d", sqlString, startPoint, perPage);
	}

	@Override
	public boolean tempTableExist(final int sid, final RelationKey table)
			throws SQLException {
		this.logger.info(String.format("Getting if temp table exists for sid %d and tablename %s", sid, table.getDbTableName()));

		try {
			final Connection connection = getConnection();

			final String sql = String.format("SHOW TABLES LIKE 'temp_%s'", table.getDbTableName());

			try (Statement statement = connection.createStatement()) {

				final ResultSet rs = statement.executeQuery(sql);
				if (rs.next()) {
					return true;
				} else {
					return false;
				}

			} catch (final SQLException e) {
				this.logger.error(String.format("Getting if temp table exists for sid %d and tablename %s FAILED", sid,
						table.getDbTableName()), e);

				throw e;
			}
		} catch (final SQLException e) {
			this.logger.info(String.format("FAILED to getting if temp table exists for sid %d and tablename %s", sid,
					table.getDbTableName()));
			throw e;
		}
	}

	@Override
	public void removeTable(final String tableName)
			throws SQLException {
		this.logger.info(String.format("Removing table for sid %d and tablename %s", getSid(), tableName));

		final String sql = String.format("DROP TABLE IF EXISTS %s", wrapName(tableName));

		try {
			executeUpdate(sql);
		}
		catch (final SQLException e) {
			this.logger.error(String.format("Removing table for sid %d and tablename %s FAILED", getSid(), tableName), e);

			throw e;
		}
	}

	@Override
	public int getColCount(final int sid, final RelationKey tableInfo) throws SQLException {
		this.logger.info(String.format("Getting column count for sid %d", sid));

		try {
			final Connection connection = getConnection();

			final String sql = "select count(*) from information_schema.columns where table_schema= ? and table_name= ?";

			try (PreparedStatement statement = connection.prepareStatement(sql)) {
				statement.setString(1, "colfusion_fileToDB_" + sid);
				statement.setString(2, tableInfo.getDbTableName());

				int colCount = 0;
				final ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					colCount = Integer.parseInt(rs.getString(1));
				}
				return colCount;

			} catch (final SQLException e) {
				this.logger.error(String.format("Getting column count for sid %d FAILED", sid), e);

				throw e;
			}
		} catch (final SQLException e) {
			this.logger.info(String.format("FAILED to Getting column count for sid %d", sid));
			throw e;
		}
	}

	@Override
	public ArrayList<ArrayList<String>> getRows(final RelationKey tableInfo, final int colCount) throws SQLException {
		this.logger.info(String.format("Getting rows for table %s", tableInfo.getDbTableName()));
		final ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		try {
			final Connection connection = getConnection();

			//TODO:use prepared statement
			final String sql = String.format("select * from %s", wrapName(tableInfo.getDbTableName()));

			try (Statement statement = connection.createStatement()) {

				final ResultSet rs1 = statement.executeQuery(sql);
				while (rs1.next()) {
					int colIndex = 1;
					final ArrayList<String> temp = new ArrayList<String>();
					while (colIndex <= colCount) {
						temp.add(rs1.getString(colIndex));
						colIndex++;
					}
					rows.add(temp);
				}
				return rows;

			} catch (final SQLException e) {
				this.logger.error(String.format("Getting rows for table %s FAILED", tableInfo.getDbTableName()), e);

				throw e;
			}
		} catch (final SQLException e) {
			this.logger.info(String.format("FAILED to getting rows for table %s", tableInfo.getDbTableName()));
			throw e;
		}
	}

	@Override
	public void createTableFromTable(final String tableNameToCreate, final String tableNameFromWhichToCreate)
			throws SQLException {

		final String message = String.format("Creating table %s from %s for sid %d", tableNameToCreate, tableNameFromWhichToCreate, getSid());
		this.logger.info(message);

		final String sql = String.format("CREATE TABLE %s SELECT * FROM %s", wrapName(tableNameToCreate), wrapName(tableNameFromWhichToCreate));

		try {
			executeUpdate(sql);
		} catch (final SQLException e) {
			this.logger.error("FAILED " + message, e);

			throw e;
		}
	}

	@Override
	public void insertIntoTable(final String query, final int sid, final String tableName)
			throws SQLException {

		this.logger.info(String.format("Inserting into table temp_%s for sid %d", tableName, sid));

		try {
			final Connection connection = getConnection();

			try (Statement statement = connection.createStatement()) {

				statement.executeUpdate(query);

			} catch (final SQLException e) {
				this.logger.error(String.format("Inserting into table temp_%s for sid %d FAILED", tableName, sid), e);

				throw e;
			}
		} catch (final SQLException e) {
			this.logger.info(String.format("FAILED to Inserting into table temp_%s for sid %d", tableName, sid));
			throw e;
		}
	}

	@Override
	public void importCsvToTable(final String dir, final RelationKey tableInfo) throws SQLException {
		final String message = String.format("Importing from %s to table %s", dir, tableInfo.getDbTableName());
		this.logger.info(message);

		final String sql = String.format("LOAD DATA LOCAL INFILE '%s' into table %s  fields terminated by ','  optionally enclosed by '\"' escaped by '\"' lines terminated by '\\r\\n'",
				dir, wrapName(tableInfo.getDbTableName()));

		try {
			executeUpdate(sql);
		} catch (final SQLException e) {
			this.logger.error("FAILED " + message, e);

			throw e;
		}
	}

	@Override
	public int getCount(final RelationKey relationKey) throws SQLException {
		final String sql = String.format("SELECT COUNT(*) as ct from %s", wrapName(relationKey.getDbTableName()));

		try (Statement statement = getConnection().createStatement()) {

			final ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				return rs.getInt("ct");
			}

			return 0;
		} catch (final SQLException e) {
			this.logger.error(String.format("getAllColumnsInTable FAILED on table '%s'", relationKey.getDbTableName()), e);

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
		final String indexName = String.format("Index_%s_%s_%s", this.getDatabase(), relationKey.getDbTableName(), columnName);

		final String indexShortName = StringUtils.makeShortUnique(indexName, INDEX_NAME_MAX_LENGTH);

		this.logger.info(String.format("Generated index name %s", indexShortName));
		return indexShortName;
	}

	@Override
	public List<String> getAllColumnsInTable(final RelationKey relationKey) throws SQLException {
		this.logger.info(String.format("Getting all columns in table '%s'", relationKey.getDbTableName()));

		final String sql = "SELECT `COLUMN_NAME` as columnName FROM `INFORMATION_SCHEMA`.`COLUMNS` WHERE `TABLE_SCHEMA`= ? AND `TABLE_NAME`= ?";

		try (java.sql.PreparedStatement statement = getConnection().prepareStatement(sql)) {
			statement.setString(1, this.getDatabase());
			statement.setString(2, relationKey.getDbTableName());

			final ResultSet rs = statement.executeQuery();
			final List<String> result = new ArrayList<String>();
			while (rs.next()) {
				result.add(rs.getString("columnName"));
			}

			this.logger.info(String.format("Got %d columns in table '%s'", result.size(), relationKey.getDbTableName()));

			return result;
		} catch (final SQLException e) {
			this.logger.error(String.format("getAllColumnsInTable FAILED on table '%s'", relationKey.getDbTableName()), e);

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

		this.logger.info(String.format("Checking if an index exists with name %s for table %s", indexName, relationKey.getDbTableName()));

		final String sql = "SELECT COUNT(*) as cnt FROM INFORMATION_SCHEMA.STATISTICS WHERE table_name = ? and index_name = ?";

		try (java.sql.PreparedStatement statement = getConnection().prepareStatement(sql)) {
			statement.setString(1, relationKey.getDbTableName());
			statement.setString(2, indexName);

			final ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return rs.getInt("cnt") != 0;
			}

			return false;
		} catch (final SQLException e) {
			this.logger.error(String.format("doesIndexExist FAILED on table %s and index name %s", relationKey.getDbTableName(), indexName), e);

			throw e;
		}
	}

	@Override
	public void createIndecesIfNotExist(final RelationKey relationKey, final String columnName) throws SQLException {
		final String indexName = makeIndexName(relationKey, columnName);

		if (doesIndexExist(relationKey, indexName)) {
			return;
		}

		final String sql = String.format("ALTER TABLE %s ADD INDEX %s (%s(%d));",
				wrapName(relationKey.getDbTableName()), wrapName(indexName), wrapName(columnName), MYSQL_INDEX_KEY_LENGTH);

		try {
			executeUpdate(sql);
		} catch (final SQLException e) {

			this.logger.error(String.format("createIndecesIfNotExist FAILED for table %s and column name %s and index name %s", relationKey.getDbTableName(), columnName, indexName), e);
			throw e;
		}
	}

	@Override
	public String makeInsertPreparedSQL(final RelationKey tableName, final ResultSetMetaData metaData) throws SQLException {
		final StringBuilder result = new StringBuilder("INSERT INTO ").append(wrapName(tableName.getDbTableName())).append(" VALUES (");
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
