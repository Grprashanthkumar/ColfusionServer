package edu.pitt.sis.exp.colfusion.bll.relationships.relationshipGraph;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.DnameViewModel;

public class RelationshipGraphNode {
	private int sid;
	private String tableName;
	private String sidTitle;
	
	private List<DnameViewModel> allColumns;

	public RelationshipGraphNode(final int sid, final String tableName, final String sidTitle, final List<DnameViewModel> allColumns) {
		setSid(sid);
		setTableName(tableName);
		setSidTitle(sidTitle);
		setAllColumns(allColumns);
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
	 * @return the allColumns
	 */
	public List<DnameViewModel> getAllColumns() {
		return allColumns;
	}

	/**
	 * @param allColumns the allColumns to set
	 */
	public void setAllColumns(final List<DnameViewModel> allColumns) {
		this.allColumns = allColumns;
	}
}
