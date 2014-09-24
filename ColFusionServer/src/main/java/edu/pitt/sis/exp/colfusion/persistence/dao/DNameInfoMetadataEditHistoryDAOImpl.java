/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManagerImpl.VariableMetadataHistoryItem;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionDnameinfoMetadataEditHistory;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;

/**
 * @author Evgeny
 *
 */
public class DNameInfoMetadataEditHistoryDAOImpl extends GenericDAOImpl<ColfusionDnameinfoMetadataEditHistory, Integer> 
	implements DNameInfoMetadataEditHistoryDAO {
	
	Logger logger = LogManager.getLogger(DNameInfoMetadataEditHistoryDAOImpl.class.getName());

	@Override
	public void saveHistoryIfChanged(Integer cid, int userId, String oldValue, String newValue, VariableMetadataHistoryItem editedAttribute, String reason) throws Exception {
		// If value was not changed, then we don't need to save anything
		if (newValue == null ) {
			return ;
		}
		
		if (newValue.equals(oldValue)) {
			return;
		}
				
		DNameInfoDAO dNameInfoDAO = new DNameInfoDAOImpl();
		ColfusionDnameinfo dNameInfo;
		try {
			dNameInfo = dNameInfoDAO.findByID(ColfusionDnameinfo.class, cid);
		} catch (Exception e) {
			logger.error(String.format("dNameInfoDAO.findByID in saveHistoryIfChanged failed for cid = %d", cid));
			
			throw e;
		}
		
		UsersDAO userDAO = new UsersDAOImpl();
		ColfusionUsers colfusionUsers;
		try {
			colfusionUsers = userDAO.findByID(ColfusionUsers.class, userId);
		} catch (Exception e) {
			logger.error(String.format("userDAO.findByID in saveHistoryIfChanged failed for userId = %d", userId));
			
			throw e;
		}
		
		try {
			this.save(new ColfusionDnameinfoMetadataEditHistory(dNameInfo, colfusionUsers, new Date(), editedAttribute.getValue(), reason, newValue));
		} catch (Exception e) {
			logger.error(String.format("save in saveHistoryIfChanged failed for old value = %s, reasond = %s, new value = %s", editedAttribute.getValue(), reason, newValue));
			
			throw e;
		}	
	}
}
