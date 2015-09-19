/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.responseModels;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataViewModel;


/**
 * @author Evgeny
 *
 */
public class StoryMetadataResponse extends GeneralResponseImpl {
	private StoryMetadataViewModel payload = new StoryMetadataViewModel();

	/**
	 * @return the payload
	 */
	public StoryMetadataViewModel getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final StoryMetadataViewModel payload) {
		this.payload = payload;
	}
}
