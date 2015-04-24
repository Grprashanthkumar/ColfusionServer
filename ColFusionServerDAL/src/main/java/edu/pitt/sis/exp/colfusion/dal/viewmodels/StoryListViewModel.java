package edu.pitt.sis.exp.colfusion.dal.viewmodels;


import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;
/**
 * @author Weichuan Hong
 *
 */

@XmlRootElement
public class StoryListViewModel {
	@Expose private int sid;
	@Expose private String title;
	//added description property - Shruti Sabusuresh
	@Expose private String description;
	@Expose private UserViewModel user;
	@Expose private String path;
	@Expose private Date entryDate;
	@Expose private Date lastUpdated;
	@Expose private String status;
	@Expose private String rawDataPath;
	@Expose private String sourceType;
	@Expose private LicenseViewModel license;
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UserViewModel getUser() {
		return user;
	}
	public void setUser(UserViewModel user) {
		this.user = user;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRawDataPath() {
		return rawDataPath;
	}
	public void setRawDataPath(String rawDataPath) {
		this.rawDataPath = rawDataPath;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	public LicenseViewModel getLicense() {
		return license;
	}
	public void setLicense(LicenseViewModel license) {
		this.license = license;
	}
	
	
	
}
