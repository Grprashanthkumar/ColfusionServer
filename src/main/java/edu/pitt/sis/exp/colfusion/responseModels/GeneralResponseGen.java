package edu.pitt.sis.exp.colfusion.responseModels;

public interface GeneralResponseGen<T> {

	/**
	 * @return the isSuccessful
	 */
	public abstract boolean isSuccessful();

	/**
	 * @param isSuccessful the isSuccessful to set
	 */
	public abstract void setSuccessful(boolean isSuccessful);

	/**
	 * @return the message
	 */
	public abstract String getMessage();

	/**
	 * @param message the message to set
	 */
	public abstract void setMessage(String message);

	/**
	 * @return the payload
	 */
	public abstract T getPayload();

	/**
	 * @param payload the payload to set
	 */
	public abstract void setPayload(T payload);

}