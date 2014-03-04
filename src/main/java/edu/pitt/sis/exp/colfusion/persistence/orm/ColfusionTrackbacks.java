package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 5:25:41 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * ColfusionTrackbacks generated by hbm2java
 */
public class ColfusionTrackbacks implements java.io.Serializable {

	private Integer trackbackId;
	private int trackbackLinkId;
	private int trackbackUserId;
	private String trackbackType;
	private String trackbackStatus;
	private Date trackbackModified;
	private Date trackbackDate;
	private String trackbackUrl;
	private String trackbackTitle;
	private String trackbackContent;

	public ColfusionTrackbacks() {
	}

	public ColfusionTrackbacks(int trackbackLinkId, int trackbackUserId,
			Date trackbackModified) {
		this.trackbackLinkId = trackbackLinkId;
		this.trackbackUserId = trackbackUserId;
		this.trackbackModified = trackbackModified;
	}

	public ColfusionTrackbacks(int trackbackLinkId, int trackbackUserId,
			String trackbackType, String trackbackStatus,
			Date trackbackModified, Date trackbackDate, String trackbackUrl,
			String trackbackTitle, String trackbackContent) {
		this.trackbackLinkId = trackbackLinkId;
		this.trackbackUserId = trackbackUserId;
		this.trackbackType = trackbackType;
		this.trackbackStatus = trackbackStatus;
		this.trackbackModified = trackbackModified;
		this.trackbackDate = trackbackDate;
		this.trackbackUrl = trackbackUrl;
		this.trackbackTitle = trackbackTitle;
		this.trackbackContent = trackbackContent;
	}

	public Integer getTrackbackId() {
		return this.trackbackId;
	}

	public void setTrackbackId(Integer trackbackId) {
		this.trackbackId = trackbackId;
	}

	public int getTrackbackLinkId() {
		return this.trackbackLinkId;
	}

	public void setTrackbackLinkId(int trackbackLinkId) {
		this.trackbackLinkId = trackbackLinkId;
	}

	public int getTrackbackUserId() {
		return this.trackbackUserId;
	}

	public void setTrackbackUserId(int trackbackUserId) {
		this.trackbackUserId = trackbackUserId;
	}

	public String getTrackbackType() {
		return this.trackbackType;
	}

	public void setTrackbackType(String trackbackType) {
		this.trackbackType = trackbackType;
	}

	public String getTrackbackStatus() {
		return this.trackbackStatus;
	}

	public void setTrackbackStatus(String trackbackStatus) {
		this.trackbackStatus = trackbackStatus;
	}

	public Date getTrackbackModified() {
		return this.trackbackModified;
	}

	public void setTrackbackModified(Date trackbackModified) {
		this.trackbackModified = trackbackModified;
	}

	public Date getTrackbackDate() {
		return this.trackbackDate;
	}

	public void setTrackbackDate(Date trackbackDate) {
		this.trackbackDate = trackbackDate;
	}

	public String getTrackbackUrl() {
		return this.trackbackUrl;
	}

	public void setTrackbackUrl(String trackbackUrl) {
		this.trackbackUrl = trackbackUrl;
	}

	public String getTrackbackTitle() {
		return this.trackbackTitle;
	}

	public void setTrackbackTitle(String trackbackTitle) {
		this.trackbackTitle = trackbackTitle;
	}

	public String getTrackbackContent() {
		return this.trackbackContent;
	}

	public void setTrackbackContent(String trackbackContent) {
		this.trackbackContent = trackbackContent;
	}

}
