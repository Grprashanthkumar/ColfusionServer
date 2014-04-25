/**
 * 
 */
package edu.pitt.sis.exp.colfusion.responseModels;


/**
 * @author Evgeny
 *
 */
public class GeneralResponseGenImpl<T> implements GeneralResponseGen<T> {
		
	private boolean isSuccessful;
    private String message;
    private T payload;
    
	@Override
	public boolean isSuccessful() {
		return isSuccessful;
	}
	
	@Override
	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	@Override
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public T getPayload() {
		return payload;
	}
	
	@Override
	public void setPayload(T payload) {
		this.payload = payload;
	}	
}
