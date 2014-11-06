/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

/**
 * @author Weichuan Hong
 *
 */

@XmlRootElement
public class BasicTableInfoViewModel {
	@Expose private Integer cid;
	@Expose private String dname_chosen;
	@Expose private String dname_value_type;
	@Expose private String dname_value_unit;
	@Expose private String dname_value_description;
	@Expose private String dname_original_name;
	
	public BasicTableInfoViewModel() {
		
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
