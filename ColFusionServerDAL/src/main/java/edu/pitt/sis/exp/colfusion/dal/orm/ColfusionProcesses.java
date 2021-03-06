package edu.pitt.sis.exp.colfusion.dal.orm;

// Generated 2014-6-10 16:17:59 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * ColfusionProcesses generated by hbm2java
 */
public class ColfusionProcesses implements java.io.Serializable {

	private Integer pid;
	private String status;
	private String processSer;
	private String processClass;
	private String reasonForStatus;
	private Set colfusionRelationshipsColumnsDataMathingRatioses = new HashSet(
			0);

	public ColfusionProcesses() {
	}

	public ColfusionProcesses(String status, String processSer,
			String processClass, String reasonForStatus,
			Set colfusionRelationshipsColumnsDataMathingRatioses) {
		this.status = status;
		this.processSer = processSer;
		this.processClass = processClass;
		this.reasonForStatus = reasonForStatus;
		this.colfusionRelationshipsColumnsDataMathingRatioses = colfusionRelationshipsColumnsDataMathingRatioses;
	}

	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProcessSer() {
		return this.processSer;
	}

	public void setProcessSer(String processSer) {
		this.processSer = processSer;
	}

	public String getProcessClass() {
		return this.processClass;
	}

	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}

	public String getReasonForStatus() {
		return this.reasonForStatus;
	}

	public void setReasonForStatus(String reasonForStatus) {
		this.reasonForStatus = reasonForStatus;
	}

	public Set getColfusionRelationshipsColumnsDataMathingRatioses() {
		return this.colfusionRelationshipsColumnsDataMathingRatioses;
	}

	public void setColfusionRelationshipsColumnsDataMathingRatioses(
			Set colfusionRelationshipsColumnsDataMathingRatioses) {
		this.colfusionRelationshipsColumnsDataMathingRatioses = colfusionRelationshipsColumnsDataMathingRatioses;
	}

}
