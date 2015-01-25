package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;

import edu.pitt.sis.exp.colfusion.dal.dao.ChartsDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.ChartsDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCharts;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

public class ChartsManagerImpl extends GeneralManagerImpl<ChartsDAO, ColfusionCharts, Integer> implements ChartsManager{
   Logger logger = LogManager.getLogger(CanvasesManagerImpl.class.getName());
	
   public ChartsManagerImpl() {
		super(new ChartsDAOImpl(), ColfusionCharts.class);
	}
   
   public ColfusionCharts createNewChart(ColfusionCanvases canvas, String name, String type){
	   ColfusionCharts newChart = new ColfusionCharts();
	   newChart.setColfusionCanvases(canvas);
	   newChart.setName(name);
	   newChart.setType(type);
	   
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
}
