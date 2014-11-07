/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoTableKTRDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoTableKTRDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoTableKtrId;

/**
 * @author Evgeny
 *
 */

public class SourceInfoTableKTRManagerImpl extends GeneralManagerImpl<SourceInfoTableKTRDAO, ColfusionSourceinfoTableKtr, ColfusionSourceinfoTableKtrId> 
	implements SourceInfoTableKTRManager {

	Logger logger = LogManager.getLogger(SourceInfoTableKTRManagerImpl.class.getName());
	
	public SourceInfoTableKTRManagerImpl() {
		super(new SourceInfoTableKTRDAOImpl(), ColfusionSourceinfoTableKtr.class);
	}
}
