/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.databaseHandlers;

/**
 * @author Evgeny
 *
 */
public abstract class DatabaseHandlerBase {
	private String host;
    private int port;
    private String user;
    private String password;
    private String database;
    private DatabaseHanderType databaseHanderType;
    
    public DatabaseHandlerBase(String host, int port, String user, String password, String database, DatabaseHanderType databaseHanderType) {
    	
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
	 * @return the databaseHanderType
	 */
	public DatabaseHanderType getDatabaseHanderType() {
		return databaseHanderType;
	}
	/**
	 * @param databaseHanderType the databaseHanderType to set
	 */
	public void setDatabaseHanderType(DatabaseHanderType databaseHanderType) {
		this.databaseHanderType = databaseHanderType;
	}
    
	/**
     * Create a database for given name if it doesn't exists yet.
     */
	public abstract void createDatabaseIfNotExist();

	/**
	 * Creates a table where the data should be loaded.
	 * @param tableName the name of the table to create.
	 */
	public abstract void createTableIfNotExist(String tableName);
	
}
