/**
 * 
 */
package edu.pitt.sis.exp.colfusion.importers.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This enumerator describes the types of the data sources. The values should correspond to the values written in the 
 * source_type field in the sourceinfo table.
 * 
 * @author Evgeny
 *
 */
//TODO: move it to other place
public enum DataSourceTypes {
	DATA_FILE("data file"), DUMP_FILE("dump file"), EXTERNAL_DATABASE("external database");
    
	private static Logger logger = LogManager.getLogger(DataSourceTypes.class.getName());
	
    private String value;

    private DataSourceTypes(String value) {
            this.value = value;
    }
    
    public String getValue(){
    	return this.value;
    }
    
    static public boolean isMember(String enumValueToTest) {
    	DataSourceTypes[] enumValues = DataSourceTypes.values();
        for (DataSourceTypes enumValue : enumValues)
            if (enumValue.value.equals(enumValueToTest))
                return true;
        return false;
    }
    
    /**
     * Gets the enum by the text value of it's item.
     * @param text is the value of the enum item.
     * @return the enum item which matches the value with the given text.
     */
    public static DataSourceTypes fromString(String text) {
        if (text != null) {
          for (DataSourceTypes b : DataSourceTypes.values()) {
            if (text.equalsIgnoreCase(b.value)) {
              return b;
            }
          }
        }
        
        logger.error("No constant with text " + text + " found");
        throw new IllegalArgumentException("No constant with text " + text + " found");
      }
}
