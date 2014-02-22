/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionTagCache;

/**
 * @author Evgeny
 *
 */
public class TagsCacheDAOImpl extends GenericDAOImpl<ColfusionTagCache, Integer> implements TagsCacheDAO {

	Logger logger = LogManager.getLogger(TagsCacheDAOImpl.class.getName());
	
	/**
	 * Deletes all tag cached records from tag cache table.
	 */
	@Override
	public void deleteAll() {
		String sql = "DELETE FROM ColfusionTagCache";
   		
		logger.info(String.format("Starting processing deleteAll"));
		
		Query query = HibernateUtil.getSession().createQuery(sql);
		int numberOfDeletedTags = query.executeUpdate();
        
        logger.info(String.format("Finish processing deleteAll, %s tags were deleted", numberOfDeletedTags));
	}

}
