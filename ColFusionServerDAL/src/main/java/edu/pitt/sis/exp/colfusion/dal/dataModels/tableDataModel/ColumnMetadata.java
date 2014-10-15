package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import com.google.gson.annotations.Expose;

public class ColumnMetadata {
	@Expose private int cid;
	@Expose private String originalName;
	@Expose private String chosenName;
	@Expose private String description;
	@Expose private String variableMeasuringUnit;
	@Expose private String variableValueType;
	@Expose private String variableValueFormat;
	@Expose private String missingValue;
	
	public ColumnMetadata() {
		
	}
	
	public ColumnMetadata(final int cid, final String originalName, final String chosenName, final String description, final String variableMeasuringUnit,
			final String variableValueType, final String variableValueFormat, final String missingValue) {
		this.setCid(cid);
		this.setOriginalName(originalName);
		this.setChosenName(chosenName);
		this.setDescription(description);
		this.setVariableMeasuringUnit(variableMeasuringUnit);
		this.setVariableValueType(variableValueType);
		this.setVariableValueFormat(variableValueFormat);
		this.setMissingValue(missingValue);
	}

	/**
	 * @return the cid
	 */
	public int getCid() {
		return cid;
	}

	/**
	 * @param cid the cid to set
	 */
	public void setCid(final int cid) {
		this.cid = cid;
	}

	/**
	 * @return the originalName
	 */
	public String getOriginalName() {
		return originalName;
	}

	/**
	 * @param originalName the originalName to set
	 */
	public void setOriginalName(final String originalName) {
		this.originalName = originalName;
	}

	/**
	 * @return the chosenName
	 */
	public String getChosenName() {
		return chosenName;
	}

	/**
	 * @param chosenName the chosenName to set
	 */
	public void setChosenName(final String chosenName) {
		this.chosenName = chosenName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the variableMeasuringUnit
	 */
	public String getVariableMeasuringUnit() {
		return variableMeasuringUnit;
	}

	/**
	 * @param variableMeasuringUnit the variableMeasuringUnit to set
	 */
	public void setVariableMeasuringUnit(final String variableMeasuringUnit) {
		this.variableMeasuringUnit = variableMeasuringUnit;
	}

	/**
	 * @return the variableValueType
	 */
	public String getVariableValueType() {
		return variableValueType;
	}

	/**
	 * @param variableValueType the variableValueType to set
	 */
	public void setVariableValueType(final String variableValueType) {
		this.variableValueType = variableValueType;
	}

	/**
	 * @return the variableValueFormat
	 */
	public String getVariableValueFormat() {
		return variableValueFormat;
	}

	/**
	 * @param variableValueFormat the variableValueFormat to set
	 */
	public void setVariableValueFormat(final String variableValueFormat) {
		this.variableValueFormat = variableValueFormat;
	}

	/**
	 * @return the missingValue
	 */
	public String getMissingValue() {
		return missingValue;
	}

	/**
	 * @param missingValue the missingValue to set
	 */
	public void setMissingValue(final String missingValue) {
		this.missingValue = missingValue;
	}
}
