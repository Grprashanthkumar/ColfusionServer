package edu.pitt.sis.exp.colfusion.relationships.relationshipGraph;

public class RelationshipGraphEdge {
	private int relId;
	private String relName;
	private double confidence;
	
	private NodeInfo sidFrom;
	private NodeInfo sidTo;
	
	public RelationshipGraphEdge(final int relId, final String relName, final double confidence, 
			final NodeInfo sidFrom, final NodeInfo sidTo) {
		setRelId(relId);
		setRelName(relName);
		setConfidence(confidence);
		setSidFrom(sidFrom);
		setSidTo(sidTo);		
	}
	
	/**
	 * @return the relId
	 */
	public int getRelId() {
		return relId;
	}

	/**
	 * @param relId the relId to set
	 */
	public void setRelId(final int relId) {
		this.relId = relId;
	}

	/**
	 * @return the relName
	 */
	public String getRelName() {
		return relName;
	}

	/**
	 * @param relName the relName to set
	 */
	public void setRelName(final String relName) {
		this.relName = relName;
	}

	/**
	 * @return the sidFrom
	 */
	public NodeInfo getSidFrom() {
		return sidFrom;
	}

	/**
	 * @param sidFrom the sidFrom to set
	 */
	public void setSidFrom(final NodeInfo sidFrom) {
		this.sidFrom = sidFrom;
	}

	/**
	 * @return the sidTo
	 */
	public NodeInfo getSidTo() {
		return sidTo;
	}

	/**
	 * @param sidTo the sidTo to set
	 */
	public void setSidTo(final NodeInfo sidTo) {
		this.sidTo = sidTo;
	}

	/**
	 * @return the confidence
	 */
	public double getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(final double confidence) {
		this.confidence = confidence;
	}
	
	public static class NodeInfo {
		private int sid;
		private String sidTitle;
		private String tableName;
		
		public NodeInfo(final int sid, final String sidTitle, final String tableName) {
			setSid(sid);
			setSidTitle(sidTitle);
			setTableName(tableName);
		}
		
		/**
		 * @return the sid
		 */
		public int getSid() {
			return sid;
		}
		/**
		 * @param sid the sid to set
		 */
		public void setSid(final int sid) {
			this.sid = sid;
		}
		/**
		 * @return the sidTitle
		 */
		public String getSidTitle() {
			return sidTitle;
		}
		/**
		 * @param sidTitle the sidTitle to set
		 */
		public void setSidTitle(final String sidTitle) {
			this.sidTitle = sidTitle;
		}
		/**
		 * @return the tableName
		 */
		public String getTableName() {
			return tableName;
		}
		/**
		 * @param tableName the tableName to set
		 */
		public void setTableName(final String tableName) {
			this.tableName = tableName;
		}
	}
}
