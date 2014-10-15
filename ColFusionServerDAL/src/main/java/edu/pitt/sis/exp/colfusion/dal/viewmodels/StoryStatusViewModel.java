package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;

public class StoryStatusViewModel  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose private Integer Eid = null;
	@Expose private Integer Sid;
	@Expose private String UserId = null;
	@Expose private Date TimeStart = null;
	@Expose private Date TimeEnd = null;
	@Expose private String ExitStatus = null;
	@Expose private String ErrorMessage = null;
	@Expose private Integer RecordsProcessed = null;
	@Expose private String status = null;
	@Expose private String panCommand = null;
	@Expose private String tableName = null;
	@Expose private String Log = null;
	@Expose private String numberProcessRecords = null;

	public Integer getEid() {
		return Eid;
	}
	public void setEid(Integer eid) {
		Eid = eid;
	}
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public Date getTimeStart() {
		return TimeStart;
	}
	public void setTimeStart(Date timeStart) {
		TimeStart = timeStart;
	}
	public Date getTimeEnd() {
		return TimeEnd;
	}
	public void setTimeEnd(Date timeEnd) {
		TimeEnd = timeEnd;
	}
	public String getExitStatus() {
		return ExitStatus;
	}
	public void setExitStatus(String exitStatus) {
		ExitStatus = exitStatus;
	}
	public String getErrorMessage() {
		return ErrorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		ErrorMessage = errorMessage;
	}
	public Integer getRecordsProcessed() {
		return RecordsProcessed;
	}
	public void setRecordsProcessed(Integer recordsProcessed) {
		RecordsProcessed = recordsProcessed;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPanCommand() {
		return panCommand;
	}
	public void setPanCommand(String panCommand) {
		this.panCommand = panCommand;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getLog() {
		return Log;
	}
	public void setLog(String log) {
		Log = log;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getSid() {
		return Sid;
	}
	public void setSid(Integer sid) {
		Sid = sid;
	}
	public String getNumberProcessRecords() {
		return numberProcessRecords;
	}
	public void setNumberProcessRecords(String numberProcessRecords) {
		this.numberProcessRecords = numberProcessRecords;
	}

	
	
}
