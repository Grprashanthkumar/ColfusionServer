/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class DatasetVariableViewModel {
	private String originalName;
	private String chosenName;
	private String description;
	private String variableMeasuringUnit;
	private String variableValueType;
	private String variableValueFormat;
	private boolean checked;
	
	public DatasetVariableViewModel() {
		
	}
	
	public DatasetVariableViewModel(String originalName, String chosenName, String description, String variableMeasuringUnit, String variableValueType, String variableValueFormat,
			boolean checked) {
		setChosenName(chosenName);
		setDescription(description);
		setVariableValueFormat(variableValueFormat);
		setOriginalName(originalName);
		setVariableMeasuringUnit(variableMeasuringUnit);
		setVariableValueType(variableValueType);
		setChecked(checked);
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
	public void setOriginalName(String originalName) {
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
	public void setChosenName(String chosenName) {
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
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the unit
	 */
	public String getVariableMeasuringUnit() {
		return variableMeasuringUnit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setVariableMeasuringUnit(String variableMeasuringUnit) {
		this.variableMeasuringUnit = variableMeasuringUnit;
	}

	/**
	 * @return the valueType
	 */
	public String getVariableValueType() {
		return variableValueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setVariableValueType(String valueType) {
		this.variableValueType = valueType;
	}

	/**
	 * @return the format
	 */
	public String getVariableValueFormat() {
		return variableValueFormat;
	}

	/**
	 * @param format the format to set
	 */
	public void setVariableValueFormat(String valueFormat) {
		this.variableValueFormat = valueFormat;
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
