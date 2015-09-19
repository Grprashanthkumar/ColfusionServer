package edu.pitt.sis.exp.colfusion.bll.responseModels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.LocationViewModel;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class LocationListResponse extends GeneralResponseImpl implements Gsonazable{

	@Expose private ArrayList<LocationViewModel> payload =  new ArrayList<LocationViewModel>();
	
	public ArrayList<LocationViewModel> getPayload() {
		return payload;
	}

	public void setPayload(final ArrayList<LocationViewModel> payload) {
		this.payload = payload;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return Gsonizer.toJson(this, true);
	}

	@Override
	public void fromJson() {
		// TODO Auto-generated method stub
		
	}

}
