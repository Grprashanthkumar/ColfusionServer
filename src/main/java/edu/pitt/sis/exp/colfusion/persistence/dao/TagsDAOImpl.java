/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionTags;

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
	 */
	@Override
	public void deleteAllBySid(int sid) {
		
		String sql = "DELETE FROM ColfusionTags ct WHERE ct.id.tagLinkId = :sid";
	   		
		logger.info(String.format("Starting processing deleteAllBySid for %s", sid));
		
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sid);
		int numberOfDeletedTags = query.executeUpdate();
        
        logger.info(String.format("Finish processing deleteAllBySid for %s, %s tags were deleted", sid, numberOfDeletedTags));
	}

}
