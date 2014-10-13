package edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class ColumnGroup implements Gsonazable{
	
//	static {
//		Gsonizer.registerTypeAdapter(ColumnGroup.class, new ColumnGroupSerializer());
//	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger logger = LogManager.getLogger(ColumnGroup.class.getName());
	
	@Expose private String tableName;
	//private final int sid; 
	
	@Expose private List<Column> columns = new ArrayList<Column>();
	
	public ColumnGroup() {
		
	}
	
	public ColumnGroup(final String tableName) {
	//	super();
		
		if (tableName.length() == 0) {
			logger.error("ColumnGroup(): Table name cannot be empty.");
			throw new IllegalArgumentException("Table name cannot be empty.");
		}
		
		this.tableName = tableName;
	}
	
	public ColumnGroup(final String tableName, final List<Column> columns) {
		if (tableName.length() == 0) {
			logger.error("ColumnGroup(): Table name cannot be empty.");
			throw new IllegalArgumentException("Table name cannot be empty.");
		}
		
		this.tableName = tableName;
		this.setColumns(columns);
	}
	
	/**
	 * @return the sid
	 */
//	public int getSid() {
//		return sid;
//	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
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
		
		result.append(String.format("Table Name: %s. Column Group: ", tableName));
		
		for (Column column : this.getColumns()) {
			result.append(column.toString());
			result.append(System.getProperty("line.separator"));
		}
		
		return result.toString();
	}

	/**
	 * @return the columns
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(final List<Column> columns) {
		this.columns = columns;
	}
}
