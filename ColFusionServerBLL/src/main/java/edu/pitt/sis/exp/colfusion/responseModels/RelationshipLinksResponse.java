package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipLinkViewModel;

public class RelationshipLinksResponse extends GeneralResponseImpl {
	private List<RelationshipLinkViewModel> payload = new ArrayList<>();

	/**
	 * @return the payload
	 */
	public List<RelationshipLinkViewModel> getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final List<RelationshipLinkViewModel> payload) {
		this.payload = payload;
	}
}
