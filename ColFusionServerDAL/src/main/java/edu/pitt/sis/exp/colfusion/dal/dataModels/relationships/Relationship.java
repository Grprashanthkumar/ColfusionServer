package edu.pitt.sis.exp.colfusion.dal.dataModels.relationships;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.annotations.Expose;

public class Relationship {
	
	static Logger logger = LogManager.getLogger(Relationship.class.getName());
	
	@Expose private int sidFrom;
	@Expose private String tableNameFrom;
	
	@Expose private int sidTo;
	@Expose private String tableNameTo;
	
	@Expose private List<RelationshipLink> links;
	
	public Relationship() {
		
	}
	
	public Relationship(final int sidFrom, final String tableNameFrom, final int sidTo, final String tableNameTo, final List<RelationshipLink> links) throws Exception {
		
		this.setSidFrom(sidFrom);
		this.setTableNameFrom(tableNameFrom);
		this.setSidTo(sidTo);
		this.setTableNameTo(tableNameTo);
		this.setLinks(links);
	}

	/**
	 * @return the sidFrom
	 */
	public int getSidFrom() {
		return sidFrom;
	}

	/**
	 * @return the tableNameFrom
	 */
	public String getTableNameFrom() {
		return tableNameFrom;
	}

	/**
	 * @return the sidTo
	 */
	public int getSidTo() {
		return sidTo;
	}

	/**
	 * @return the tableNameTo
	 */
	public String getTableNameTo() {
		return tableNameTo;
	}

	/**
	 * @return the links
	 */
	public List<RelationshipLink> getLinks() {
		return links;
	}

	/**
	 * @param sidFrom the sidFrom to set
	 */
	private void setSidFrom(int sidFrom) {
		this.sidFrom = sidFrom;
	}

	/**
	 * @param tableNameFrom the tableNameFrom to set
	 */
	private void setTableNameFrom(String tableNameFrom) {
		this.tableNameFrom = tableNameFrom;
	}

	/**
	 * @param sidTo the sidTo to set
	 */
	private void setSidTo(int sidTo) {
		this.sidTo = sidTo;
	}

	/**
	 * @param tableNameTo the tableNameTo to set
	 */
	private void setTableNameTo(String tableNameTo) {
		this.tableNameTo = tableNameTo;
	}

	/**
	 * @param links the links to set
	 */
	private void setLinks(List<RelationshipLink> links) {
		this.links = links;
	}
}
