/**
 * 
 */
package edu.pitt.sis.exp.colfusion.responseModels;

import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataHistoryViewModel;

/**
 * @author Evgeny
 *
 */
public class StoryMetadataHistoryResponse extends GeneralResponse {
	private StoryMetadataHistoryViewModel payload = new StoryMetadataHistoryViewModel();

	/**
	 * @return the payload
	 */
	public StoryMetadataHistoryViewModel getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(StoryMetadataHistoryViewModel payload) {
		this.payload = payload;
	}
}
