/**
 * 
 */
package edu.pitt.sis.exp.colfusion.similarityJoins;

import java.util.List;
import java.util.Map;

import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransformation;

/**
 * @author Evgeny
 *
 */
public class ColfusionResultSet {
	//TODO: this should be a class that describes each row. Each row should consist of sections of cells, each section belongs to one table
	private final List<Map<String, String>> resultSet;
	
	public ColfusionResultSet(final List<Map<String, String>> resultSet) {
		this.resultSet = resultSet;
	}
	
	
	public String getStringFromTransformaion(final RelationshipTransformation transformation) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public int size() {
		return resultSet.size();
	}
	
	public List<Map<String, String>> getData() {
		return resultSet;
	}


	public static String getByTransformation(
			final Map<String, String> rowTable, final RelationshipTransformation transformationTable1) {
		//TODO:for now just take one column, later need to be able to actually perform transformation.
		
		String columnName = transformationTable1.getColumnDbNames().get(0);
		
		return rowTable.get(columnName);
	}
}
