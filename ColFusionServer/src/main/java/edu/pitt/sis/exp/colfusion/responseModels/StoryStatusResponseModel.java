package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryStatusViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;


/**
 * @author Weichuan Hong
 *
 */
public class StoryStatusResponseModel extends GeneralResponseImpl implements Gsonazable{
	@Expose private List<StoryStatusViewModel> payload = new ArrayList<StoryStatusViewModel>();

	/**
	 * @return the payload
	 */
	public List<StoryStatusViewModel> getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final List<StoryStatusViewModel> payload) {
		this.payload = payload;
	}

	@Override
	public String toJson() {
		return Gsonizer.toJson(this, true);
	}

	@Override
	public void fromJson() {
		// TODO Auto-generated method stub
		
	}
}
