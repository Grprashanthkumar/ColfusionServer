package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 2:44:19 PM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionLikesId generated by hbm2java
 */
public class ColfusionLikesId implements java.io.Serializable {

	private int likeUpdateId;
	private int likeUserId;

	public ColfusionLikesId() {
	}

	public ColfusionLikesId(int likeUpdateId, int likeUserId) {
		this.likeUpdateId = likeUpdateId;
		this.likeUserId = likeUserId;
	}

	public int getLikeUpdateId() {
		return this.likeUpdateId;
	}

	public void setLikeUpdateId(int likeUpdateId) {
		this.likeUpdateId = likeUpdateId;
	}

	public int getLikeUserId() {
		return this.likeUserId;
	}

	public void setLikeUserId(int likeUserId) {
		this.likeUserId = likeUserId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ColfusionLikesId))
			return false;
		ColfusionLikesId castOther = (ColfusionLikesId) other;

		return (this.getLikeUpdateId() == castOther.getLikeUpdateId())
				&& (this.getLikeUserId() == castOther.getLikeUserId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getLikeUpdateId();
		result = 37 * result + this.getLikeUserId();
		return result;
	}

}
