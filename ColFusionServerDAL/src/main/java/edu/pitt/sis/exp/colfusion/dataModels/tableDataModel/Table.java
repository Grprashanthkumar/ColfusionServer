package edu.pitt.sis.exp.colfusion.dataModels.tableDataModel;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class Table extends ArrayList<Row> implements Gsonazable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public Table() {
		super();
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
		
		result.append(String.format("Table: "));
		
		for (Row row : this) {
			result.append(row.toString());
			result.append(System.getProperty("line.separator"));
		}
		
		return result.toString();
	}
}
