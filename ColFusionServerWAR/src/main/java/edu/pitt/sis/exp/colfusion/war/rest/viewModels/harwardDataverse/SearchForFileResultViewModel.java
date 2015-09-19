package edu.pitt.sis.exp.colfusion.war.rest.viewModels.harwardDataverse;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.bll.dataverse.DataverseFileInfo;
import edu.pitt.sis.exp.colfusion.war.rest.viewModels.ViewModel;

@XmlRootElement
public class SearchForFileResultViewModel implements ViewModel {
	private String fileId;
	private String fileName;
	private int size;
	private String citation;
	private String publishedAt;

	public SearchForFileResultViewModel() {

	}

	public SearchForFileResultViewModel(final DataverseFileInfo dataverseFileInfo) {
		setFileId(dataverseFileInfo.getFileId());
		setFileName(dataverseFileInfo.getFileName());
		setSize(dataverseFileInfo.getSize());
		setCitation(dataverseFileInfo.getCitation());
		setPublishedAt(dataverseFileInfo.getPublishedAt());
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

	public int getSize() {
		return this.size;
	}

	public void setSize(final int size) {
		this.size = size;
	}

	public String getCitation() {
		return this.citation;
	}

	public void setCitation(final String citation) {
		this.citation = citation;
	}

	public String getPublishedAt() {
		return this.publishedAt;
	}

	public void setPublishedAt(final String publishedAt) {
		this.publishedAt = publishedAt;
	}
}
