package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dao.ChartsDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.ChartsDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCharts;

public class ChartsManagerImpl extends GeneralManagerImpl<ChartsDAO, ColfusionCharts, Integer> implements ChartsManager{
   Logger logger = LogManager.getLogger(CanvasesManagerImpl.class.getName());
	
   public ChartsManagerImpl() {
		super(new ChartsDAOImpl(), ColfusionCharts.class);
	}
}
