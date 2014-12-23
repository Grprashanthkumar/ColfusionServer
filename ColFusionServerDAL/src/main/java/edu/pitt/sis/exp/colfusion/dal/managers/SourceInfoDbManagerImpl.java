package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDBDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDBDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;

public class SourceInfoDbManagerImpl extends GeneralManagerImpl<SourceInfoDBDAO, ColfusionSourceinfoDb, Integer>
		implements SourceInfoDbManager {

	Logger logger = LogManager.getLogger(SourceInfoDbManagerImpl.class.getName());
	
	public SourceInfoDbManagerImpl() {
		super(new SourceInfoDBDAOImpl(), ColfusionSourceinfoDb.class);
		// TODO Auto-generated constructor stub
	}

}
