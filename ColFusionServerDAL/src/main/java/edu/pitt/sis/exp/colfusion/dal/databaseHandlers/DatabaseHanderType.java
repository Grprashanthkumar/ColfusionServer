/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.databaseHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public enum DatabaseHanderType {
	MYSQL("mysql"), MSSQL("mssql"), POSTGRESQL("postgresql"), LINKEDSERVER("linked server");
    
	private static Logger logger = LogManager.getLogger(DatabaseHanderType.class.getName());
	
    private String value;

    private DatabaseHanderType(String value) {
            this.value = value;
    }
    
    public String getValue(){
    	return this.value;
    }
    
    static public boolean isMember(String enumValueToTest) {
    	DatabaseHanderType[] enumValues = DatabaseHanderType.values();
        for (DatabaseHanderType enumValue : enumValues)
            if (enumValue.value.equals(enumValueToTest))
                return true;
        return false;
    }
    
    /**
     * Gets the enum by the text value of it's item.
     * @param text is the value of the enum item.
     * @return the enum item which matches the value with the given text.
     */
    public static DatabaseHanderType fromString(String text) {
        if (text != null) {
          for (DatabaseHanderType b : DatabaseHanderType.values()) {
            if (text.equalsIgnoreCase(b.value)) {
              return b;
            }
          }
        }
        
        logger.error("No constant with text " + text + " found");
        throw new IllegalArgumentException("No constant with text " + text + " found");
      }
}
