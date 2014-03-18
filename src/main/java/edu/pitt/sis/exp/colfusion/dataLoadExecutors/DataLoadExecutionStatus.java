/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dataLoadExecutors;

/**
 * @author Evgeny
 *
 */
public enum DataLoadExecutionStatus {
	IN_PROGRESS("in progress"), FAILED("error"), SUCCESS("sucess");
    

    private String value;

    private DataLoadExecutionStatus(String value) {
            this.value = value;
    }
    
    public String getValue(){
    	return this.value;
    }
    
    static public boolean isMember(String enumValueToTest) {
    	DataLoadExecutionStatus[] enumValues = DataLoadExecutionStatus.values();
        for (DataLoadExecutionStatus enumValue : enumValues)
            if (enumValue.value.equals(enumValueToTest))
                return true;
        return false;
    }
}
