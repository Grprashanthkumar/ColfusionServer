/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl.HistoryItem;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoMetadataEditHistory;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;

/**
 * @author Evgeny
 *
 */
public class SourceInfoMetadataEditHistoryDAOImpl extends GenericDAOImpl<ColfusionSourceinfoMetadataEditHistory, Integer> implements SourceInfoMetadataEditHistoryDAO {

	Logger logger = LogManager.getLogger(SourceInfoMetadataEditHistoryDAOImpl.class.getName());
	
	@Override
	public void saveHistoryIfChanged(int sid, int userId, String oldValue, String newValue, HistoryItem itemType, String reason) throws Exception {
		// If value was not changed, then we don't need to save anything
		if (newValue == null ) {
			return ;
		}
		
		if (newValue.equals(oldValue)) {
			return;
		}
				
		SourceInfoDAO sourceInfoDAO = new SourceInfoDAOImpl();
		ColfusionSourceinfo colfusionSourceinfo;
		try {
			colfusionSourceinfo = sourceInfoDAO.findByID(ColfusionSourceinfo.class, sid);
		} catch (Exception e) {
			logger.error(String.format("saveHistoryIfChanged failed on sourceInfoDAO.findByID(ColfusionSourceinfo.class, sid); for sid = %d", sid));
			
			throw e;
		}
		
		UsersDAO userDAO = new UsersDAOImpl();
		ColfusionUsers colfusionUsers;
		try {
			colfusionUsers = userDAO.findByID(ColfusionUsers.class, userId);
		} catch (Exception e) {
			logger.error(String.format("saveHistoryIfChanged failed on userDAO.findByID(ColfusionUsers.class, userId); for userId = %d", userId));
			
			throw e;
		}
		
		try {
			this.save(new ColfusionSourceinfoMetadataEditHistory(colfusionSourceinfo, colfusionUsers, new Date(), itemType.getValue(), reason, newValue));
		} catch (Exception e) {
			logger.error(String.format("saveHistoryIfChanged failed on save for sid = %d, userId = %d", sid, userId));
			
			throw e;
		}		
	}

}
