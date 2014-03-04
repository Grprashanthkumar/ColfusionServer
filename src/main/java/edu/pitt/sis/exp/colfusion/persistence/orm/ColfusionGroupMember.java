package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 5:25:41 PM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionGroupMember generated by hbm2java
 */
public class ColfusionGroupMember implements java.io.Serializable {

	private Integer memberId;
	private int memberUserId;
	private int memberGroupId;
	private String memberRole;
	private String memberStatus;

	public ColfusionGroupMember() {
	}

	public ColfusionGroupMember(int memberUserId, int memberGroupId,
			String memberRole, String memberStatus) {
		this.memberUserId = memberUserId;
		this.memberGroupId = memberGroupId;
		this.memberRole = memberRole;
		this.memberStatus = memberStatus;
	}

	public Integer getMemberId() {
		return this.memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public int getMemberUserId() {
		return this.memberUserId;
	}

	public void setMemberUserId(int memberUserId) {
		this.memberUserId = memberUserId;
	}

	public int getMemberGroupId() {
		return this.memberGroupId;
	}

	public void setMemberGroupId(int memberGroupId) {
		this.memberGroupId = memberGroupId;
	}

	public String getMemberRole() {
		return this.memberRole;
	}

	public void setMemberRole(String memberRole) {
		this.memberRole = memberRole;
	}

	public String getMemberStatus() {
		return this.memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

}
