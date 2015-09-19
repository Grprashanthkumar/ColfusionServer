package edu.pitt.sis.exp.colfusion.bll.responseModels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.LicenseViewModel;

public class LicensesResponseModel extends GeneralResponseImpl {
	private List<LicenseViewModel> payload = new ArrayList<LicenseViewModel>();
	  
	public List<LicenseViewModel> getPayload() {
		return this.payload;
	}
	  
	public void setPayload(final List<LicenseViewModel> payload) {
		this.payload = payload; 
	}
}
