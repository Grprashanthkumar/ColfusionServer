package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 17, 2014 8:18:50 PM by Hibernate Tools 3.4.0.CR1

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