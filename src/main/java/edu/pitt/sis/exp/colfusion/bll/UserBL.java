/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.managers.UserManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.responseModels.AuthorsResponse;
import edu.pitt.sis.exp.colfusion.utils.MappingUtils;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryAuthorViewModel;

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
	public AuthorsResponse lookUpAuthors(String searchTerm, int limit) {
		UserManager userMng = new UserManagerImpl();
		
		logger.info(String.format("started to look up up to %d users which contain %s search term", limit, searchTerm));
		
		List<ColfusionUsers> users = userMng.lookUpUser(searchTerm, limit);
		
		AuthorsResponse result = new AuthorsResponse();
		
		for (ColfusionUsers user : users) {
			StoryAuthorViewModel author = MappingUtils.getInstance().mapColfusionUserToStoryAuthorViewModel(user);
			
			result.getPayload().add(author);
		}
		
		logger.info(String.format("finished to look up and mapping up to %d users which contain %s search term", limit, searchTerm));
		
		return result;
	}
}
