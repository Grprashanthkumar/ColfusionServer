package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 25, 2014 9:55:27 AM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionDnameMetaData generated by hbm2java
 */
public class ColfusionDnameMetaData implements java.io.Serializable {

	private Integer metaId;
	private ColfusionDnameinfo colfusionDnameinfo;
	private String type;
	private String value;
	private int sid;

	public ColfusionDnameMetaData() {
	}

	public ColfusionDnameMetaData(ColfusionDnameinfo colfusionDnameinfo,
			String type, String value, int sid) {
		this.colfusionDnameinfo = colfusionDnameinfo;
		this.type = type;
		this.value = value;
		this.sid = sid;
	}

	public Integer getMetaId() {
		return this.metaId;
	}

	public void setMetaId(Integer metaId) {
		this.metaId = metaId;
	}

	public ColfusionDnameinfo getColfusionDnameinfo() {
		return this.colfusionDnameinfo;
	}

	public void setColfusionDnameinfo(ColfusionDnameinfo colfusionDnameinfo) {
		this.colfusionDnameinfo = colfusionDnameinfo;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getSid() {
		return this.sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

}
