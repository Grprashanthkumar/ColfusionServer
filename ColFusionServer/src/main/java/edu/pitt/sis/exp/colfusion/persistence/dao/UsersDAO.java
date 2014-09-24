/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.List;

import org.hibernate.HibernateException;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;

/**
 * @author Evgeny
 *
 */
public interface UsersDAO extends GenericDAO<ColfusionUsers, Integer> {
	/**
	 * 
	 * Finds users who has either first or last name, or username containing searchTerm.
	 * 
	 * @param searchTerm to find to be contained in user name.
	 * @param limit the number of users that should be returned.
	 * @return user who satisfy search term. 
	 * @throws Exception 
	 */
	public List<ColfusionUsers> lookUpUser(String searchTerm, int limit) throws HibernateException;
}
