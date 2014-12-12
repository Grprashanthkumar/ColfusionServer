/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.utils.HibernateUtil;

/**
 * @author Evgeny
 *
 */
public class RelationshipsDAOImpl extends GenericDAOImpl<ColfusionRelationships, Integer> implements
		RelationshipsDAO {
	
	Logger logger = LogManager.getLogger(RelationshipsDAOImpl.class.getName());

	@Override
	public List<ColfusionRelationships> findRelationshipByStatus(Integer status) {
		
		String sql = "select rel from ColfusionRelationships rel where rel.status = :status";
		Query query =null;
		try {
			//HibernateUtil.beginTransaction();
			query = HibernateUtil.getSession().createQuery(sql).setParameter("status",status);
		}catch (Exception e){
			logger.error(String.format("findRelationshipsByStatus failed on HibernateUtil.getSession().... for status = %s ", status), e);
			throw e;
		}
		return this.findMany(query);
	}

}
