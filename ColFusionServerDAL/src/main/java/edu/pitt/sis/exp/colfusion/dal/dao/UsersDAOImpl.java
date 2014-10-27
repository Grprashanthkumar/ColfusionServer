/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author Evgeny
 *
 */
public class UsersDAOImpl extends GenericDAOImpl<ColfusionUsers, Integer> implements UsersDAO {

	Logger logger = LogManager.getLogger(UsersDAOImpl.class.getName());
	
	@Override
	public List<ColfusionUsers> lookUpUser(final String searchTerm, final int limit) throws HibernateException {
		String sql = "select u from ColfusionUsers u where u.userLogin like :searchTerm or u.userNames like :searchTerm";
		Query query = null;
		try {
			query = HibernateUtil.getSession().createQuery(sql).setParameter("searchTerm", "%" + searchTerm + "%").setMaxResults(limit);
		} catch (Exception e) {
			logger.error(String.format("lookUpUser failed on HibernateUtil.getSession().... for searhTerm = %s and limi = %d", searchTerm, limit), e);
			
			throw new HibernateException(e);
		}
		
		return this.findMany(query);
	}
	
	@Override
	public List<String> queryUserEmails(String userLevel) throws HibernateException{
		String hql = "SELECT userEmail FROM ColfusionUsers WHERE userLevel = '" + userLevel + "'";
		List<String> queryResult = null;
		
		try {		
			Query query = HibernateUtil.getSession().createQuery(hql);
	    	if(query != null){
	    		@SuppressWarnings("unchecked")
	    		List<String> tempQueryResult = (List<String>)query.list();
	    		queryResult = tempQueryResult;
	    	}
		} catch (Exception e) {
			logger.error(String.format("queryUserEmails failed on HibernateUtil.getSession()..."));
			
			throw new HibernateException(e);
		}
		
		return queryResult;
	}
}
