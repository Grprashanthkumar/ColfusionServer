package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;

public class GetLicenseViewModal {
	private List<ColfusionLicense> licenseList;
	
	public GetLicenseViewModal(){
		this.setLicenseList(new ArrayList<ColfusionLicense>());
	}
	
	public List<ColfusionLicense> getLicenseList(){
		return this.licenseList;
	}
	
	public void setLicenseList(final List<ColfusionLicense> licenseList){
		this.licenseList = licenseList;
	}
}
