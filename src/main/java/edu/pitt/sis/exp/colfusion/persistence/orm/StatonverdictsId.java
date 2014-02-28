package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 2:44:19 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * StatonverdictsId generated by hbm2java
 */
public class StatonverdictsId implements java.io.Serializable {

	private int relId;
	private long numberOfVerdicts;
	private BigDecimal numberOfApproved;
	private BigDecimal numberOfReject;
	private BigDecimal numberOfNotSure;
	private BigDecimal avgConfidence;

	public StatonverdictsId() {
	}

	public StatonverdictsId(int relId, long numberOfVerdicts) {
		this.relId = relId;
		this.numberOfVerdicts = numberOfVerdicts;
	}

	public StatonverdictsId(int relId, long numberOfVerdicts,
			BigDecimal numberOfApproved, BigDecimal numberOfReject,
			BigDecimal numberOfNotSure, BigDecimal avgConfidence) {
		this.relId = relId;
		this.numberOfVerdicts = numberOfVerdicts;
		this.numberOfApproved = numberOfApproved;
		this.numberOfReject = numberOfReject;
		this.numberOfNotSure = numberOfNotSure;
		this.avgConfidence = avgConfidence;
	}

	public int getRelId() {
		return this.relId;
	}

	public void setRelId(int relId) {
		this.relId = relId;
	}

	public long getNumberOfVerdicts() {
		return this.numberOfVerdicts;
	}

	public void setNumberOfVerdicts(long numberOfVerdicts) {
		this.numberOfVerdicts = numberOfVerdicts;
	}

	public BigDecimal getNumberOfApproved() {
		return this.numberOfApproved;
	}

	public void setNumberOfApproved(BigDecimal numberOfApproved) {
		this.numberOfApproved = numberOfApproved;
	}

	public BigDecimal getNumberOfReject() {
		return this.numberOfReject;
	}

	public void setNumberOfReject(BigDecimal numberOfReject) {
		this.numberOfReject = numberOfReject;
	}

	public BigDecimal getNumberOfNotSure() {
		return this.numberOfNotSure;
	}

	public void setNumberOfNotSure(BigDecimal numberOfNotSure) {
		this.numberOfNotSure = numberOfNotSure;
	}

	public BigDecimal getAvgConfidence() {
		return this.avgConfidence;
	}

	public void setAvgConfidence(BigDecimal avgConfidence) {
		this.avgConfidence = avgConfidence;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof StatonverdictsId))
			return false;
		StatonverdictsId castOther = (StatonverdictsId) other;

		return (this.getRelId() == castOther.getRelId())
				&& (this.getNumberOfVerdicts() == castOther
						.getNumberOfVerdicts())
				&& ((this.getNumberOfApproved() == castOther
						.getNumberOfApproved()) || (this.getNumberOfApproved() != null
						&& castOther.getNumberOfApproved() != null && this
						.getNumberOfApproved().equals(
								castOther.getNumberOfApproved())))
				&& ((this.getNumberOfReject() == castOther.getNumberOfReject()) || (this
						.getNumberOfReject() != null
						&& castOther.getNumberOfReject() != null && this
						.getNumberOfReject().equals(
								castOther.getNumberOfReject())))
				&& ((this.getNumberOfNotSure() == castOther
						.getNumberOfNotSure()) || (this.getNumberOfNotSure() != null
						&& castOther.getNumberOfNotSure() != null && this
						.getNumberOfNotSure().equals(
								castOther.getNumberOfNotSure())))
				&& ((this.getAvgConfidence() == castOther.getAvgConfidence()) || (this
						.getAvgConfidence() != null
						&& castOther.getAvgConfidence() != null && this
						.getAvgConfidence()
						.equals(castOther.getAvgConfidence())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getRelId();
		result = 37 * result + (int) this.getNumberOfVerdicts();
		result = 37
				* result
				+ (getNumberOfApproved() == null ? 0 : this
						.getNumberOfApproved().hashCode());
		result = 37
				* result
				+ (getNumberOfReject() == null ? 0 : this.getNumberOfReject()
						.hashCode());
		result = 37
				* result
				+ (getNumberOfNotSure() == null ? 0 : this.getNumberOfNotSure()
						.hashCode());
		result = 37
				* result
				+ (getAvgConfidence() == null ? 0 : this.getAvgConfidence()
						.hashCode());
		return result;
	}

}
