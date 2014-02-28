package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 2:44:19 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * ColfusionPentahoLogStepId generated by hbm2java
 */
public class ColfusionPentahoLogStepId implements java.io.Serializable {

	private Integer idBatch;
	private String channelId;
	private Date logDate;
	private String transname;
	private String stepname;
	private Integer stepCopy;
	private Long linesRead;
	private Long linesWritten;
	private Long linesUpdated;
	private Long linesInput;
	private Long linesOutput;
	private Long linesRejected;
	private Long errors;
	private String logField;

	public ColfusionPentahoLogStepId() {
	}

	public ColfusionPentahoLogStepId(Integer idBatch, String channelId,
			Date logDate, String transname, String stepname, Integer stepCopy,
			Long linesRead, Long linesWritten, Long linesUpdated,
			Long linesInput, Long linesOutput, Long linesRejected, Long errors,
			String logField) {
		this.idBatch = idBatch;
		this.channelId = channelId;
		this.logDate = logDate;
		this.transname = transname;
		this.stepname = stepname;
		this.stepCopy = stepCopy;
		this.linesRead = linesRead;
		this.linesWritten = linesWritten;
		this.linesUpdated = linesUpdated;
		this.linesInput = linesInput;
		this.linesOutput = linesOutput;
		this.linesRejected = linesRejected;
		this.errors = errors;
		this.logField = logField;
	}

	public Integer getIdBatch() {
		return this.idBatch;
	}

	public void setIdBatch(Integer idBatch) {
		this.idBatch = idBatch;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Date getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getTransname() {
		return this.transname;
	}

	public void setTransname(String transname) {
		this.transname = transname;
	}

	public String getStepname() {
		return this.stepname;
	}

	public void setStepname(String stepname) {
		this.stepname = stepname;
	}

	public Integer getStepCopy() {
		return this.stepCopy;
	}

	public void setStepCopy(Integer stepCopy) {
		this.stepCopy = stepCopy;
	}

	public Long getLinesRead() {
		return this.linesRead;
	}

	public void setLinesRead(Long linesRead) {
		this.linesRead = linesRead;
	}

	public Long getLinesWritten() {
		return this.linesWritten;
	}

	public void setLinesWritten(Long linesWritten) {
		this.linesWritten = linesWritten;
	}

	public Long getLinesUpdated() {
		return this.linesUpdated;
	}

	public void setLinesUpdated(Long linesUpdated) {
		this.linesUpdated = linesUpdated;
	}

	public Long getLinesInput() {
		return this.linesInput;
	}

	public void setLinesInput(Long linesInput) {
		this.linesInput = linesInput;
	}

	public Long getLinesOutput() {
		return this.linesOutput;
	}

	public void setLinesOutput(Long linesOutput) {
		this.linesOutput = linesOutput;
	}

	public Long getLinesRejected() {
		return this.linesRejected;
	}

	public void setLinesRejected(Long linesRejected) {
		this.linesRejected = linesRejected;
	}

	public Long getErrors() {
		return this.errors;
	}

	public void setErrors(Long errors) {
		this.errors = errors;
	}

	public String getLogField() {
		return this.logField;
	}

	public void setLogField(String logField) {
		this.logField = logField;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ColfusionPentahoLogStepId))
			return false;
		ColfusionPentahoLogStepId castOther = (ColfusionPentahoLogStepId) other;

