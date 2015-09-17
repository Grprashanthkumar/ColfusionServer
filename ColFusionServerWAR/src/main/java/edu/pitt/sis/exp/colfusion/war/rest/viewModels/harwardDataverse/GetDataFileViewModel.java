package edu.pitt.sis.exp.colfusion.war.rest.viewModels.harwardDataverse;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class GetDataFileViewModel {
	private String sid;

	private String fileId;

	private String fileName;

	public GetDataFileViewModel() {

	}

	public GetDataFileViewModel(final String sid, final String fileId, final String fileName) {
		setSid(sid);
		setFileId(fileId);
		setFileName(fileName);
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(final String sid) {
		this.sid = sid;
	}

	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(final String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}
}
