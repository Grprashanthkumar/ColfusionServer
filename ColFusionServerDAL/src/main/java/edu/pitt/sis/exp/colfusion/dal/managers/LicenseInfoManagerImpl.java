package edu.pitt.sis.exp.colfusion.dal.managers;

import edu.pitt.sis.exp.colfusion.dal.dao.LicenseInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.LicenseInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;

public class LicenseInfoManagerImpl extends GeneralManagerImpl<LicenseInfoDAO, ColfusionLicense,Integer> implements LicenseInfoManager {
	//Logger logger = LogManager.getLogger(SourceInfoManagerImpl.class.getName());
	public LicenseInfoManagerImpl(){
		super(new LicenseInfoDAOImpl(),ColfusionLicense.class);
	}
	
}
