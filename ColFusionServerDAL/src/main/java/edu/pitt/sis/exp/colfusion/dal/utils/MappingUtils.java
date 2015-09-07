/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryAuthorViewModel;

public class MappingUtils {
	
	final Logger logger = LogManager.getLogger(MappingUtils.class.getName());
	
	private static MappingUtils instance = null;
	
	protected MappingUtils() {
		
	}
	
	public static MappingUtils getInstance() {
		if(instance == null) {
	         instance = new MappingUtils();
	    }
	  
		return instance;
	}
	
	/**
	 * Maps {@link ColfusionUsers} to {@link StoryAuthorViewModel}. Only maps first level fields. The roleId is not set.
	 * @param user the user of type {@link ColfusionUsers} from which to extract the information.
	 * @return {@link StoryAuthorViewModel} which holds information from the extracted user.
	 */
	public StoryAuthorViewModel mapColfusionUserToStoryAuthorViewModel(final ColfusionUsers user) {
		StoryAuthorViewModel result = new StoryAuthorViewModel();
		
		result.setUserId(user.getUserId());
		result.setAvatarSource(user.getUserAvatarSource());
		result.setKarma(user.getUserKarma());
		result.setLogin(user.getUserLogin());
		
		//Currently in the database user real name is stored as one value: Last Name, First Name
		// Some user might have provided only one of them, so here I am trying to split database value in the Last Name and First name.
		result.setFirstName("");
		result.setLastName("");
		if (user.getUserNames() != null) {
			String[] names = user.getUserNames().split(",");
			if (names.length == 2) {
				result.setFirstName(names[1]);
				result.setLastName(names[0]);
			}
			else if (names.length == 1) {
				result.setLastName(names[0]);
			}
		}
		
		return result;
	}
}
