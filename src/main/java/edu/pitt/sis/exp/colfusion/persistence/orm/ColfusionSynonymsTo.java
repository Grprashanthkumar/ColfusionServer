package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated 2014-6-10 16:17:59 by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionSynonymsTo generated by hbm2java
 */
public class ColfusionSynonymsTo implements java.io.Serializable {

	private ColfusionSynonymsToId id;
	private ColfusionSourceinfo colfusionSourceinfo;
	private ColfusionUsers colfusionUsers;

	public ColfusionSynonymsTo() {
	}

	public ColfusionSynonymsTo(ColfusionSynonymsToId id,
			ColfusionSourceinfo colfusionSourceinfo,
			ColfusionUsers colfusionUsers) {
		this.id = id;
		this.colfusionSourceinfo = colfusionSourceinfo;
		this.colfusionUsers = colfusionUsers;
	}

	public ColfusionSynonymsToId getId() {
		return this.id;
	}

	public void setId(ColfusionSynonymsToId id) {
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
