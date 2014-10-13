package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.LicenseInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.LicenseInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfoMetadataEditHistory;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.dal.utils.MappingUtils;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataHistoryLogRecordViewModel;

public class LicenseInfoManagerImpl extends GeneralManagerImpl<ColfusionLicense,Integer> implements LicenseInfoManager {
	//Logger logger = LogManager.getLogger(SourceInfoManagerImpl.class.getName());
	public LicenseInfoManagerImpl(){
		super(new LicenseInfoDAOImpl(),ColfusionLicense.class);
	}
	
	@Override
	public List<ColfusionLicense> getLicenseFromDB(){
		try{
			HibernateUtil.beginTransaction();
			List<ColfusionLicense> returnList = new ArrayList<ColfusionLicense>();
			returnList =((LicenseInfoDAO)this._dao).getLicense();
			return returnList;
		}catch(Exception e){
			System.out.println("LicenseInfoManager error license");
		}
		return null;
	}
	

}
