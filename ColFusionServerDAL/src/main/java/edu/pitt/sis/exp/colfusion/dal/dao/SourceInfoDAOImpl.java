/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

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
		String sql = "select DISTINCT si from ColfusionSourceinfo si join si.colfusionUsers cu left outer join si.colfusionLicense cl right outer join si.colfusionSourceinfoUsers csu where cu.userId = :userId order by si.sid DESC";
		Query query = null;
		try {
			query = HibernateUtil.getSession().createQuery(sql).setParameter("userId", userId);
		} catch (Exception e) {
			logger.error(String.format("findDatasetsInfoByUserId failed on HibernateUtil.getSession().... for userId = %d", userId), e);
			
			throw e;
		}
		
		return this.findMany(query);
	}

	@Override
	public List<ColfusionSourceinfo> findDatasetInfoBySidOrTitle(final String searchTerm) {
		
		ColfusionSourceinfo sourceInfo = null;
		String hql = "";
		
		Query query = null;
		try {
			hql = "SELECT si FROM ColfusionSourceinfo si WHERE si.title like '%"+searchTerm+"%'";	
			query = HibernateUtil.getSession().createQuery(hql);
//			hql = "SELECT si FROM ColfusionSourceinfo si WHERE si.title like :title";		
//			query = HibernateUtil.getSession().createQuery(hql).setParameter("title", searchTerm);
		} catch (Exception e) {
			logger.error(String.format("findDatasetInfoBySid failed on HibernateUtil.getSession().createQuery(sql).setParameter(title, title); for title = %s", searchTerm), e);
			
			throw e;
		}
    
        List<ColfusionSourceinfo> results = findMany(query);
		return results;
	}

	//TODO FIXME DON'T USE TRANSACTIONS ON THIS LEVEL
	@Override
	public List<ColfusionSourceinfo> findOneSidsTrue() throws HibernateException{
		List<ColfusionSourceinfo> returnList = new ArrayList<ColfusionSourceinfo>();
		String sql = "select {cs.*} from Colfusion_sourceinfo cs where Status<>'draft' and cs.sid not in (Select sid1 from Colfusion_relationships union Select sid2 from Colfusion_relationships)";
	
		logger.info(String.format("Starting processing findOneSidsTrue "));
		
		SQLQuery query = null;
		try {
			HibernateUtil.beginTransaction();
			query = this.getSession().createSQLQuery(sql);
			query.addEntity("cs",ColfusionSourceinfo.class);
			returnList =findMany(query);
			HibernateUtil.commitTransaction();
		} catch (Exception e) {
			logger.error(String.format("findOneSidsTrue failed on HibernateUtil.getSession().createQuery(sql)"), e);
			throw e;
		}
     
        logger.info(String.format("Finish processing findOneSidsTrue"));
		
        return returnList;
	}
	
	@Override
	public ColfusionSourceinfo findDatasetInfoBySid(final int sid, final boolean includeDraft) throws HibernateException {
		
		ColfusionSourceinfo sourceInfo = null;
		String sql = "";
		if (includeDraft) {
			sql = "SELECT si FROM ColfusionSourceinfo si WHERE si.sid = :sid and (Status = 'queued' or Status = 'draft' or Status = 'private')";
	    }
		else {
			sql = "SELECT si FROM ColfusionSourceinfo si WHERE si.sid = :sid and (Status = 'queued' or Status = 'private')";
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

	@Override
	public List<ColfusionSourceinfo> findSourceInfoByStatus(final SourceInfoStatus status) throws HibernateException{
		String sql = "select si from ColfusionSourceinfo si where si.status = :status";
		Query query = null;
		try {
			query = HibernateUtil.getSession().createQuery(sql).setParameter("status", status.getDatabaseStatusValue());
			
			List<ColfusionSourceinfo> result = this.findMany(query);
			
			HibernateUtil.commitTransaction();
			
			return result;
		}catch (Exception e){
			logger.error(String.format("findSourceInfoByStatus failed on HibernateUtil.getSession().... for status = %s ", status.getDatabaseStatusValue()), e);
			throw e;
		}		
	}
}
