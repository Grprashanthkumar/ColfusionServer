package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class Table implements Gsonazable{
	
//	static {
//		Gsonizer.registerTypeAdapter(Table.class, new TableSerializer());
//	}
	
	@Expose private List<Row> rows = new ArrayList<Row>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public Table() {
		
	}
	
	public Table(final List<Row> rows) {
		this.setRows(rows);
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
		
		for (Row row : this.getRows()) {
			result.append(row.toString());
			result.append(System.getProperty("line.separator"));
		}
		
		return result.toString();
	}

	/**
	 * @return the rows
	 */
	public List<Row> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(final List<Row> rows) {
		this.rows = rows;
	}
}
