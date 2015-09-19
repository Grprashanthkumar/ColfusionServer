package edu.pitt.sis.exp.colfusion.bll.responseModels;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTableViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class StoryTableResponse  extends GeneralResponseImpl implements Gsonazable{

	@Expose private ArrayList<StoryTableViewModel> payload =  new ArrayList<StoryTableViewModel>();
	
	public void setPayload(final ArrayList<StoryTableViewModel> payload) {
		this.payload = payload;
	}

	public ArrayList<StoryTableViewModel> getPayload() {
		return payload;
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
