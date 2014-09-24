/**
 * 
 */
package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.viewmodels.StoryAuthorViewModel;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class LookedUpUsersArrayOnly {
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
	public void setPayload(ArrayList<StoryAuthorViewModel> payload) {
		this.payload = payload;
	}
}
