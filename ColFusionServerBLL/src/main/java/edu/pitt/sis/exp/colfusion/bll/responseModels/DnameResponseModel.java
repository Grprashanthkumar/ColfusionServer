package edu.pitt.sis.exp.colfusion.bll.responseModels;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.DnameViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class DnameResponseModel  extends GeneralResponseImpl implements Gsonazable{
	
@Expose private List<DnameViewModel> payload = new ArrayList<DnameViewModel>();
	
	public List<DnameViewModel> getPayload() {
		return payload;
	}

	public void setPayload(final List<DnameViewModel> payload) {
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
