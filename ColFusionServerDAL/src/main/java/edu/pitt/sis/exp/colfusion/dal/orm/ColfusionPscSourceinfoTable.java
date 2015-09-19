package edu.pitt.sis.exp.colfusion.dal.orm;

// Generated Dec 3, 2014 8:12:54 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * ColfusionPscSourceinfoTable generated by hbm2java
 */
public class ColfusionPscSourceinfoTable implements java.io.Serializable {

	private ColfusionPscSourceinfoTableId id;
	private ColfusionSourceinfo colfusionSourceinfo;
	private ColfusionProcesses colfusionProcesses;
	
	private String pscHost;
	private int pscDatabasePort;
	private String pscDatabaseName;
	private String pscTableName;
	
	private String pscDatabaseUser;
	private String pscDatabasePassword;
	private String pscDatabaseVendor;
	
	private Date whenReplicationStarted;
	private Date whenReplicationFinished;

	public ColfusionPscSourceinfoTable() {
	}

	public ColfusionPscSourceinfoTable(final ColfusionPscSourceinfoTableId id,
			final ColfusionSourceinfo colfusionSourceinfo, final String pscDatabaseName,
			final String pscTableName, final String pscHost, final int pscDatabasePort, 
			final String pscDatabaseUser, final String pscDatabasePassword, final String pscDatabaseVendor) {
		this.id = id;
		this.colfusionSourceinfo = colfusionSourceinfo;
		this.pscDatabaseName = pscDatabaseName;
		this.pscTableName = pscTableName;
		
		this.pscHost = pscHost;
		this.pscDatabasePort = pscDatabasePort;
		this.pscDatabaseUser = pscDatabaseUser;
		this.pscDatabasePassword = pscDatabasePassword;
		this.pscDatabaseVendor = pscDatabaseVendor;
	}

	public ColfusionPscSourceinfoTable(final ColfusionPscSourceinfoTableId id,
			final ColfusionSourceinfo colfusionSourceinfo,
			final ColfusionProcesses colfusionProcesses, final String pscDatabaseName,
			final String pscTableName, final String pscHost, final int pscDatabasePort, 
			final String pscDatabaseUser, final String pscDatabasePassword, final String pscDatabaseVendor,
			final Date whenReplicationStarted,
			final Date whenReplicationFinished) {
		this.id = id;
		this.colfusionSourceinfo = colfusionSourceinfo;
		this.colfusionProcesses = colfusionProcesses;
		this.pscDatabaseName = pscDatabaseName;
		this.pscTableName = pscTableName;
		
		this.pscHost = pscHost;
		this.pscDatabasePort = pscDatabasePort;
		this.pscDatabaseUser = pscDatabaseUser;
		this.pscDatabasePassword = pscDatabasePassword;
		this.pscDatabaseVendor = pscDatabaseVendor;
		
		this.whenReplicationStarted = whenReplicationStarted;
		this.whenReplicationFinished = whenReplicationFinished;
	}

	public ColfusionPscSourceinfoTableId getId() {
		return this.id;
	}

	public void setId(final ColfusionPscSourceinfoTableId id) {
		this.id = id;
	}

	public ColfusionSourceinfo getColfusionSourceinfo() {
		return this.colfusionSourceinfo;
	}

	public void setColfusionSourceinfo(final ColfusionSourceinfo colfusionSourceinfo) {
		this.colfusionSourceinfo = colfusionSourceinfo;
	}

	public ColfusionProcesses getColfusionProcesses() {
		return this.colfusionProcesses;
	}

	public void setColfusionProcesses(final ColfusionProcesses colfusionProcesses) {
		this.colfusionProcesses = colfusionProcesses;
	}

	public String getPscDatabaseName() {
		return this.pscDatabaseName;
	}

	public void setPscDatabaseName(final String pscDatabaseName) {
		this.pscDatabaseName = pscDatabaseName;
	}

	public String getPscTableName() {
		return this.pscTableName;
	}

	public void setPscTableName(final String pscTableName) {
		this.pscTableName = pscTableName;
	}

	public Date getWhenReplicationStarted() {
		return this.whenReplicationStarted;
	}

	public void setWhenReplicationStarted(final Date whenReplicationStarted) {
		this.whenReplicationStarted = whenReplicationStarted;
	}

	public Date getWhenReplicationFinished() {
		return this.whenReplicationFinished;
	}

	public void setWhenReplicationFinished(final Date whenReplicationFinished) {
		this.whenReplicationFinished = whenReplicationFinished;
	}

	/**
	 * @return the pscHost
	 */
	public String getPscHost() {
		return pscHost;
	}

	/**
	 * @param pscHost the pscHost to set
	 */
	public void setPscHost(final String pscHost) {
		this.pscHost = pscHost;
	}

	/**
	 * @return the pscDatabasePort
	 */
	public int getPscDatabasePort() {
		return pscDatabasePort;
	}

	/**
	 * @param pscDatabasePort the pscDatabasePort to set
	 */
	public void setPscDatabasePort(final int pscDatabasePort) {
		this.pscDatabasePort = pscDatabasePort;
	}

	/**
	 * @return the pscDatabaseUser
	 */
	public String getPscDatabaseUser() {
		return pscDatabaseUser;
	}

	/**
	 * @param pscDatabaseUser the pscDatabaseUser to set
	 */
	public void setPscDatabaseUser(final String pscDatabaseUser) {
		this.pscDatabaseUser = pscDatabaseUser;
	}

	/**
	 * @return the pscDatabasePassword
	 */
	public String getPscDatabasePassword() {
		return pscDatabasePassword;
	}

	/**
	 * @param pscDatabasePassword the pscDatabasePassword to set
	 */
	public void setPscDatabasePassword(final String pscDatabasePassword) {
		this.pscDatabasePassword = pscDatabasePassword;
	}

	/**
	 * @return the pscDatabaseVendor
	 */
	public String getPscDatabaseVendor() {
		return pscDatabaseVendor;
	}

	/**
	 * @param pscDatabaseVendor the pscDatabaseVendor to set
	 */
	public void setPscDatabaseVendor(final String pscDatabaseVendor) {
		this.pscDatabaseVendor = pscDatabaseVendor;
	}

}