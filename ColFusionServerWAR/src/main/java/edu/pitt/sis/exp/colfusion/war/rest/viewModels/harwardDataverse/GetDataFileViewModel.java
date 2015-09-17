package edu.pitt.sis.exp.colfusion.war.rest.viewModels.harwardDataverse;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The model is used for getting a file from dataverse network.
 */
@XmlRootElement
public class GetDataFileViewModel {

	/**
	 * Id of the story for which this file needs to be uploaded.
	 */
	private String sid;

	/**
	 * Dataverse file id.
	 */
	private String fileId;

	/**
	 * File name that should be used to call the downloaded file.
	 */
	private String fileName;

	public GetDataFileViewModel() {

	}

	public GetDataFileViewModel(final String sid, final String fileId, final String fileName) {
		setSid(sid);
		setFileId(fileId);
		setFileName(fileName);
	}

	/**
	 * Id of the story for which this file needs to be uploaded.
	 *
	 * @return
	 */
	public String getSid() {
		return this.sid;
	}

	/**
	 * Id of the story for which this file needs to be uploaded.
	 *
	 * @param sid
	 */
	public void setSid(final String sid) {
		this.sid = sid;
	}

	/**
	 * Dataverse file id.
	 *
	 * @return
	 */
	public String getFileId() {
		return this.fileId;
	}

	/**
	 * Dataverse file id.
	 *
	 * @param fileId
	 */
	public void setFileId(final String fileId) {
		this.fileId = fileId;
	}

	/**
	 * File name that should be used to call the downloaded file.
	 *
	 * @return
	 */
	public String getFileName() {
		return this.fileName;
	}

	/**
	 * File name that should be used to call the downloaded file.
	 *
	 * @param fileName
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}
}
