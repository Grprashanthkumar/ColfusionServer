package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.BasicTableInfoViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class BasicTableResponseModel extends GeneralResponseImpl implements Gsonazable {
	private List<BasicTableInfoViewModel> payload = new ArrayList<BasicTableInfoViewModel>();

	/**
	 * @return the payload
	 */
	public List<BasicTableInfoViewModel> getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final List<BasicTableInfoViewModel> payload) {
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
