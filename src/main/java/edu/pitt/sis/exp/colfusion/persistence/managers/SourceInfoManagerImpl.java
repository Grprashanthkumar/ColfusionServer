/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;

/**
 * @author Evgeny
 *
 */
public class SourceInfoManagerImpl implements SourceInfoManager {

	Logger logger = LogManager.getLogger(SourceInfoManagerImpl.class.getName());
	
	private SourceInfoDAO sourceInfoDAO = new SourceInfoDAOImpl();
	
	@Override
	public List<ColfusionSourceinfo> loadAllSourceinfos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveNewSourceinfo(ColfusionSourceinfo sourceInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ColfusionSourceinfo findSourceinfoById(BigDecimal sid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteSourceinfo(ColfusionSourceinfo sourceInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ColfusionSourceinfo> findDatasetsInfoByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColfusionSourceinfo> findDatasetInfoBySidOrTitle(
			String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColfusionSourceinfo findDatasetInfoBySid(int sid, boolean includeDraft) {
		ColfusionSourceinfo sourceinfo = null;
        try {
            HibernateUtil.beginTransaction();
            
            sourceinfo = sourceInfoDAO.findDatasetInfoBySid(sid, includeDraft);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        }
        return sourceinfo;
	}

	@Override
	public List<ColfusionSourceinfo> findDatasetsInfoByTitle(String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
