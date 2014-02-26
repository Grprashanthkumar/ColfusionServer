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
	private String valueUnit;
	private String valueType;
	private String valueFormat;
	
	public DatasetVariableViewModel() {
		
	}
	
	public DatasetVariableViewModel(String originalName, String chosenName, String description, String valueUnit, String valueType, String valueFormat) {
		setChosenName(chosenName);
		setDescription(description);
		setFormat(valueFormat);
		setOriginalName(originalName);
		setUnit(valueUnit);
		setValueType(valueType);
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
	public String getUnit() {
		return valueUnit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String valueUnit) {
		this.valueUnit = valueUnit;
	}

	/**
	 * @return the valueType
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * @param valueType the valueType to set
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return valueFormat;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String valueFormat) {
		this.valueFormat = valueFormat;
	}
}
