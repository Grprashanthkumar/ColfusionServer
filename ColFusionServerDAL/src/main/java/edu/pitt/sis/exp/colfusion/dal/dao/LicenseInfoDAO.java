package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;

public interface LicenseInfoDAO extends GenericDAO<ColfusionLicense,Integer>{
	public List<ColfusionLicense> getLicense() ;
}