package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;

public interface LicenseInfoManager extends GeneralManager<ColfusionLicense,Integer> {

	public List<ColfusionLicense> getLicenseFromDB();
}
