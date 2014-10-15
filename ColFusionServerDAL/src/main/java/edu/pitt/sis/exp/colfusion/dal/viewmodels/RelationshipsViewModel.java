package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gson.annotations.Expose;

public class RelationshipsViewModel {

	@Expose private Integer rel_id;
	@Expose private String name;
	@Expose private String description;
	@Expose private Integer creator;
	@Expose private Date creationTime;
	@Expose private String creatorLogin;
	@Expose private Integer sidFrom;
	@Expose private Integer sidTo;
	@Expose private String titleFrom;
	@Expose private String titleTo;
	@Expose private String tableNameFrom;
	@Expose private String tableNameTo;
	@Expose private Long numberOfVerdicts;
	@Expose private BigDecimal numberOfApproved;
	@Expose private BigDecimal numberOfReject;
	@Expose private BigDecimal numberOfNotSure;
	@Expose private BigDecimal avgConfidence;
	
	public RelationshipsViewModel() {
		
	}
	
	public RelationshipsViewModel(Integer relid, String name, String description, Integer creator,
			Date creationTime, String creatorLogin, Integer sidFrom, Integer sidTo,
			String titleFrom, String titleTo, String tableNameFrom, String tableNameTo,
			Long numberOfVerdicts, BigDecimal numberOfApproved, BigDecimal numberOfReject, BigDecimal numberOfNotSure,
			BigDecimal avgConfidence) { 
		this.rel_id = relid;
		this.name = name;
		this.description = description;
		this.creator = creator;
		this.creationTime = creationTime;
		this.creatorLogin = creatorLogin;
		this.sidFrom = sidFrom;
		this.sidTo = sidTo;
		this.titleFrom = titleFrom;
		this.titleTo = titleTo;
		this.tableNameFrom = tableNameFrom;
		this.tableNameTo = tableNameTo;
		this.numberOfVerdicts = numberOfVerdicts;
		this.numberOfApproved = numberOfApproved;
		this.numberOfReject = numberOfReject;
		this.numberOfNotSure = numberOfNotSure;
		this.avgConfidence = avgConfidence; 
	}
	
	public int getRelid() {
		return rel_id;
	}
	public void setRelid(int relid) {
		this.rel_id = relid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getCreator() {
		return creator;
	}
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getCreatorLogin() {
		return creatorLogin;
	}
	public void setCreatorLogin(String creatorLogin) {
		this.creatorLogin = creatorLogin;
	}
	public Integer getSidFrom() {
		return sidFrom;
	}
	public void setSidFrom(Integer sidFrom) {
		this.sidFrom = sidFrom;
	}
	public Integer getSidTo() {
		return sidTo;
	}
	public void setSidTo(Integer sidTo) {
		this.sidTo = sidTo;
	}
	public String getTitleFrom() {
		return titleFrom;
	}
	public void setTitleFrom(String titleFrom) {
		this.titleFrom = titleFrom;
	}
	public String getTitleTo() {
		return titleTo;
	}
	public void setTitleTo(String titleTo) {
		this.titleTo = titleTo;
	}
	public String getTableNameFrom() {
		return tableNameFrom;
	}
	public void setTableNameFrom(String tableNameFrom) {
		this.tableNameFrom = tableNameFrom;
	}
	public String getTableNameTo() {
		return tableNameTo;
	}
	public void setTableNameTo(String tableNameTo) {
		this.tableNameTo = tableNameTo;
	}
	public long getNumberOfVerdicts() {
		return numberOfVerdicts;
	}
	public void setNumberOfVerdicts(long numberOfVerdicts) {
		this.numberOfVerdicts = numberOfVerdicts;
	}
	public BigDecimal getNumberOfApproved() {
		return numberOfApproved;
	}
	public void setNumberOfApproved(BigDecimal numberOfApproved) {
		this.numberOfApproved = numberOfApproved;
	}
	public BigDecimal getNumberOfReject() {
		return numberOfReject;
	}
	public void setNumberOfReject(BigDecimal numberOfReject) {
		this.numberOfReject = numberOfReject;
	}
	public BigDecimal getNumberOfNotSure() {
		return numberOfNotSure;
	}
	public void setNumberOfNotSure(BigDecimal numberOfNotSure) {
		this.numberOfNotSure = numberOfNotSure;
	}
	public BigDecimal getAvgConfidence() {
		return avgConfidence;
	}
	public void setAvgConfidence(BigDecimal avgConfidence) {
		this.avgConfidence = avgConfidence;
	}
	
	
}
