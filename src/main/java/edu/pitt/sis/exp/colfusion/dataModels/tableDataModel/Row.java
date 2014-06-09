package edu.pitt.sis.exp.colfusion.dataModels.tableDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.exp.colfusion.relationships.transformation.RelationshipTransformation;

public class Row extends ArrayList<ColumnGroup> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Row() {
		super();
	}

	public String getByTransformation(final RelationshipTransformation transformationTable1) {
       
        // TODO:for now just take one column, later need to be able to actually
        // perform transformation.

		Map<String, String> values = getValuesFromCids(transformationTable1.getColumnDbNames());
		               
        return values.get(transformationTable1.getColumnDbNames().get(0));
    }

	
	//TODO: SUper bad, super inefficient. 3 netsted fors...
	private Map<String, String> getValuesFromCids(final List<String> columnDbNames) {
		Map<String, String> result = new HashMap<>();
		
		for (String columnDbName : columnDbNames) {
			for (ColumnGroup columnGroup : this) {
				for (Column column : columnGroup) {
					if (column.getOriginalName().equals(columnDbName)) {
						result.put(columnDbName, (String) column.getCell().getValue());
					}
				}
			}
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(String.format("Row: "));
		
		for (ColumnGroup columnGroup : this) {
			result.append(columnGroup.toString());
			result.append(System.getProperty("line.separator"));
		}
		
		return result.toString();
	}
}
