/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.process;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public enum ProcessStatusEnum {
	NEW("new"), RUNNING("running"), FAILED("failed"), DONE("done");
    
	private static Logger logger = LogManager.getLogger(ProcessStatusEnum.class.getName());
	
    private String value;

    private ProcessStatusEnum(String value) {
            this.value = value;
    }
    
    public String getValue(){
    	return this.value;
    }
    
    static public boolean isMember(String enumValueToTest) {
    	ProcessStatusEnum[] enumValues = ProcessStatusEnum.values();
        for (ProcessStatusEnum enumValue : enumValues)
            if (enumValue.value.equals(enumValueToTest))
                return true;
        return false;
    }
    
    /**
     * Gets the enum by the text value of it's item.
     * @param text is the value of the enum item.
     * @return the enum item which matches the value with the given text.
     */
    public static ProcessStatusEnum fromString(String text) {
        if (text != null) {
          for (ProcessStatusEnum b : ProcessStatusEnum.values()) {
            if (text.equalsIgnoreCase(b.value)) {
              return b;
            }
          }
        }
        
        logger.error("No constant with text " + text + " found");
        throw new IllegalArgumentException("No constant with text " + text + " found");
      }
}
