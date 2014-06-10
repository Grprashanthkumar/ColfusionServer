package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated 2014-6-10 16:17:59 by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;

/**
 * ColfusionRelationshipsColumnsDataMathingRatios generated by hbm2java
 */
public class ColfusionRelationshipsColumnsDataMathingRatios implements
		java.io.Serializable {

	private ColfusionRelationshipsColumnsDataMathingRatiosId id;
	private ColfusionProcesses colfusionProcesses;
	private BigDecimal dataMatchingFromRatio;
	private BigDecimal dataMatchingToRatio;

	public ColfusionRelationshipsColumnsDataMathingRatios() {
	}

	public ColfusionRelationshipsColumnsDataMathingRatios(
			ColfusionRelationshipsColumnsDataMathingRatiosId id,
			ColfusionProcesses colfusionProcesses) {
		this.id = id;
		this.colfusionProcesses = colfusionProcesses;
	}

	public ColfusionRelationshipsColumnsDataMathingRatios(
			ColfusionRelationshipsColumnsDataMathingRatiosId id,
			ColfusionProcesses colfusionProcesses,
			BigDecimal dataMatchingFromRatio, BigDecimal dataMatchingToRatio) {
		this.id = id;
		this.colfusionProcesses = colfusionProcesses;
		this.dataMatchingFromRatio = dataMatchingFromRatio;
		this.dataMatchingToRatio = dataMatchingToRatio;
	}

	public ColfusionRelationshipsColumnsDataMathingRatiosId getId() {
		return this.id;
	}

	public void setId(ColfusionRelationshipsColumnsDataMathingRatiosId id) {
		this.id = id;
	}

	public ColfusionProcesses getColfusionProcesses() {
		return this.colfusionProcesses;
	}

	public void setColfusionProcesses(ColfusionProcesses colfusionProcesses) {
		this.colfusionProcesses = colfusionProcesses;
	}

	public BigDecimal getDataMatchingFromRatio() {
		return this.dataMatchingFromRatio;
	}

	public void setDataMatchingFromRatio(BigDecimal dataMatchingFromRatio) {
		this.dataMatchingFromRatio = dataMatchingFromRatio;
	}

	public BigDecimal getDataMatchingToRatio() {
		return this.dataMatchingToRatio;
	}

	public void setDataMatchingToRatio(BigDecimal dataMatchingToRatio) {
		this.dataMatchingToRatio = dataMatchingToRatio;
	}

}
