package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 28, 2014 2:44:19 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * ColfusionDnameinfo generated by hbm2java
 */
public class ColfusionDnameinfo implements java.io.Serializable {

	private Integer cid;
	private ColfusionSourceinfo colfusionSourceinfo;
	private String dnameChosen;
	private String dnameValueType;
	private String dnameValueUnit;
	private String dnameValueFormat;
	private String dnameValueDescription;
	private String dnameOriginalName;
	private boolean isConstant;
	private String constantValue;
	private String missingValue;
	private Set colfusionDnameinfoMetadataEditHistories = new HashSet(0);
	private ColfusionColumnTableInfo colfusionColumnTableInfo;

	public ColfusionDnameinfo() {
	}

	public ColfusionDnameinfo(ColfusionSourceinfo colfusionSourceinfo,
			String dnameChosen, String dnameOriginalName, boolean isConstant) {
		this.colfusionSourceinfo = colfusionSourceinfo;
		this.dnameChosen = dnameChosen;
		this.dnameOriginalName = dnameOriginalName;
		this.isConstant = isConstant;
	}

	public ColfusionDnameinfo(ColfusionSourceinfo colfusionSourceinfo,
			String dnameChosen, String dnameValueType, String dnameValueUnit,
			String dnameValueFormat, String dnameValueDescription,
			String dnameOriginalName, boolean isConstant, String constantValue,
			String missingValue, Set colfusionDnameinfoMetadataEditHistories,
			ColfusionColumnTableInfo colfusionColumnTableInfo) {
		this.colfusionSourceinfo = colfusionSourceinfo;
		this.dnameChosen = dnameChosen;
		this.dnameValueType = dnameValueType;
		this.dnameValueUnit = dnameValueUnit;
		this.dnameValueFormat = dnameValueFormat;
		this.dnameValueDescription = dnameValueDescription;
		this.dnameOriginalName = dnameOriginalName;
		this.isConstant = isConstant;
		this.constantValue = constantValue;
		this.missingValue = missingValue;
		this.colfusionDnameinfoMetadataEditHistories = colfusionDnameinfoMetadataEditHistories;
		this.colfusionColumnTableInfo = colfusionColumnTableInfo;
	}

	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public ColfusionSourceinfo getColfusionSourceinfo() {
		return this.colfusionSourceinfo;
	}

	public void setColfusionSourceinfo(ColfusionSourceinfo colfusionSourceinfo) {
		this.colfusionSourceinfo = colfusionSourceinfo;
	}

	public String getDnameChosen() {
		return this.dnameChosen;
	}

	public void setDnameChosen(String dnameChosen) {
		this.dnameChosen = dnameChosen;
	}

	public String getDnameValueType() {
		return this.dnameValueType;
	}

	public void setDnameValueType(String dnameValueType) {
		this.dnameValueType = dnameValueType;
	}

	public String getDnameValueUnit() {
		return this.dnameValueUnit;
	}

	public void setDnameValueUnit(String dnameValueUnit) {
		this.dnameValueUnit = dnameValueUnit;
	}

	public String getDnameValueFormat() {
		return this.dnameValueFormat;
	}

	public void setDnameValueFormat(String dnameValueFormat) {
		this.dnameValueFormat = dnameValueFormat;
	}

	public String getDnameValueDescription() {
		return this.dnameValueDescription;
	}

	public void setDnameValueDescription(String dnameValueDescription) {
		this.dnameValueDescription = dnameValueDescription;
	}

	public String getDnameOriginalName() {
		return this.dnameOriginalName;
	}

	public void setDnameOriginalName(String dnameOriginalName) {
		this.dnameOriginalName = dnameOriginalName;
	}

	public boolean isIsConstant() {
		return this.isConstant;
	}

	public void setIsConstant(boolean isConstant) {
		this.isConstant = isConstant;
	}

	public String getConstantValue() {
		return this.constantValue;
	}

	public void setConstantValue(String constantValue) {
		this.constantValue = constantValue;
	}

	public String getMissingValue() {
		return this.missingValue;
	}

	public void setMissingValue(String missingValue) {
		this.missingValue = missingValue;
	}

	public Set getColfusionDnameinfoMetadataEditHistories() {
		return this.colfusionDnameinfoMetadataEditHistories;
	}

	public void setColfusionDnameinfoMetadataEditHistories(
			Set colfusionDnameinfoMetadataEditHistories) {
		this.colfusionDnameinfoMetadataEditHistories = colfusionDnameinfoMetadataEditHistories;
	}

	public ColfusionColumnTableInfo getColfusionColumnTableInfo() {
		return this.colfusionColumnTableInfo;
	}

	public void setColfusionColumnTableInfo(
			ColfusionColumnTableInfo colfusionColumnTableInfo) {
		this.colfusionColumnTableInfo = colfusionColumnTableInfo;
	}

}
