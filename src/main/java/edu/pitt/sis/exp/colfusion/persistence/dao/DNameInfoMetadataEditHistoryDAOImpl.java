/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.Date;

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
	

	@Override
	public void saveHistoryIfChanged(Integer cid, int userId, String oldValue, String newValue, VariableMetadataHistoryItem editedAttribute, String reason) {
		// If value was not changed, then we don't need to save anything
		if (newValue == null ) {
			return ;
		}
		
		if (newValue.equals(oldValue)) {
			return;
		}
				
		DNameInfoDAO dNameInfoDAO = new DNameInfoDAOImpl();
		ColfusionDnameinfo dNameInfo = dNameInfoDAO.findByID(ColfusionDnameinfo.class, cid);
		
		UsersDAO userDAO = new UsersDAOImpl();
		ColfusionUsers colfusionUsers = userDAO.findByID(ColfusionUsers.class, userId);
		
		this.save(new ColfusionDnameinfoMetadataEditHistory(dNameInfo, colfusionUsers, new Date(), editedAttribute.getValue(), reason, newValue));	
	}
}
