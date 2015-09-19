/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.managers.UserManager;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.utils.MappingUtils;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryAuthorViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.AuthorsResponse;

/**
 * @author Evgeny
 *
 */
public class UserBL {
	final Logger logger = LogManager.getLogger(UserBL.class.getName());
	
	/**
	 * Looks up users who contain search term either in their names or login. Return up to specified limit of users.
	 * 
	 * @param searchTerm to look for in users names.
	 * @param limit up to how many users to return.
	 * @return the response model {@link AuthorsResponse} which has payload with found users as {@link List} of {@link StoryAuthorViewModel}.
	 */
	public AuthorsResponse lookUpAuthors(final String searchTerm, final int limit) {
		logger.info(String.format("started to look up up to %d users which contain %s search term", limit, searchTerm));
		
		AuthorsResponse result = new AuthorsResponse();
		
		result.isSuccessful = true;
		result.message = "OK";
		
		if (searchTerm.length() == 0 || limit == 0) {
			result.message = "Search term is empty or limit is set to 0";
			return result;
		}
		
		try {
			UserManager userMng = new UserManagerImpl();
			
			List<ColfusionUsers> users = userMng.lookUpUser(searchTerm, limit);
			
			for (ColfusionUsers user : users) {
				StoryAuthorViewModel author = MappingUtils.getInstance().mapColfusionUserToStoryAuthorViewModel(user);
				
				result.getPayload().add(author);
			}
		} catch (Exception e) {
			logger.error(String.format("lookUpAuthors in UserBL failed for search term: %s and limit %d", searchTerm, limit), e);
			result.isSuccessful = false;
			result.message = String.format("lookUpAuthors in UserBL failed for search term: %s and limit %d", searchTerm, limit);		
		}
		
		logger.info(String.format("finished to look up and mapping up to %d users which contain %s search term", limit, searchTerm));
		
		return result;
	}
}
