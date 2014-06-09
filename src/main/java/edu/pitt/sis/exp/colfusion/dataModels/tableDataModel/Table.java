package edu.pitt.sis.exp.colfusion.dataModels.tableDataModel;

import java.util.ArrayList;

public class Table extends ArrayList<Row> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public Table() {
		super();
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(String.format("Table: "));
		
		for (Row row : this) {
			result.append(row.toString());
			result.append(System.getProperty("line.separator"));
		}
		
		return result.toString();
	}
}
