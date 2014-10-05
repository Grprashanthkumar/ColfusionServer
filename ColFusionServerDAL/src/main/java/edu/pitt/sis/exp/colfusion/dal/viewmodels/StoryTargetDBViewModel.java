/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class StoryTargetDBViewModel {
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
	
	public StoryTargetDBViewModel() {
		
	}
	
	public StoryTargetDBViewModel(final int sid, final String serverAddress, final int port, final String userName, final String password, final String databaseName, final String driver,
			final int isLocal, final String linkedServerName) {
		
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
	public void setSid(final int sid) {
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
	public void setServerAddress(final String serverAddress) {
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
	public void setPort(final int port) {
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
	public void setUserName(final String userName) {
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
	public void setPassword(final String password) {
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
	public void setDatabaseName(final String databaseName) {
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
	public void setDriver(final String driver) {
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
	public void setIsLocal(final int isLocal) {
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
	public void setLinkedServerName(final String linkedServerName) {
		this.linkedServerName = linkedServerName;
	}
	
	@Override
	public String toString() {
		
		//TODO: see if JSON serialization could be used here. Because if add more fieds to the class, we would need to update it.
		String result =  String.format("\n sid: %d,\n severAddress: %s,\n port: %d,\n userName: %s,\n password: %s,\n databaseName: %s,\n driver: %s,\n "
				+ "isLocal:%d,\n linkedServerName:%s", sid, serverAddress, port, userName, password, databaseName, driver, isLocal, linkedServerName);
	
		return result;
	}
}
