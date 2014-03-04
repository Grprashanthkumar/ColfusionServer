package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 5:25:41 PM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionSourceinfoUserId generated by hbm2java
 */
public class ColfusionSourceinfoUserId implements java.io.Serializable {

	private int sid;
	private int uid;
	private int rid;

	public ColfusionSourceinfoUserId() {
	}

	public ColfusionSourceinfoUserId(int sid, int uid, int rid) {
		this.sid = sid;
		this.uid = uid;
		this.rid = rid;
	}

	public int getSid() {
		return this.sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getUid() {
		return this.uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getRid() {
		return this.rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ColfusionSourceinfoUserId))
			return false;
		ColfusionSourceinfoUserId castOther = (ColfusionSourceinfoUserId) other;

		return (this.getSid() == castOther.getSid())
				&& (this.getUid() == castOther.getUid())
				&& (this.getRid() == castOther.getRid());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSid();
		result = 37 * result + this.getUid();
		result = 37 * result + this.getRid();
		return result;
	}

}
