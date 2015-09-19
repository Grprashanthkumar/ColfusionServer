package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class RowsResponseModel extends GeneralResponseImpl implements Gsonazable{

	@Expose private ArrayList<String> payload =  new ArrayList<String>();

	public void setPayload(final ArrayList<String> payload){
		this.payload = payload;
	}
	
	public ArrayList<String> getPayload() {
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
