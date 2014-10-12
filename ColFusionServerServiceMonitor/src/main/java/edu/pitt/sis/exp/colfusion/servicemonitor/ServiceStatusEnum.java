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

    private ServiceStatusEnum(String value){
            this.value = value;
    }
    
    public String getValue(){
    	return this.value;
    }
}
