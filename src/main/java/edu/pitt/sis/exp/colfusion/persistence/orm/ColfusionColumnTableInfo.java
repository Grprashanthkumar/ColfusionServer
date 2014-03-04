package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 5:25:41 PM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionColumnTableInfo generated by hbm2java
 */
public class ColfusionColumnTableInfo implements java.io.Serializable {

	private int cid;
	private ColfusionDnameinfo colfusionDnameinfo;
	private String tableName;

	public ColfusionColumnTableInfo() {
	}

	public ColfusionColumnTableInfo(ColfusionDnameinfo colfusionDnameinfo,
			String tableName) {
		this.colfusionDnameinfo = colfusionDnameinfo;
		this.tableName = tableName;
	}

	public int getCid() {
		return this.cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public ColfusionDnameinfo getColfusionDnameinfo() {
		return this.colfusionDnameinfo;
	}

	public void setColfusionDnameinfo(ColfusionDnameinfo colfusionDnameinfo) {
		this.colfusionDnameinfo = colfusionDnameinfo;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
