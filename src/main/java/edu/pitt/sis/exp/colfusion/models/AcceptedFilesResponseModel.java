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
	public ArrayList<OneUploadedItem> Payload;
	
	public AcceptedFilesResponseModel() {
		Payload = new ArrayList<OneUploadedItem>();
	}

}
