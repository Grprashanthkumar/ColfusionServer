package edu.pitt.sis.exp.colfusion.servicemonitor;

/**
 * @author Hao Bai
 * 
 * This class is used for enumerating service's status.
 */
public enum ServiceStatusEnum{
	STOPPED("stopped"), 
	STARTED("started"), 
	RUNNING("running");
	
    private String value;

    /**
     * set Service Status Enum
     * 
     * @param value
     */
    private ServiceStatusEnum(String value){
            this.value = value;
    }
    
    /**
     * get value
     * 
     * @return value
     */
    public String getValue(){
    	return this.value;
    }
}
