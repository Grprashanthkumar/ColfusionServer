package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.ChartsDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.ChartsDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCharts;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionStory;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

public class ChartsManagerImpl extends GeneralManagerImpl<ChartsDAO, ColfusionCharts, Integer> implements ChartsManager{
   Logger logger = LogManager.getLogger(CanvasesManagerImpl.class.getName());
	
   public ChartsManagerImpl() {
		super(new ChartsDAOImpl(), ColfusionCharts.class);
	}
   
   public ColfusionCharts createNewChart(String type, String did, String dname, String tname, String columns, ColfusionStory story){
	   ColfusionCharts newChart = new ColfusionCharts();
	   newChart.setType(type);
	   newChart.setDid(did);
	   newChart.setDname(dname);
	   newChart.setTname(tname);
	   newChart.setColumns(columns);
	   newChart.setStory(story);
	   
	   try{
		   HibernateUtil.beginTransaction();
		   
		   _dao.save(newChart);
		   
		   HibernateUtil.commitTransaction();
	   }
	   catch(HibernateException ex){
		   this.logger.error("getTableInfo failed HibernateException", ex);
		   throw ex;
	   }
	   
	   return newChart;
   }
   
   public List<ColfusionCharts> showAllByStoryId(ColfusionStory story){
	   try {
			HibernateUtil.beginTransaction();
			
			String hql = "SELECT C FROM Charts C WHERE C.story = :story";
			
			Query query = HibernateUtil.getSession().createQuery(hql);
			query.setParameter("story", story);
			
			List<ColfusionCharts> result = _dao.findMany(query);
			
			for(ColfusionCharts chart: result){
				Hibernate.initialize(chart.getStory());
			}
			
			HibernateUtil.commitTransaction();
			
			return result;
		} catch (Exception ex) {
			 HibernateUtil.rollbackTransaction();
		     this.logger.error("Cannot find the records", ex);
			 throw ex;
		}
   }
}
