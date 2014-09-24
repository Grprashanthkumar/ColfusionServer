package edu.pitt.sis.exp.colfusion.dataModels.tableDataModel;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;
import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

@XmlRootElement
public class ColumnGroup extends ArrayList<Column> implements Gsonazable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger logger = LogManager.getLogger(ColumnGroup.class.getName());
	
	@Expose private final String tableName;
	//private final int sid; 
	
	public ColumnGroup(final String tableName) {
		super();
		
	//	this.sid = sid;
		
		if (tableName.length() == 0) {
			logger.error("ColumnGroup(): Table name cannot be empty.");
			throw new IllegalArgumentException("Table name cannot be empty.");
		}
		
		this.tableName = tableName;
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
		
		for (Column column : this) {
			result.append(column.toString());
			result.append(System.getProperty("line.separator"));
		}
		
		return result.toString();
	}
}
