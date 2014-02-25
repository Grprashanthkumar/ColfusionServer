package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 25, 2014 9:55:27 AM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * ColfusionComments generated by hbm2java
 */
public class ColfusionComments implements java.io.Serializable {

	private Integer commentId;
	private int commentRandkey;
	private Integer commentParent;
	private int commentLinkId;
	private int commentUserId;
	private Date commentDate;
	private short commentKarma;
	private String commentContent;
	private int commentVotes;
	private String commentStatus;

	public ColfusionComments() {
	}

	public ColfusionComments(int commentRandkey, int commentLinkId,
			int commentUserId, Date commentDate, short commentKarma,
			int commentVotes, String commentStatus) {
		this.commentRandkey = commentRandkey;
		this.commentLinkId = commentLinkId;
		this.commentUserId = commentUserId;
		this.commentDate = commentDate;
		this.commentKarma = commentKarma;
		this.commentVotes = commentVotes;
		this.commentStatus = commentStatus;
	}

	public ColfusionComments(int commentRandkey, Integer commentParent,
			int commentLinkId, int commentUserId, Date commentDate,
			short commentKarma, String commentContent, int commentVotes,
			String commentStatus) {
		this.commentRandkey = commentRandkey;
		this.commentParent = commentParent;
		this.commentLinkId = commentLinkId;
		this.commentUserId = commentUserId;
		this.commentDate = commentDate;
		this.commentKarma = commentKarma;
		this.commentContent = commentContent;
		this.commentVotes = commentVotes;
		this.commentStatus = commentStatus;
	}

	public Integer getCommentId() {
		return this.commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public int getCommentRandkey() {
		return this.commentRandkey;
	}

	public void setCommentRandkey(int commentRandkey) {
		this.commentRandkey = commentRandkey;
	}

	public Integer getCommentParent() {
		return this.commentParent;
	}

	public void setCommentParent(Integer commentParent) {
		this.commentParent = commentParent;
	}

	public int getCommentLinkId() {
		return this.commentLinkId;
	}

	public void setCommentLinkId(int commentLinkId) {
		this.commentLinkId = commentLinkId;
	}

	public int getCommentUserId() {
		return this.commentUserId;
	}

	public void setCommentUserId(int commentUserId) {
		this.commentUserId = commentUserId;
	}

	public Date getCommentDate() {
		return this.commentDate;
	}

	public void setCommentDate(Date commentDate) {
		this.commentDate = commentDate;
	}

	public short getCommentKarma() {
		return this.commentKarma;
	}

	public void setCommentKarma(short commentKarma) {
		this.commentKarma = commentKarma;
	}

	public String getCommentContent() {
		return this.commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public int getCommentVotes() {
		return this.commentVotes;
	}

	public void setCommentVotes(int commentVotes) {
		this.commentVotes = commentVotes;
	}

	public String getCommentStatus() {
		return this.commentStatus;
	}

	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}

}
