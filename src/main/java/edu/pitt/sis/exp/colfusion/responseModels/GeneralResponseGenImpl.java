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
    
	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen#isSuccessful()
	 */
	@Override
	public boolean isSuccessful() {
		return isSuccessful;
	}
	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen#setSuccessful(boolean)
	 */
	@Override
	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}
	
	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}
	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen#setMessage(java.lang.String)
	 */
	@Override
	public void setMessage(String message) {
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen#getPayload()
	 */
	@Override
	public T getPayload() {
		return payload;
	}
	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen#setPayload(T)
	 */
	@Override
	public void setPayload(T payload) {
		this.payload = payload;
	}	
}
