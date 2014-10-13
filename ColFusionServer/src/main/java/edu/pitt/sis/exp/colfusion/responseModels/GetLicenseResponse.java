package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.GetLicenseViewModal;

public class GetLicenseResponse extends GeneralResponseImpl {
	private GetLicenseViewModal payload = new GetLicenseViewModal();
	  
	  public GetLicenseViewModal getPayload() {
			return this.payload;
		}
	  
	  public void setPayload(final GetLicenseViewModal payload) {
			this.payload = payload; 
		}
}
