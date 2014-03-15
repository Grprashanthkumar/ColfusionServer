/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class StoryTargetDB {
	private int sid;
	private String serverAddress;
	private int port;
	private String userName;
	private String password;
	private String databaseName;
	/**
	 * The vendor of the data store.
	 */
	private String driver;
	private int isLocal;
	private String linkedServerName;
	
	public StoryTargetDB() {
		
	}
	
	public StoryTargetDB(int sid, String serverAddress, int port, String userName, String password, String databaseName, String driver,
			int isLocal, String linkedServerName) {
		
		setSid(sid);
		setServerAddress(serverAddress);
		setPort(port);
		setUserName(userName);
		setPassword(password);
		setDatabaseName(databaseName);
		setDriver(driver);
		setIsLocal(isLocal);
		setLinkedServerName(linkedServerName);
		
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
	public void setSid(int sid) {
		this.sid = sid;
	}
	/**
	 * @return the serverAddress
	 */
	public String getServerAddress() {
		return serverAddress;
	}
	/**
	 * @param serverAddress the serverAddress to set
	 */
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	/**
	 * @param databaseName the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}
	/**
	 * @param driver the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}
	/**
	 * @return the is_local
	 */
	public int getIsLocal() {
		return isLocal;
	}
	/**
	 * @param is_local the is_local to set
	 */
	public void setIsLocal(int isLocal) {
		this.isLocal = isLocal;
	}
	/**
	 * @return the linkedServerName
	 */
	public String getLinkedServerName() {
		return linkedServerName;
	}
	/**
	 * @param linkedServerName the linkedServerName to set
	 */
	public void setLinkedServerName(String linkedServerName) {
		this.linkedServerName = linkedServerName;
	}
}
