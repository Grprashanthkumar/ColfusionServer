package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 25, 2014 9:55:27 AM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionMiscData generated by hbm2java
 */
public class ColfusionMiscData implements java.io.Serializable {

	private String name;
	private String data;

	public ColfusionMiscData() {
	}

	public ColfusionMiscData(String name) {
		this.name = name;
	}

	public ColfusionMiscData(String name, String data) {
		this.name = name;
		this.data = data;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
