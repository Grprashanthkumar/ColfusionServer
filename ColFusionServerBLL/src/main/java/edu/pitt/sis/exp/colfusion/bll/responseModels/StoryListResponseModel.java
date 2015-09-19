package edu.pitt.sis.exp.colfusion.bll.responseModels;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryListViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class StoryListResponseModel extends GeneralResponseImpl implements Gsonazable{
	
	@Expose private List<StoryListViewModel> payload = new ArrayList<StoryListViewModel>();
	
	public List<StoryListViewModel> getPayload() {
		return payload;
	}

	public void setPayload(final List<StoryListViewModel> payload) {
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
