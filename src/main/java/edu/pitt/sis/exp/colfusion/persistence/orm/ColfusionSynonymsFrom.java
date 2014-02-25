package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 25, 2014 9:55:27 AM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionSynonymsFrom generated by hbm2java
 */
public class ColfusionSynonymsFrom implements java.io.Serializable {

	private ColfusionSynonymsFromId id;
	private ColfusionSourceinfo colfusionSourceinfo;
	private ColfusionUsers colfusionUsers;

	public ColfusionSynonymsFrom() {
	}

	public ColfusionSynonymsFrom(ColfusionSynonymsFromId id,
			ColfusionSourceinfo colfusionSourceinfo,
			ColfusionUsers colfusionUsers) {
		this.id = id;
		this.colfusionSourceinfo = colfusionSourceinfo;
		this.colfusionUsers = colfusionUsers;
	}

	public ColfusionSynonymsFromId getId() {
		return this.id;
	}

	public void setId(ColfusionSynonymsFromId id) {
		this.id = id;
	}

	public ColfusionSourceinfo getColfusionSourceinfo() {
		return this.colfusionSourceinfo;
	}

	public void setColfusionSourceinfo(ColfusionSourceinfo colfusionSourceinfo) {
		this.colfusionSourceinfo = colfusionSourceinfo;
	}

	public ColfusionUsers getColfusionUsers() {
		return this.colfusionUsers;
	}

	public void setColfusionUsers(ColfusionUsers colfusionUsers) {
		this.colfusionUsers = colfusionUsers;
	}

}
