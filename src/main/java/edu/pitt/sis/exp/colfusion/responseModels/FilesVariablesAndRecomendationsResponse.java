/**
 * 
 */
package edu.pitt.sis.exp.colfusion.responseModels;

import java.util.ArrayList;

import edu.pitt.sis.exp.colfusion.viewmodels.FilesVariablesAndRecomendationsViewModel;

/**
 * @author Evgeny
 *
 */
public class FilesVariablesAndRecomendationsResponse extends GeneralResponse {
	/**
	 * The files which were uploaded and stored
	 */
	protected ArrayList<FilesVariablesAndRecomendationsViewModel> payload;
	
	public FilesVariablesAndRecomendationsResponse() {
		setPayload(new ArrayList<FilesVariablesAndRecomendationsViewModel>());
	}
	
	public FilesVariablesAndRecomendationsResponse(ArrayList<FilesVariablesAndRecomendationsViewModel> payload) {
		setPayload(payload);
	}

	/**
	 * @return the payload
	 */
	public ArrayList<FilesVariablesAndRecomendationsViewModel> getPayload() {
		return this.payload;
	}

	/**
	 * @param payload the payload to set
	 */
	public void setPayload(ArrayList<FilesVariablesAndRecomendationsViewModel> payload) {
		this.payload = payload;
	}
}
