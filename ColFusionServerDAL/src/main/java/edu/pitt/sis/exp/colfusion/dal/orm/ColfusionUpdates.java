package edu.pitt.sis.exp.colfusion.dal.orm;

// Generated 2014-6-10 16:17:59 by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionUpdates generated by hbm2java
 */
public class ColfusionUpdates implements java.io.Serializable {

	private Integer updateId;
	private Integer updateTime;
	private char updateType;
	private int updateLinkId;
	private int updateUserId;
	private int updateGroupId;
	private int updateLikes;
	private String updateLevel;
	private String updateText;

	public ColfusionUpdates() {
	}

	public ColfusionUpdates(char updateType, int updateLinkId,
			int updateUserId, int updateGroupId, int updateLikes,
			String updateText) {
		this.updateType = updateType;
		this.updateLinkId = updateLinkId;
		this.updateUserId = updateUserId;
		this.updateGroupId = updateGroupId;
		this.updateLikes = updateLikes;
		this.updateText = updateText;
	}

	public ColfusionUpdates(Integer updateTime, char updateType,
			int updateLinkId, int updateUserId, int updateGroupId,
			int updateLikes, String updateLevel, String updateText) {
		this.updateTime = updateTime;
		this.updateType = updateType;
		this.updateLinkId = updateLinkId;
		this.updateUserId = updateUserId;
		this.updateGroupId = updateGroupId;
		this.updateLikes = updateLikes;
		this.updateLevel = updateLevel;
		this.updateText = updateText;
	}

	public Integer getUpdateId() {
		return this.updateId;
	}

	public void setUpdateId(Integer updateId) {
		this.updateId = updateId;
	}

	public Integer getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}

	public char getUpdateType() {
		return this.updateType;
	}

	public void setUpdateType(char updateType) {
		this.updateType = updateType;
	}

	public int getUpdateLinkId() {
		return this.updateLinkId;
	}

	public void setUpdateLinkId(int updateLinkId) {
		this.updateLinkId = updateLinkId;
	}

	public int getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(int updateUserId) {
		this.updateUserId = updateUserId;
	}

	public int getUpdateGroupId() {
		return this.updateGroupId;
	}

	public void setUpdateGroupId(int updateGroupId) {
		this.updateGroupId = updateGroupId;
	}

	public int getUpdateLikes() {
		return this.updateLikes;
	}

	public void setUpdateLikes(int updateLikes) {
		this.updateLikes = updateLikes;
	}

	public String getUpdateLevel() {
		return this.updateLevel;
	}

	public void setUpdateLevel(String updateLevel) {
		this.updateLevel = updateLevel;
	}

	public String getUpdateText() {
		return this.updateText;
	}

	public void setUpdateText(String updateText) {
		this.updateText = updateText;
	}

}
