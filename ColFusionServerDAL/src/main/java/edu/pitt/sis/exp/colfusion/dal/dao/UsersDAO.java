/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.hibernate.HibernateException;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;

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
	
	/**
	 * @author Hao Bai
	 * Query users' email address.
	 * 
	 * @param userLevel to find to be contained in user level.
	 * @return users' email addresses. 
	 * @throws Exception 
	 */
	public List<String> queryUserEmails(String userLevel) throws HibernateException;
}
