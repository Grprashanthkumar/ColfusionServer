package edu.pitt.sis.exp.colfusion.dal.dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionServices;
/**
 * @author Hao Bai
 *
 */
public class ServicesDAOImpl extends GenericDAOImpl<ColfusionServices, Integer> implements ServicesDAO {

	Logger logger = LogManager.getLogger(ServicesDAOImpl.class.getName());
	
}
