package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class Row implements Gsonazable {
	
//	static {
//		Gsonizer.registerTypeAdapter(Row.class, new RowSerializer());
//	}
	
	@Expose private List<ColumnGroup> columnGroups = new ArrayList<ColumnGroup>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Row() {
//		super();
	}

	public Row(final List<ColumnGroup> columnGroups) {
		this.setColumnGroups(columnGroups);
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
			for (ColumnGroup columnGroup : this.getColumnGroups()) {
				for (Column column : columnGroup.getColumns()) {
					if (column.getOriginalName().equals(columnDbName)) {
						result.put(columnDbName, (String) column.getCell().getValue());
					}
				}
			}
		}
		
		return result;
	}

	@Override
	public String toJson() {
		return Gsonizer.toJson(this, false);
	}

	@Override
	public void fromJson() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		
		result.append(String.format("Row: "));
		
		for (ColumnGroup columnGroup : this.getColumnGroups()) {
			result.append(columnGroup.toString());
			result.append(System.getProperty("line.separator"));
		}
		
		return result.toString();
	}

	/**
	 * @return the columnGroups
	 */
	public List<ColumnGroup> getColumnGroups() {
		return columnGroups;
	}

	/**
	 * @param columnGroups the columnGroups to set
	 */
	public void setColumnGroups(final List<ColumnGroup> columnGroups) {
		this.columnGroups = columnGroups;
	}
}
