package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated 2014-6-10 16:17:59 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * ColfusionDnameinfoMetadataEditHistory generated by hbm2java
 */
public class ColfusionDnameinfoMetadataEditHistory implements
		java.io.Serializable {

	private Integer hid;
	private ColfusionDnameinfo colfusionDnameinfo;
	private ColfusionUsers colfusionUsers;
	private Date whenSaved;
	private String editedAttribute;
	private String reason;
	private String value;

	public ColfusionDnameinfoMetadataEditHistory() {
	}

	public ColfusionDnameinfoMetadataEditHistory(
			ColfusionDnameinfo colfusionDnameinfo,
			ColfusionUsers colfusionUsers, Date whenSaved,
			String editedAttribute, String value) {
		this.colfusionDnameinfo = colfusionDnameinfo;
		this.colfusionUsers = colfusionUsers;
		this.whenSaved = whenSaved;
		this.editedAttribute = editedAttribute;
		this.value = value;
	}

	public ColfusionDnameinfoMetadataEditHistory(
			ColfusionDnameinfo colfusionDnameinfo,
			ColfusionUsers colfusionUsers, Date whenSaved,
			String editedAttribute, String reason, String value) {
		this.colfusionDnameinfo = colfusionDnameinfo;
		this.colfusionUsers = colfusionUsers;
		this.whenSaved = whenSaved;
		this.editedAttribute = editedAttribute;
		this.reason = reason;
		this.value = value;
	}

	public Integer getHid() {
		return this.hid;
	}

	public void setHid(Integer hid) {
		this.hid = hid;
	}

	public ColfusionDnameinfo getColfusionDnameinfo() {
		return this.colfusionDnameinfo;
	}

	public void setColfusionDnameinfo(ColfusionDnameinfo colfusionDnameinfo) {
		this.colfusionDnameinfo = colfusionDnameinfo;
	}

	public ColfusionUsers getColfusionUsers() {
		return this.colfusionUsers;
	}

	public void setColfusionUsers(ColfusionUsers colfusionUsers) {
		this.colfusionUsers = colfusionUsers;
	}

	public Date getWhenSaved() {
		return this.whenSaved;
	}

	public void setWhenSaved(Date whenSaved) {
		this.whenSaved = whenSaved;
	}

	public String getEditedAttribute() {
		return this.editedAttribute;
	}

	public void setEditedAttribute(String editedAttribute) {
		this.editedAttribute = editedAttribute;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
