/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.responseModels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.dal.viewmodels.FileContentInfoViewModel;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class FileContentInfoReponse extends GeneralResponseImpl {
	/**
	 * The files which were uploaded and stored
	 */
	protected ArrayList<FileContentInfoViewModel> payload;
	
	public FileContentInfoReponse() {
		setPayload(new ArrayList<FileContentInfoViewModel>());
	}
	
	public FileContentInfoReponse(final ArrayList<FileContentInfoViewModel> payload) {
		setPayload(payload);
	}

	/**
	 * @return the payload
	 */
	public ArrayList<FileContentInfoViewModel> getPayload() {
		return this.payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(final ArrayList<FileContentInfoViewModel> payload) {
		this.payload = payload;
	}
}
