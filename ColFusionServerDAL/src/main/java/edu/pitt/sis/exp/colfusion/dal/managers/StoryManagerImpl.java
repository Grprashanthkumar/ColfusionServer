package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.LicenseViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryListViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.UserViewModel;

public class StoryManagerImpl extends GeneralManagerImpl<ColfusionSourceinfo, Integer> implements StoryManager{
	
	Logger logger = LogManager.getLogger(StoryManagerImpl.class.getName());

	public StoryManagerImpl() {
		super(new SourceInfoDAOImpl(), ColfusionSourceinfo.class);
	}

	@Override
	public List<StoryListViewModel> getStoryListViewModel(int pageNo, int perPage) {
		try{
			HibernateUtil.beginTransaction();
			String hql = "SELECT src.sid, src.title, cus.userId, cus.userLogin, src.path, src.entryDate, src.lastUpdated, src.status, src.rawDataPath, src.sourceType, cli.licenseId, cli.licenseName, cli.licenseUrl " 
	                  + "FROM ColfusionSourceinfo src join src.colfusionUsers cus join src.colfusionLicense cli";
	        Query query = HibernateUtil.getSession().createQuery(hql);
	        query.setFirstResult((pageNo-1)*perPage);
	        query.setMaxResults(perPage);
	        List<Object> storyListObjs = query.list();
	        List<StoryListViewModel> result = new ArrayList<>();
	        for (Object storyListObj : storyListObjs) {
	        	Object[] storyColumns = (Object[]) storyListObj;
	        	StoryListViewModel storyListViewModel = new StoryListViewModel();
	        	storyListViewModel.setSid((int)storyColumns[0]);
	        	storyListViewModel.setTitle((String)storyColumns[1]);
	        	UserViewModel userViewModel = new UserViewModel();
	        	userViewModel.setUserId((int)storyColumns[2]);
	        	userViewModel.setUserLogin((String)storyColumns[3]);
	        	storyListViewModel.setUser(userViewModel);
	        	storyListViewModel.setPath((String)storyColumns[4]);
	        	storyListViewModel.setEntryDate((Date)storyColumns[5]);
	        	storyListViewModel.setLastUpdated((Date)storyColumns[6]);
	        	storyListViewModel.setStatus((String)storyColumns[7]);
	        	storyListViewModel.setRawDataPath((String)storyColumns[8]);
	        	storyListViewModel.setSourceType((String)storyColumns[9]);
	        	LicenseViewModel licenseViewModel = new LicenseViewModel();
	        	licenseViewModel.setLicenseId((int)storyColumns[10]);
	        	licenseViewModel.setLicenseName((String)storyColumns[11]);
	        	licenseViewModel.setLicenseURL((String)storyColumns[12]);
	        	storyListViewModel.setLicense(licenseViewModel);
	        	result.add(storyListViewModel);
	        }  
	        return result;
  
		} catch (NonUniqueResultException ex) {
	
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("getStoryListViewModel failed NonUniqueResultException", ex);
	        throw ex;
	    } catch (HibernateException ex) {
	
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("getStoryListViewModel failed HibernateException", ex);
	    	throw ex;
	    }	
	}

}
