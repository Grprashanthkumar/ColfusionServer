/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dao.OpenRefineProjectMapDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionOpenrefineProjectMap;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionOpenrefineProjectMapId;

/**
 * @author xxl
 *
 */
public class OpenRefineProjectMapManagerImpl extends GeneralManagerImpl<ColfusionOpenrefineProjectMap, ColfusionOpenrefineProjectMapId>
		implements OpenRefineProjectMapManager {

Logger logger = LogManager.getLogger(OpenRefineProjectMapManagerImpl.class.getName());
	
	public OpenRefineProjectMapManagerImpl() {
		super(new OpenRefineProjectMapDAOImpl(), ColfusionOpenrefineProjectMap.class);
	}
}
