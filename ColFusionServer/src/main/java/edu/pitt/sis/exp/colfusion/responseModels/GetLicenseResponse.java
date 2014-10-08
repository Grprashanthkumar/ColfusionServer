package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;

public class GetLicenseResponse extends GeneralResponseImpl {
	private List<ColfusionLicense> payload = new ArrayList<ColfusionLicense>();
	  
	  public List<ColfusionLicense> getPayload() {
			return this.payload;
		}
	  
	  public void setPayload(final List<ColfusionLicense> payload) {
			this.payload = payload; 
		}
}
