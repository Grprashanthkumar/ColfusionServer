/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionTags;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author Evgeny
 *
 */
public class TagsDAOImpl extends GenericDAOImpl<ColfusionTags, Integer> implements TagsDAO {
	
	Logger logger = LogManager.getLogger(TagsDAOImpl.class.getName());

	/**
	 * Delete all tags from the tags table of a specific story specified by sid.
	 * 
	 * @param sid of the story for which to delete all tags.
	 * @throws Exception 
	 */
	@Override
	public void deleteAllBySid(final int sid) throws Exception {
		
		String sql = "DELETE FROM ColfusionTags ct WHERE ct.id.tagLinkId = :sid";
	   		
		logger.info(String.format("Starting processing deleteAllBySid for %s", sid));
		
		Query query = null;
		try {
			query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sid);
		} catch (Exception e) {
			logger.error(String.format("deleteAllBySid faild on HibernateUtil.getSession().createQuery(sql).setParameter(sid, sid); for sid = %d", sid), e);
			
			throw e;
		}
		int numberOfDeletedTags = query.executeUpdate();
        
        logger.info(String.format("Finish processing deleteAllBySid for %s, %s tags were deleted", sid, numberOfDeletedTags));
	}

}
