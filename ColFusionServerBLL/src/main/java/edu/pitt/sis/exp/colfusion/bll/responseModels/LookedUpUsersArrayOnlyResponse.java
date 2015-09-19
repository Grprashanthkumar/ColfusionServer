/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.responseModels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryAuthorViewModel;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class LookedUpUsersArrayOnlyResponse {
	private ArrayList<StoryAuthorViewModel> payload = new ArrayList<>();

	/**
	 * @return the payload
	 */
	public ArrayList<StoryAuthorViewModel> getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final ArrayList<StoryAuthorViewModel> payload) {
		this.payload = payload;
	}
}
