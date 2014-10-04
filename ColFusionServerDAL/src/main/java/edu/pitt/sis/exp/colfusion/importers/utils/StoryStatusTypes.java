/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public enum StoryStatusTypes {
	DRAFT("draft"), QUEUED("queued");
    
	private static Logger logger = LogManager.getLogger(StoryStatusTypes.class.getName());
	
    private String value;

    private StoryStatusTypes(String value) {
            this.value = value;
    }
    
    public String getValue(){
    	return this.value;
    }
    
    static public boolean isMember(String enumValueToTest) {
    	StoryStatusTypes[] enumValues = StoryStatusTypes.values();
        for (StoryStatusTypes enumValue : enumValues)
            if (enumValue.value.equals(enumValueToTest))
                return true;
        return false;
    }
    
    /**
     * Gets the enum by the text value of it's item.
     * @param text is the value of the enum item.
     * @return the enum item which matches the value with the given text.
     */
    public static StoryStatusTypes fromString(String text) {
        if (text != null) {
          for (StoryStatusTypes b : StoryStatusTypes.values()) {
            if (text.equalsIgnoreCase(b.value)) {
              return b;
            }
          }
        }
        
        logger.error("No constant with text " + text + " found");
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
