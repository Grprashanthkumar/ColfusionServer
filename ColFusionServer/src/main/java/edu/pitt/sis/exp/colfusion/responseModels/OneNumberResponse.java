/**
 * 
 */
package edu.pitt.sis.exp.colfusion.responseModels;

import java.io.Serializable;

/**
 * @author Evgeny
 *
 */
public class OneNumberResponse extends GeneralResponseImpl {
	private Serializable payload;

	/**
	 * @return the payload
	 */
	public Serializable getPayload() {
		return payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(Serializable payload) {
		this.payload = payload;
	}
}
