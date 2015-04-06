package edu.pitt.sis.exp.colfusion.dal.managers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dao.LocationIndexDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.LocationIndexDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionIndexlocation;


public class LocationIndexManagerImpl extends GeneralManagerImpl<LocationIndexDAO, ColfusionIndexlocation, Integer>  implements LocationIndexManager {

	Logger logger = LogManager.getLogger(LocationIndexDAOImpl.class.getName());
	
	public LocationIndexManagerImpl() {
		super(new LocationIndexDAOImpl(), ColfusionIndexlocation.class);
	}
	
	

}
