/**
 * 
 */
package edu.pitt.sis.exp.colfusion.utils;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.PropertyKeys;

/**
 * @author Evgeny
 *
 */
public class StoryUtils {
	
	/**
	 * Generates linked server name for given story.
	 * @param sid is of the story.
	 * @return linked server name.
	 */
	public static String generateLinkedServerName(int sid) {
		return ConfigManager.getInstance().getPropertyByName(PropertyKeys.targetFileToDBDatabase_DatabaseNamePrefix) + sid;
	}
}
