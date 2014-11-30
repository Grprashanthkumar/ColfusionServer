/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

/**
 * @author Weichuan Hong
 *
 * @see DatasetVariableViewModel DatasetVariableViewModel they might be describing the same thing.
 */
//TODO: How is this different from BasicTableInfoViewModel?
@XmlRootElement
public class DnameViewModel {
	@Expose private Integer cid;
	@Expose private Integer sid;
	@Expose private String dname_chosen;
	@Expose private String dname_value_type;
	@Expose private String dname_value_unit;
	@Expose private String dname_value_format;
	@Expose private String dname_value_description;
	@Expose private String dname_original_name;
	@Expose private boolean isConstant;
	@Expose private String constant_value;
	@Expose private String missing_value;
	
	public Integer getSid() {
		return sid;
	}

	public void setSid(final Integer sid) {
		this.sid = sid;
	}

	public String getDname_value_format() {
		return dname_value_format;
	}

	public void setDname_value_format(final String dname_value_format) {
		this.dname_value_format = dname_value_format;
	}

	public boolean getIsConstant() {
		return isConstant;
	}

	public void setIsConstant(final boolean isConstant) {
		this.isConstant = isConstant;
	}

	public String getConstant_value() {
		return constant_value;
	}

	public void setConstant_value(final String constant_value) {
		this.constant_value = constant_value;
	}

	public String getMissing_value() {
		return missing_value;
	}

	public void setMissing_value(final String missing_value) {
		this.missing_value = missing_value;
	}

	public DnameViewModel() {
		
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(final Integer cid) {
		this.cid = cid;
	}

	public String getDname_chosen() {
		return dname_chosen;
	}

	public void setDname_chosen(final String dname_chosen) {
		this.dname_chosen = dname_chosen;
	}

	public String getDname_value_type() {
		return dname_value_type;
	}

	public void setDname_value_type(final String dname_value_type) {
		this.dname_value_type = dname_value_type;
	}

	public String getDname_value_unit() {
		return dname_value_unit;
	}

	public void setDname_value_unit(final String dname_value_unit) {
		this.dname_value_unit = dname_value_unit;
	}

	public String getDname_value_description() {
		return dname_value_description;
	}

	public void setDname_value_description(final String dname_value_description) {
		this.dname_value_description = dname_value_description;
	}

	public String getDname_original_name() {
		return dname_original_name;
	}

	public void setDname_original_name(final String dname_original_name) {
		this.dname_original_name = dname_original_name;
	}

}
