package edu.pitt.sis.exp.colfusion.bll.responseModels;

import edu.pitt.sis.exp.colfusion.bll.relationships.relationshipGraph.RelationshipGraph;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class RelationshipGraphResponseModel extends GeneralResponseImpl implements Gsonazable {
	private RelationshipGraph payload;

	/**
	 * @return the payload
	 */
	public RelationshipGraph getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final RelationshipGraph payload) {
		this.payload = payload;
	}

	@Override
	public String toJson() {
		return Gsonizer.toJson(this, false);
	}

	@Override
	public void fromJson() {
		// TODO Auto-generated method stub
		
	}
}
