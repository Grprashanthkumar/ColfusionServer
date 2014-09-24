/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.dao.OpenRefineProjectMapDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionOpenrefineProjectMap;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionOpenrefineProjectMapId;

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
