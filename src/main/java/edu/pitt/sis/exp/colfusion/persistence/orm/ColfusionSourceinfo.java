package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 17, 2014 8:18:50 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ColfusionSourceinfo generated by hbm2java
 */
public class ColfusionSourceinfo implements java.io.Serializable {

	private Integer sid;
	private ColfusionUsers colfusionUsers;
	private String title;
	private String path;
	private Date entryDate;
	private Date lastUpdated;
	private String status;
	private String rawDataPath;
	private String sourceType;
	private String provenance;
	private Set colfusionSynonymsFroms = new HashSet(0);
	private Set colfusionExecuteinfos = new HashSet(0);
	private Set colfusionTemporaries = new HashSet(0);
	private Set colfusionSynonymsTos = new HashSet(0);
	private Set colfusionRelationshipsesForSid1 = new HashSet(0);
	private Set colfusionDesAttachmentses = new HashSet(0);
	private Set colfusionRelationshipsesForSid2 = new HashSet(0);
	private Set colfusionDnameinfos = new HashSet(0);
	private ColfusionSourceinfoDb colfusionSourceinfoDb;
	private Set colfusionVisualizations = new HashSet(0);

	public ColfusionSourceinfo() {
	}

	public ColfusionSourceinfo(ColfusionUsers colfusionUsers, Date entryDate,
			String sourceType) {
		this.colfusionUsers = colfusionUsers;
		this.entryDate = entryDate;
		this.sourceType = sourceType;
	}

	public ColfusionSourceinfo(ColfusionUsers colfusionUsers, String title,
			String path, Date entryDate, Date lastUpdated, String status,
			String rawDataPath, String sourceType, String provenance,
			Set colfusionSynonymsFroms, Set colfusionExecuteinfos,
			Set colfusionTemporaries, Set colfusionSynonymsTos,
			Set colfusionRelationshipsesForSid1, Set colfusionDesAttachmentses,
			Set colfusionRelationshipsesForSid2, Set colfusionDnameinfos,
			ColfusionSourceinfoDb colfusionSourceinfoDb,
			Set colfusionVisualizations) {
		this.colfusionUsers = colfusionUsers;
		this.title = title;
		this.path = path;
		this.entryDate = entryDate;
		this.lastUpdated = lastUpdated;
		this.status = status;
		this.rawDataPath = rawDataPath;
		this.sourceType = sourceType;
		this.provenance = provenance;
		this.colfusionSynonymsFroms = colfusionSynonymsFroms;
		this.colfusionExecuteinfos = colfusionExecuteinfos;
		this.colfusionTemporaries = colfusionTemporaries;
		this.colfusionSynonymsTos = colfusionSynonymsTos;
		this.colfusionRelationshipsesForSid1 = colfusionRelationshipsesForSid1;
		this.colfusionDesAttachmentses = colfusionDesAttachmentses;
		this.colfusionRelationshipsesForSid2 = colfusionRelationshipsesForSid2;
		this.colfusionDnameinfos = colfusionDnameinfos;
		this.colfusionSourceinfoDb = colfusionSourceinfoDb;
		this.colfusionVisualizations = colfusionVisualizations;
	}

	public Integer getSid() {
		return this.sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public ColfusionUsers getColfusionUsers() {
		return this.colfusionUsers;
	}

	public void setColfusionUsers(ColfusionUsers colfusionUsers) {
		this.colfusionUsers = colfusionUsers;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRawDataPath() {
		return this.rawDataPath;
	}

	public void setRawDataPath(String rawDataPath) {
		this.rawDataPath = rawDataPath;
	}

	public String getSourceType() {
		return this.sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getProvenance() {
		return this.provenance;
	}

	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}

	public Set getColfusionSynonymsFroms() {
		return this.colfusionSynonymsFroms;
	}

	public void setColfusionSynonymsFroms(Set colfusionSynonymsFroms) {
		this.colfusionSynonymsFroms = colfusionSynonymsFroms;
	}

	public Set getColfusionExecuteinfos() {
		return this.colfusionExecuteinfos;
	}

	public void setColfusionExecuteinfos(Set colfusionExecuteinfos) {
		this.colfusionExecuteinfos = colfusionExecuteinfos;
	}

	public Set getColfusionTemporaries() {
		return this.colfusionTemporaries;
	}

	public void setColfusionTemporaries(Set colfusionTemporaries) {
		this.colfusionTemporaries = colfusionTemporaries;
	}

	public Set getColfusionSynonymsTos() {
		return this.colfusionSynonymsTos;
	}

	public void setColfusionSynonymsTos(Set colfusionSynonymsTos) {
		this.colfusionSynonymsTos = colfusionSynonymsTos;
	}

	public Set getColfusionRelationshipsesForSid1() {
		return this.colfusionRelationshipsesForSid1;
	}

	public void setColfusionRelationshipsesForSid1(
			Set colfusionRelationshipsesForSid1) {
		this.colfusionRelationshipsesForSid1 = colfusionRelationshipsesForSid1;
	}

	public Set getColfusionDesAttachmentses() {
		return this.colfusionDesAttachmentses;
	}

	public void setColfusionDesAttachmentses(Set colfusionDesAttachmentses) {
		this.colfusionDesAttachmentses = colfusionDesAttachmentses;
	}

	public Set getColfusionRelationshipsesForSid2() {
		return this.colfusionRelationshipsesForSid2;
	}

	public void setColfusionRelationshipsesForSid2(
			Set colfusionRelationshipsesForSid2) {
		this.colfusionRelationshipsesForSid2 = colfusionRelationshipsesForSid2;
	}

	public Set getColfusionDnameinfos() {
		return this.colfusionDnameinfos;
	}

	public void setColfusionDnameinfos(Set colfusionDnameinfos) {
		this.colfusionDnameinfos = colfusionDnameinfos;
	}

	public ColfusionSourceinfoDb getColfusionSourceinfoDb() {
		return this.colfusionSourceinfoDb;
	}

	public void setColfusionSourceinfoDb(
			ColfusionSourceinfoDb colfusionSourceinfoDb) {
		this.colfusionSourceinfoDb = colfusionSourceinfoDb;
	}

	public Set getColfusionVisualizations() {
		return this.colfusionVisualizations;
	}

	public void setColfusionVisualizations(Set colfusionVisualizations) {
		this.colfusionVisualizations = colfusionVisualizations;
	}

}
