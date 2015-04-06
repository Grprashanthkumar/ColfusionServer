package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.FacetedSearchViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class FacetedSearchResponseModel extends GeneralResponseImpl implements Gsonazable {
	@Expose private List<FacetedSearchViewModel> payload = new ArrayList<FacetedSearchViewModel>();

	public List<FacetedSearchViewModel> getPayload() {
		return payload;
	}

	public void setPayload(final List<FacetedSearchViewModel> payload) {
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
