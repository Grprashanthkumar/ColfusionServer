/**
 * 
 */
package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.viewmodels.OneUploadedItemViewModel;

/**
 * Represents the model which returned by the storeUploadedFiles method.
 * 
 * @author Evgeny
 *
 */
@XmlRootElement
public class AcceptedFilesResponse extends GeneralResponseImpl {

	/**
	 * The files which were uploaded and stored
	 */
	protected ArrayList<OneUploadedItemViewModel> payload;
	
	public AcceptedFilesResponse() {
		setPayload(new ArrayList<OneUploadedItemViewModel>());
	}
	
	public AcceptedFilesResponse(ArrayList<OneUploadedItemViewModel> payload) {
		setPayload(payload);
	}

	/**
	 * @return the payload
	 */
	public ArrayList<OneUploadedItemViewModel> getPayload() {
		return this.payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(ArrayList<OneUploadedItemViewModel> payload) {
		this.payload = payload;
	}

}
