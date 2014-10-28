package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dao.ServicesDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;

/**
 * @author Hao Bai
 *
 */
public class ServiceManagerImpl extends GeneralManagerImpl<ColfusionServices, Integer> implements ServiceManager{

	Logger logger = LogManager.getLogger(ServiceManagerImpl.class.getName());
	
	public ServiceManagerImpl() {
		super(new ServicesDAOImpl(), ColfusionServices.class);
	}

	//***************************************
	// From ServiceManager interface
	//***************************************
	
}