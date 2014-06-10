package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated 2014-6-10 16:17:59 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * ColfusionRelationships generated by hbm2java
 */
public class ColfusionRelationships implements java.io.Serializable {

	private Integer relId;
	private ColfusionSourceinfo colfusionSourceinfoBySid1;
	private ColfusionUsers colfusionUsers;
	private ColfusionSourceinfo colfusionSourceinfoBySid2;
	private String name;
	private String description;
	private Date creationTime;
	private String tableName1;
	private String tableName2;
	private int status;
	private Set colfusionRelationshipsColumnses = new HashSet(0);
	private Set colfusionUserRelationshipVerdicts = new HashSet(0);

	public ColfusionRelationships() {
	}

	public ColfusionRelationships(
			ColfusionSourceinfo colfusionSourceinfoBySid1,
			ColfusionUsers colfusionUsers,
			ColfusionSourceinfo colfusionSourceinfoBySid2, Date creationTime,
			int status) {
		this.colfusionSourceinfoBySid1 = colfusionSourceinfoBySid1;
		this.colfusionUsers = colfusionUsers;
		this.colfusionSourceinfoBySid2 = colfusionSourceinfoBySid2;
		this.creationTime = creationTime;
		this.status = status;
	}

	public ColfusionRelationships(
			ColfusionSourceinfo colfusionSourceinfoBySid1,
			ColfusionUsers colfusionUsers,
			ColfusionSourceinfo colfusionSourceinfoBySid2, String name,
			String description, Date creationTime, String tableName1,
			String tableName2, int status, Set colfusionRelationshipsColumnses,
			Set colfusionUserRelationshipVerdicts) {
		this.colfusionSourceinfoBySid1 = colfusionSourceinfoBySid1;
		this.colfusionUsers = colfusionUsers;
		this.colfusionSourceinfoBySid2 = colfusionSourceinfoBySid2;
		this.name = name;
		this.description = description;
		this.creationTime = creationTime;
		this.tableName1 = tableName1;
		this.tableName2 = tableName2;
		this.status = status;
		this.colfusionRelationshipsColumnses = colfusionRelationshipsColumnses;
		this.colfusionUserRelationshipVerdicts = colfusionUserRelationshipVerdicts;
	}

	public Integer getRelId() {
		return this.relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}

	public ColfusionSourceinfo getColfusionSourceinfoBySid1() {
		return this.colfusionSourceinfoBySid1;
	}

	public void setColfusionSourceinfoBySid1(
			ColfusionSourceinfo colfusionSourceinfoBySid1) {
		this.colfusionSourceinfoBySid1 = colfusionSourceinfoBySid1;
	}

	public ColfusionUsers getColfusionUsers() {
		return this.colfusionUsers;
	}

	public void setColfusionUsers(ColfusionUsers colfusionUsers) {
		this.colfusionUsers = colfusionUsers;
	}

	public ColfusionSourceinfo getColfusionSourceinfoBySid2() {
		return this.colfusionSourceinfoBySid2;
	}

	public void setColfusionSourceinfoBySid2(
			ColfusionSourceinfo colfusionSourceinfoBySid2) {
		this.colfusionSourceinfoBySid2 = colfusionSourceinfoBySid2;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getTableName1() {
		return this.tableName1;
	}

	public void setTableName1(String tableName1) {
		this.tableName1 = tableName1;
	}

	public String getTableName2() {
		return this.tableName2;
	}

	public void setTableName2(String tableName2) {
		this.tableName2 = tableName2;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Set getColfusionRelationshipsColumnses() {
		return this.colfusionRelationshipsColumnses;
	}

	public void setColfusionRelationshipsColumnses(
			Set colfusionRelationshipsColumnses) {
		this.colfusionRelationshipsColumnses = colfusionRelationshipsColumnses;
	}

	public Set getColfusionUserRelationshipVerdicts() {
		return this.colfusionUserRelationshipVerdicts;
	}

	public void setColfusionUserRelationshipVerdicts(
			Set colfusionUserRelationshipVerdicts) {
		this.colfusionUserRelationshipVerdicts = colfusionUserRelationshipVerdicts;
	}

}
