package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.StoryDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.StoryDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionStory;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

import org.apache.logging.log4j.*;

public class StoryManagerImpl extends GeneralManagerImpl<StoryDAO, ColfusionStory, Integer> 
        implements StoryManager {
	
   Logger logger = LogManager.getLogger(CanvasesManagerImpl.class.getName());

	
	public StoryManagerImpl() {
		super(new StoryDAOImpl(), ColfusionStory.class);
	}
	public ColfusionStory createNewStory(String did, String dname, String tname, ColfusionCanvases canvases){
		ColfusionStory newStory = new ColfusionStory(new Date());
		newStory.setDid(did);
		newStory.setDname(dname);
		newStory.setTname(tname);
		newStory.setCanvases(canvases);
		
		try{
            HibernateUtil.beginTransaction();
			
			_dao.save(newStory);
			
			HibernateUtil.commitTransaction();
		}
		catch (Exception ex) {
			HibernateUtil.rollbackTransaction();
			
			this.logger.error("getTableInfo failed HibernateException", ex);
			throw ex;
		}
	    
		return newStory;
	}
	
	public List<ColfusionStory> showAllByCanvasId(ColfusionCanvases canvases){
		try {
			HibernateUtil.beginTransaction();
			
			String hql = "SELECT S FROM Story S WHERE S.canvases = :canvases";
			
			Query query = HibernateUtil.getSession().createQuery(hql);
			query.setParameter("canvases", canvases);
			
			List<ColfusionStory> result = _dao.findMany(query);
			
			HibernateUtil.commitTransaction();
			
			return result;
		} catch (Exception ex) {
			 HibernateUtil.rollbackTransaction();
		     this.logger.error("Cannot find the records", ex);
			 throw ex;
		}
	}
}
