/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;
import edu.pitt.sis.exp.colfusion.persistence.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDB;

//TODO: should probably be in a separate package.

/**
 * @author Evgeny
 *
 */
public class LinkedServerHandler {
	
	final Logger logger = LogManager.getLogger(LinkedServerHandler.class.getName());
	
	private String host;
    private int port;
    private String user;
    private String password;
    private String database;
	
	protected Connection connection;
	
	private ExecutionInfoManager executionInfoMgr; 
	private int executionLogId;
	
	public LinkedServerHandler(String host, int port, String user,
			String password, String database, ExecutionInfoManager executionInfoMgr, int executionLogId) throws Exception {
		
		setHost(host);
    	setPort(port);
    	setUser(user);
    	setPassword(password);
    	setDatabase(database);
    	
    	//TODO: maybe this is not the best approach, maybe we should  just utilize logger and log into db
    	this.executionInfoMgr = executionInfoMgr;
    	this.executionLogId = executionLogId;
		
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] MySQLDatabaseHandler failed: Could not load MS SQL JDBC driver. %s", e.toString()));
			}
			
			logger.error("MySQLDatabaseHandler failed: Could not load MS SQL JDBC driver", e);
			throw e;
		}
		
		openConnection(getConnectionString());
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
	public void setHost(String host) {
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
	public void setPort(int port) {
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
	public void setUser(String user) {
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
	public void setPassword(String password) {
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
	public void setDatabase(String database) {
		this.database = database;
	}
	
	/**
	 * Creates a connection string and return it.
	 * @return connection string.
	 */
	public String getConnectionString() {
		return String.format("jdbc:sqlserver://%s:%d;DatabaseName=%s", getHost(), getPort(), getDatabase());
	}
	
	
	/**
	 * Initializes the connection.
	 * @return the connection.
	 * @throws Exception 
	 */
	protected void openConnection(String connectionString) throws Exception {
		try {
			connection = DriverManager.getConnection(connectionString, getUser(), getPassword());
			
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] openConnection failed: Could not get connection. "
					+ "Connection string: %s. User: %s. Password: %s. Error message: %s", connectionString, getUser(), getPassword(), e.toString()));
			}
			
			logger.error("getConnection failed in LinkedServerHandler", e);
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

	/**
	 * Adds or updates lined server for given target database.
	 * @param sourceDBInfo connection info to target database.
	 * @throws Exception 
	 */
	public void addOrUpdateLinkedServer(StoryTargetDB sourceDBInfo) throws Exception {
	
		//TODO: this is hack to test on local machine on Mac
		if (sourceDBInfo.getServerAddress().equals("localhost")) {
			sourceDBInfo.setServerAddress(ConfigManager.getInstance().getPropertyByName(PropertyKeys.linkedServerColFusionHost));
		}
		
		dropLinkedServerIfExists(sourceDBInfo.getLinkedServerName()); 
		
		String sql = prepareAddLinkedServerSQLString(sourceDBInfo);
		
		try {
			if (addLinkedServer(sql)) {
				addLinkedServerLogin(sourceDBInfo);
			}
		} catch (Exception e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] addOrUpdateLinkedServer failed. "
					+ "Target database: %s. Error message: %s", sourceDBInfo.toString(), e.toString()));
			}
			
			throw e;
		}
	}

	private void addLinkedServerLogin(StoryTargetDB sourceDBInfo) throws Exception {
		
		Statement statement = null;
		
		String sql = "";
		
		try {
			statement = connection.createStatement();
			
			sql = String.format("exec sp_addlinkedsrvlogin @rmtsrvname = N'%s', @locallogin = N'remoteUserTest', @rmtuser = N'%s', "
					+ "@rmtpassword = N'%s', @useself = N'False';", sourceDBInfo.getLinkedServerName(), sourceDBInfo.getUserName(), sourceDBInfo.getPassword());
			
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] addLinkedServerLogin failed when execution this query: "
					+ "Qeury: %s. Error message: %s", sql, e.toString()));
			}
			
			logger.error(String.format("addLinkedServerLogin failed when execution this query: %s", sql));
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

	private boolean addLinkedServer(String sql) throws Exception {
		Statement statement = null;
		
		try {
			statement = connection.createStatement();
			
			statement.executeUpdate(sql);
			
			return true;
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] addLinkedServer failed when execution this query: "
					+ "Qeury: %s. Error message: %s", sql, e.toString()));
			}
			
			logger.error(String.format("addLinkedServer failed when execution this query: %s", sql));
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

	private String prepareAddLinkedServerSQLString(StoryTargetDB sourceDBInfo) throws Exception {
		DatabaseHanderType vendor = DatabaseHanderType.fromString(sourceDBInfo.getDriver());
		
		String srvproduct = "";
		String provider = "";
		String provstr = "";
		
		switch (vendor) {
			case MYSQL:
				srvproduct = "MySQL";
                provider = "MSDASQL";
                provstr = String.format("DRIVER={MySQL ODBC 5.2 Unicode Driver}; SERVER=%s;PORT=%d;DATABASE=%s;USER=%s;PASSWORD=%s;OPTION=3;",
                		sourceDBInfo.getServerAddress(), sourceDBInfo.getPort(), sourceDBInfo.getDatabaseName(), sourceDBInfo.getUserName(), sourceDBInfo.getPassword());
                
                break;
 
			case POSTGRESQL:
				
				srvproduct = "PostgreSQL";
                provider = "MSDASQL";
                provstr = String.format("DRIVER={PostgreSQL Unicode(x64)}; SERVER=%s;PORT=%d;DATABASE=%s;Uid=%s;Pwd=%s;",
                		sourceDBInfo.getServerAddress(), sourceDBInfo.getPort(), sourceDBInfo.getDatabaseName(), sourceDBInfo.getUserName(), sourceDBInfo.getPassword());
				
				break;
				
			default:
				
				if (executionInfoMgr != null) {
					executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] DatabaseHandler type not found in prepareAddLinkedServerSQLString. Driver: %s ",
						vendor));
				}
				
				logger.error("DatabaseHandler type not found in addOrUpdateLinkedServer", vendor);
				
				//TODO, FIXME: throw proper exception
				throw new Exception("DatabaseHandler type not found in addOrUpdateLinkedServer");
		}
		
		return String.format("exec sp_addlinkedserver @server = N'%s', @srvproduct = N'%s', @provider = N'%s', @provstr =N'%s';", 
				sourceDBInfo.getLinkedServerName(), srvproduct, provider, provstr);
	}

	private void dropLinkedServerIfExists(String linkedServerName) throws Exception {
		Statement statement = null;
		
		String sql = "";
		
		try {
			statement = connection.createStatement();
			//TODO: is SQL injection possible here? because we just put database name without any checks and the database name can come from user (or not?)
			sql = String.format("IF EXISTS (SELECT srv.name FROM sys.servers srv WHERE srv.server_id != 0 AND srv.name = N'%s')"
					+ " EXEC master.dbo.sp_dropserver @server=N'%s', @droplogins='droplogins'", linkedServerName, linkedServerName);
			
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] dropLinkedServerIfExists failed when trying to drop %s linked server by executing this query: %s",
					linkedServerName, sql));
			}
			
			logger.error(String.format("dropLinkedServerIfExists failed for %s", linkedServerName));
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