		return ((this.getIdBatch() == castOther.getIdBatch()) || (this
				.getIdBatch() != null && castOther.getIdBatch() != null && this
				.getIdBatch().equals(castOther.getIdBatch())))
				&& ((this.getChannelId() == castOther.getChannelId()) || (this
						.getChannelId() != null
						&& castOther.getChannelId() != null && this
						.getChannelId().equals(castOther.getChannelId())))
				&& ((this.getLogDate() == castOther.getLogDate()) || (this
						.getLogDate() != null && castOther.getLogDate() != null && this
						.getLogDate().equals(castOther.getLogDate())))
				&& ((this.getTransname() == castOther.getTransname()) || (this
						.getTransname() != null
						&& castOther.getTransname() != null && this
						.getTransname().equals(castOther.getTransname())))
				&& ((this.getStepname() == castOther.getStepname()) || (this
						.getStepname() != null
						&& castOther.getStepname() != null && this
						.getStepname().equals(castOther.getStepname())))
				&& ((this.getStepCopy() == castOther.getStepCopy()) || (this
						.getStepCopy() != null
						&& castOther.getStepCopy() != null && this
						.getStepCopy().equals(castOther.getStepCopy())))
				&& ((this.getLinesRead() == castOther.getLinesRead()) || (this
						.getLinesRead() != null
						&& castOther.getLinesRead() != null && this
						.getLinesRead().equals(castOther.getLinesRead())))
				&& ((this.getLinesWritten() == castOther.getLinesWritten()) || (this
						.getLinesWritten() != null
						&& castOther.getLinesWritten() != null && this
						.getLinesWritten().equals(castOther.getLinesWritten())))
				&& ((this.getLinesUpdated() == castOther.getLinesUpdated()) || (this
						.getLinesUpdated() != null
						&& castOther.getLinesUpdated() != null && this
						.getLinesUpdated().equals(castOther.getLinesUpdated())))
				&& ((this.getLinesInput() == castOther.getLinesInput()) || (this
						.getLinesInput() != null
						&& castOther.getLinesInput() != null && this
						.getLinesInput().equals(castOther.getLinesInput())))
				&& ((this.getLinesOutput() == castOther.getLinesOutput()) || (this
						.getLinesOutput() != null
						&& castOther.getLinesOutput() != null && this
						.getLinesOutput().equals(castOther.getLinesOutput())))
				&& ((this.getLinesRejected() == castOther.getLinesRejected()) || (this
						.getLinesRejected() != null
						&& castOther.getLinesRejected() != null && this
						.getLinesRejected()
						.equals(castOther.getLinesRejected())))
				&& ((this.getErrors() == castOther.getErrors()) || (this
						.getErrors() != null && castOther.getErrors() != null && this
						.getErrors().equals(castOther.getErrors())))
				&& ((this.getLogField() == castOther.getLogField()) || (this
						.getLogField() != null
						&& castOther.getLogField() != null && this
						.getLogField().equals(castOther.getLogField())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getIdBatch() == null ? 0 : this.getIdBatch().hashCode());
		result = 37 * result
				+ (getChannelId() == null ? 0 : this.getChannelId().hashCode());
		result = 37 * result
				+ (getLogDate() == null ? 0 : this.getLogDate().hashCode());
		result = 37 * result
				+ (getTransname() == null ? 0 : this.getTransname().hashCode());
		result = 37 * result
				+ (getStepname() == null ? 0 : this.getStepname().hashCode());
		result = 37 * result
				+ (getStepCopy() == null ? 0 : this.getStepCopy().hashCode());
		result = 37 * result
				+ (getLinesRead() == null ? 0 : this.getLinesRead().hashCode());
		result = 37
				* result
				+ (getLinesWritten() == null ? 0 : this.getLinesWritten()
						.hashCode());
		result = 37
				* result
				+ (getLinesUpdated() == null ? 0 : this.getLinesUpdated()
						.hashCode());
		result = 37
				* result
				+ (getLinesInput() == null ? 0 : this.getLinesInput()
						.hashCode());
		result = 37
				* result
				+ (getLinesOutput() == null ? 0 : this.getLinesOutput()
						.hashCode());
		result = 37
				* result
				+ (getLinesRejected() == null ? 0 : this.getLinesRejected()
						.hashCode());
		result = 37 * result
				+ (getErrors() == null ? 0 : this.getErrors().hashCode());
		result = 37 * result
				+ (getLogField() == null ? 0 : this.getLogField().hashCode());
		return result;
	}

}
