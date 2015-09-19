package edu.pitt.sis.exp.colfusion.dataverse;

public class DataverseFileInfo {

	private String fileId;
	private String fileName;
	private int size;
	private String citation;
	private String publishedAt;

	public DataverseFileInfo(final String fileId, final String fileName, final int size, final String citation,
			final String publishedAt) {
		setFileId(fileId);
		setFileName(fileName);
		setSize(size);
		setCitation(citation);
		setPublishedAt(publishedAt);
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
