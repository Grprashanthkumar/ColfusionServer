/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoTableKTRDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtrId;

/**
 * @author Evgeny
 *
 */
public class SourceInfoTableKTRManagerImpl extends GeneralManagerImpl<ColfusionSourceinfoTableKtr, ColfusionSourceinfoTableKtrId> 
	implements SourceInfoTableKTRManager {

	Logger logger = LogManager.getLogger(SourceInfoTableKTRManagerImpl.class.getName());
	
	public SourceInfoTableKTRManagerImpl() {
		super(new SourceInfoTableKTRDAOImpl(), ColfusionSourceinfoTableKtr.class);
	}
}