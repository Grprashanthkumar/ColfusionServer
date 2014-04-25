package edu.pitt.sis.exp.colfusion.responseModels;

public interface GeneralResponseGen<T> extends GeneralResponse {

	/**
	 * @return the payload
	 */
	public abstract T getPayload();

	/**
	 * @param payload the payload to set
	 */
	public abstract void setPayload(T payload);

}