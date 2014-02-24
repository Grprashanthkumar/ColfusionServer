/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.List;

import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;

/**
 * @author Evgeny
 *
 */
public class UsersDAOImpl extends GenericDAOImpl<ColfusionUsers, Integer> implements UsersDAO {

	@Override
	public List<ColfusionUsers> lookUpUser(String searchTerm, int limit) {
		String sql = "select u from ColfusionUsers u where u.userLogin like :searchTerm or u.userNames like :searchTerm";
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("searchTerm", "%" + searchTerm + "%").setMaxResults(limit);
		
		return this.findMany(query);
	}
}
