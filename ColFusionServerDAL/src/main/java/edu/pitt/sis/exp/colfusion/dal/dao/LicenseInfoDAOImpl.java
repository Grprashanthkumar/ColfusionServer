package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

public class LicenseInfoDAOImpl extends GenericDAOImpl<ColfusionLicense,Integer> implements LicenseInfoDAO {
	@Override
	public List<ColfusionLicense> getLicense(){
		List<ColfusionLicense> result = new ArrayList<ColfusionLicense>();
		result = this.findAll(ColfusionLicense.class);
		return result;
	}
}	



