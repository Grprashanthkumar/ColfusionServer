package edu.pitt.sis.exp.colfusion.dal.dataModels.relationships;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransformation;

public class RelationshipLink {
	
	static Logger logger = LogManager.getLogger(RelationshipLink.class.getName());
	
	@Expose private RelationshipTransformation from;
	@Expose private RelationshipTransformation to;
	
	public RelationshipLink() {
		
	}
	
	public RelationshipLink(final RelationshipTransformation from, final RelationshipTransformation to) throws Exception {
		
		this.setFrom(from);
		this.setTo(to);
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

	/**
	 * @param from the from to set
	 */
	private void setFrom(RelationshipTransformation from) {
		this.from = from;
	}

	/**
	 * @param to the to to set
	 */
	private void setTo(RelationshipTransformation to) {
		this.to = to;
	}
}
