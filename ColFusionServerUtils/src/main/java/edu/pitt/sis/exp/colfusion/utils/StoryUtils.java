/**
 * 
 */
package edu.pitt.sis.exp.colfusion.utils;

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
	public static String generateLinkedServerName(final int sid) {
		return ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_DATA_FROM_FILE_DATABASE_DATABASE_NAME_PREFIX) + sid;
	}
}
