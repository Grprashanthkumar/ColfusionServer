package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated 2014-5-12 21:35:22 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * ColfusionRelationshipsColumnsDatamathingRatios generated by hbm2java
 */
public class ColfusionRelationshipsColumnsDatamathingRatios implements
		java.io.Serializable {

	private ColfusionRelationshipsColumnsDatamathingRatiosId id;
	private ColfusionProcesses colfusionProcesses;
	private BigDecimal dataMatchingFromRatio;
	private BigDecimal dataMatchingToRatio;

	public ColfusionRelationshipsColumnsDatamathingRatios() {
	}

	public ColfusionRelationshipsColumnsDatamathingRatios(
			final ColfusionRelationshipsColumnsDatamathingRatiosId id,
			final ColfusionProcesses colfusionProcesses) {
		this.id = id;
		this.colfusionProcesses = colfusionProcesses;
	}

	public ColfusionRelationshipsColumnsDatamathingRatios(
			final ColfusionRelationshipsColumnsDatamathingRatiosId id,
			final ColfusionProcesses colfusionProcesses,
			final BigDecimal dataMatchingFromRatio, final BigDecimal dataMatchingToRatio) {
		this.id = id;
		this.colfusionProcesses = colfusionProcesses;
		this.dataMatchingFromRatio = dataMatchingFromRatio;
		this.dataMatchingToRatio = dataMatchingToRatio;
	}

	public ColfusionRelationshipsColumnsDatamathingRatiosId getId() {
		return this.id;
	}

	public void setId(final ColfusionRelationshipsColumnsDatamathingRatiosId id) {
		this.id = id;
	}

	public ColfusionProcesses getColfusionProcesses() {
		return this.colfusionProcesses;
	}

	public void setColfusionProcesses(final ColfusionProcesses colfusionProcesses) {
		this.colfusionProcesses = colfusionProcesses;
	}

	public BigDecimal getDataMatchingFromRatio() {
		return this.dataMatchingFromRatio;
	}

	public void setDataMatchingFromRatio(final BigDecimal dataMatchingFromRatio) {
		this.dataMatchingFromRatio = dataMatchingFromRatio;
	}

	public BigDecimal getDataMatchingToRatio() {
		return this.dataMatchingToRatio;
	}

	public void setDataMatchingToRatio(final BigDecimal dataMatchingToRatio) {
		this.dataMatchingToRatio = dataMatchingToRatio;
	}

}
