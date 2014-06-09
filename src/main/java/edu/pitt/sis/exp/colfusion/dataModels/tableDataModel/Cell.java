package edu.pitt.sis.exp.colfusion.dataModels.tableDataModel;

import java.io.Serializable;

public class Cell {
	
	private Serializable value;
	
	public Cell(final Serializable value) {
		this.value = value;
	}
	
	public Serializable getValue() {
		return value;
	}
	
	public void setValue(final Serializable value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
}
