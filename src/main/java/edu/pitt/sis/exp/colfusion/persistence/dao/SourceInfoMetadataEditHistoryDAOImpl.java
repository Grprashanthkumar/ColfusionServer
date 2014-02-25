/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl.HistoryItem;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoMetadataEditHistory;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;

/**
 * @author Evgeny
 *
 */
public class SourceInfoMetadataEditHistoryDAOImpl extends GenericDAOImpl<ColfusionSourceinfoMetadataEditHistory, Integer> implements SourceInfoMetadataEditHistoryDAO {

	Logger logger = LogManager.getLogger(SourceInfoMetadataEditHistoryDAOImpl.class.getName());
	
	@Override
	public void saveHistoryIfChanged(int sid, int userId, String oldValue, String newValue, HistoryItem itemType, String reason) {
		// If value was not changed, then we don't need to save anything
		if (newValue == null ) {
			return ;
		}
		
		if (newValue.equals(oldValue)) {
			return;
		}
				
		SourceInfoDAO sourceInfoDAO = new SourceInfoDAOImpl();
		ColfusionSourceinfo colfusionSourceinfo = sourceInfoDAO.findByID(ColfusionSourceinfo.class, sid);
		
		UsersDAO userDAO = new UsersDAOImpl();
		ColfusionUsers colfusionUsers = userDAO.findByID(ColfusionUsers.class, userId);
		
		this.save(new ColfusionSourceinfoMetadataEditHistory(colfusionSourceinfo, colfusionUsers, new Date(), itemType.getValue(), reason, newValue));		
	}

}
