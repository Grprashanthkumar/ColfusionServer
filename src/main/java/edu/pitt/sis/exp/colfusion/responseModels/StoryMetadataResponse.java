/**
 * 
 */
package edu.pitt.sis.exp.colfusion.responseModels;

import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;


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
	public void setPayload(StoryMetadataViewModel payload) {
		this.payload = payload;
	}
}
