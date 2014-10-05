/**
 * 
 */
package edu.pitt.sis.exp.colfusion.responseModels;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataHistoryViewModel;

/**
 * @author Evgeny
 *
 */
public class StoryMetadataHistoryResponse extends GeneralResponseImpl {
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
	public void setPayload(final StoryMetadataHistoryViewModel payload) {
		this.payload = payload;
	}
}
