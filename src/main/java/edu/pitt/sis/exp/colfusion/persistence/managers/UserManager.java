/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;

/**
 * @author Evgeny
 *
 */
public interface UserManager extends GeneralManager<ColfusionUsers, Integer> {
	
	/**
	 * 
	 * Finds users who has either first or last name, or username containing searchTerm.
	 * 
	 * @param searchTerm to find to be contained in user name.
	 * @param limit the number of users that should be returned.
	 * @return user who satisfy search term. 
	 */
	public List<ColfusionUsers> lookUpUser(String searchTerm, int limit);
	
}
