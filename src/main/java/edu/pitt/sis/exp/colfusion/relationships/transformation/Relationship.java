package edu.pitt.sis.exp.colfusion.relationships.transformation;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoDb;

public class Relationship {
	
	static Logger logger = LogManager.getLogger(Relationship.class.getName());
	
	private final int sidFrom;
	private final String tableNameFrom;
	
	private final int sidTo;
	private final String tableNameTo;
	
	private final List<RelationshipLink> links;
	
	//TODO:don't use hibernate generated classes here
	private final ColfusionSourceinfoDb dbFrom;
	private final ColfusionSourceinfoDb dbTo;
	
	public Relationship(final int sidFrom, final String tableNameFrom, final int sidTo, final String tableNameTo, final List<RelationshipLink> links,
			final ColfusionSourceinfoDb dbFrom, final ColfusionSourceinfoDb dbTo) throws Exception {
		
		this.sidFrom = sidFrom;
		this.tableNameFrom = tableNameFrom;
		this.sidTo = sidTo;
		this.tableNameTo = tableNameTo;
		this.links = links;
		this.dbFrom = dbFrom;
		this.dbTo = dbTo;
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
	 * @return the dbFrom
	 */
	public ColfusionSourceinfoDb getDbFrom() {
		return dbFrom;
	}

	/**
	 * @return the dbTo
	 */
	public ColfusionSourceinfoDb getDbTo() {
		return dbTo;
	}

	
}
