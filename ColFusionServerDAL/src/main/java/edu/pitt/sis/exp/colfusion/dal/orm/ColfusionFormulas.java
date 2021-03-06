package edu.pitt.sis.exp.colfusion.dal.orm;

// Generated 2014-6-10 16:17:59 by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionFormulas generated by hbm2java
 */
public class ColfusionFormulas implements java.io.Serializable {

	private Integer id;
	private String type;
	private boolean enabled;
	private String title;
	private String formula;

	public ColfusionFormulas() {
	}

	public ColfusionFormulas(boolean enabled) {
		this.enabled = enabled;
	}

	public ColfusionFormulas(String type, boolean enabled, String title,
			String formula) {
		this.type = type;
		this.enabled = enabled;
		this.title = title;
		this.formula = formula;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFormula() {
		return this.formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

}
