/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author Evgeny
 *
 */
public class SourceInfoDAOImpl extends GenericDAOImpl<ColfusionSourceinfo, Integer> implements SourceInfoDAO {

	Logger logger = LogManager.getLogger(SourceInfoDAOImpl.class.getName());
	
	
	@Override
	public List<ColfusionSourceinfo> findDatasetsInfoByUserId(final int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColfusionSourceinfo> findDatasetInfoBySidOrTitle(
			final String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColfusionSourceinfo findDatasetInfoBySid(final int sid, final boolean includeDraft) throws HibernateException {
		
		ColfusionSourceinfo sourceInfo = null;
		String sql = "";
		if (includeDraft) {
			sql = "SELECT si FROM ColfusionSourceinfo si WHERE si.sid = :sid and (Status = 'queued' or Status = 'draft')";
	    }
		else {
			sql = "SELECT si FROM ColfusionSourceinfo si WHERE si.sid = :sid and Status = 'queued'";
		}
		
		logger.info(String.format("Starting processing findDatasetInfoBySid for %s with drafts included %s", sid, includeDraft));
		
		Query query = null;
		try {
			query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sid);
		} catch (Exception e) {
			logger.error(String.format("findDatasetInfoBySid failed on HibernateUtil.getSession().createQuery(sql).setParameter(sid, sid); for sid = %d", sid), e);
			
			throw e;
		}
        sourceInfo = findOne(query);
        
        logger.info(String.format("Finish processing findDatasetInfoBySid for %s with drafts included %s", sid, includeDraft));
        
		return sourceInfo;
	}

	@Override
	public List<ColfusionSourceinfo> lookupStories(final String searchTerm, final int limit) throws HibernateException {
		String sql = "select si from ColfusionSourceinfo si where si.title like :searchTerm";
		Query query = null;
		try {
			query = HibernateUtil.getSession().createQuery(sql).setParameter("searchTerm", "%" + searchTerm + "%").setMaxResults(limit);
		} catch (Exception e) {
			logger.error(String.format("lookupStories failed on HibernateUtil.getSession().... for searchTerm = %s and limit = %d", searchTerm, limit), e);
			
			throw e;
		}
		
		return this.findMany(query);
	}

}