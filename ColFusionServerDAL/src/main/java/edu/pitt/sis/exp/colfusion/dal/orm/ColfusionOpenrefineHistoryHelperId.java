package edu.pitt.sis.exp.colfusion.dal.orm;

// Generated Dec 13, 2014 7:22:59 PM by Hibernate Tools 4.0.0

/**
 * ColfusionOpenrefineHistoryHelperId generated by hbm2java
 */
public class ColfusionOpenrefineHistoryHelperId implements java.io.Serializable {

	private int sid;
	private String tableName;
	private int count;
	private int isSaved;

	public ColfusionOpenrefineHistoryHelperId() {
	}

	public ColfusionOpenrefineHistoryHelperId(int sid, String tableName,
			int count, int isSaved) {
		this.sid = sid;
		this.tableName = tableName;
		this.count = count;
		this.isSaved = isSaved;
	}

	public int getSid() {
		return this.sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getIsSaved() {
		return this.isSaved;
	}

	public void setIsSaved(int isSaved) {
		this.isSaved = isSaved;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ColfusionOpenrefineHistoryHelperId))
			return false;
		ColfusionOpenrefineHistoryHelperId castOther = (ColfusionOpenrefineHistoryHelperId) other;

		return (this.getSid() == castOther.getSid())
				&& ((this.getTableName() == castOther.getTableName()) || (this
						.getTableName() != null
						&& castOther.getTableName() != null && this
						.getTableName().equals(castOther.getTableName())))
				&& (this.getCount() == castOther.getCount())
				&& (this.getIsSaved() == castOther.getIsSaved());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getSid();
		result = 37 * result
				+ (getTableName() == null ? 0 : this.getTableName().hashCode());
		result = 37 * result + this.getCount();
		result = 37 * result + this.getIsSaved();
		return result;
	}

}