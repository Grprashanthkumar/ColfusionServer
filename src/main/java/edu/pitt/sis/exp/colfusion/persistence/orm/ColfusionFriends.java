package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 25, 2014 9:55:27 AM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionFriends generated by hbm2java
 */
public class ColfusionFriends implements java.io.Serializable {

	private Integer friendId;
	private long friendFrom;
	private long friendTo;

	public ColfusionFriends() {
	}

	public ColfusionFriends(long friendFrom, long friendTo) {
		this.friendFrom = friendFrom;
		this.friendTo = friendTo;
	}

	public Integer getFriendId() {
		return this.friendId;
	}

	public void setFriendId(Integer friendId) {
		this.friendId = friendId;
	}

	public long getFriendFrom() {
		return this.friendFrom;
	}

	public void setFriendFrom(long friendFrom) {
		this.friendFrom = friendFrom;
	}

	public long getFriendTo() {
		return this.friendTo;
	}

	public void setFriendTo(long friendTo) {
		this.friendTo = friendTo;
	}

}
