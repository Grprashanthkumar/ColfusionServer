package edu.pitt.sis.exp.colfusion.relationships.transformation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RelationshipLink {
	
	static Logger logger = LogManager.getLogger(RelationshipLink.class.getName());
	
	private final RelationshipTransformation from;
	private final RelationshipTransformation to;
	
	public RelationshipLink(final RelationshipTransformation from, final RelationshipTransformation to) throws Exception {
		
		this.from = from;
		this.to = to;
	}

	/**
	 * @return the from
	 */
	public RelationshipTransformation getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public RelationshipTransformation getTo() {
		return to;
	}
}
