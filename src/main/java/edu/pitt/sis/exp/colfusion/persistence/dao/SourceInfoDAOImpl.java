/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;

/**
 * @author Evgeny
 *
 */
public class SourceInfoDAOImpl extends GenericDAOImpl<ColfusionSourceinfo, Integer> implements SourceInfoDAO {

	Logger logger = LogManager.getLogger(SourceInfoDAOImpl.class.getName());
	
	
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
		
		ColfusionSourceinfo sourceInfo = null;
		String sql = "";
		if (includeDraft) {
			sql = "SELECT si FROM ColfusionSourceinfo si WHERE si.sid = :sid and (Status = 'queued' or Status = 'draft')";
	    }
		else {
			sql = "SELECT si FROM ColfusionSourceinfo si WHERE si.sid = :sid and Status = 'queued'";
		}
		
		logger.info(String.format("Starting processing findDatasetInfoBySid for %s with drafts included %s", sid, includeDraft));
		
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sid);
        sourceInfo = findOne(query);
        
        logger.info(String.format("Finish processing findDatasetInfoBySid for %s with drafts included %s", sid, includeDraft));
        
		return sourceInfo;
	}

	@Override
	public List<ColfusionSourceinfo> lookupStories(String searchTerm, int limit) {
		String sql = "select si from ColfusionSourceinfo si where si.title like :searchTerm";
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("searchTerm", "%" + searchTerm + "%").setMaxResults(limit);
		
		return this.findMany(query);
	}

}
