/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.responseModels;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class GeneralResponseGenImpl<T extends Serializable> implements GeneralResponseGen<T> {
		
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
