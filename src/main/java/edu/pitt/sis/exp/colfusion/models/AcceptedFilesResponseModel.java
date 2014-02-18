/**
 * 
 */
package edu.pitt.sis.exp.colfusion.models;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the model which returned by the storeUploadedFiles method.
 * 
 * @author Evgeny
 *
 */
@XmlRootElement
public class AcceptedFilesResponseModel extends GeneralResponseModel {

	/**
	 * The files which were uploaded and stored
	 */
	private ArrayList<OneUploadedItem> payload;
	
	public AcceptedFilesResponseModel() {
		setPayload(new ArrayList<OneUploadedItem>());
	}
	
	public AcceptedFilesResponseModel(ArrayList<OneUploadedItem> payload) {
		setPayload(payload);
	}

	/**
	 * @return the payload
	 */
	public ArrayList<OneUploadedItem> getPayload() {
		return this.payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(ArrayList<OneUploadedItem> payload) {
		this.payload = payload;
	}

}
