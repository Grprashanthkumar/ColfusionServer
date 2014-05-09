package edu.pitt.sis.exp.colfusion.responseModels;

import java.io.Serializable;

public interface GeneralResponseGen<T extends Serializable> extends GeneralResponse {

	/**
	 * @return the payload
	 */
	public abstract T getPayload();

	/**
	 * @param payload the payload to set
	 */
	public abstract void setPayload(T payload);

}