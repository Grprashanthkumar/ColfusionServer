/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.HibernateUtil;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionTagCache;

/**
 * @author Evgeny
 *
 */
public class TagsCacheDAOImpl extends GenericDAOImpl<ColfusionTagCache, Integer> implements TagsCacheDAO {

	Logger logger = LogManager.getLogger(TagsCacheDAOImpl.class.getName());
	
	/**
	 * Deletes all tag cached records from tag cache table.
	 * @throws Exception 
	 */
	@Override
	public void deleteAll() throws Exception {
		String sql = "DELETE FROM ColfusionTagCache";
   		
		logger.info(String.format("Starting processing deleteAll"));
		
		Query query = null;
		try {
			query = HibernateUtil.getSession().createQuery(sql);
		} catch (Exception e) {
			logger.error(String.format("deleteAll faild on HibernateUtil.getSession().createQuery(sql); for sql = %s", sql), e);
			
			throw e;
		}
		int numberOfDeletedTags = query.executeUpdate();
        
        logger.info(String.format("Finish processing deleteAll, %s tags were deleted", numberOfDeletedTags));
	}

}
