/**
 * 
 */
package edu.pitt.sis.exp.colfusion.similarityJoins;

import java.util.List;
import java.util.Map;

import edu.pitt.sis.exp.colfusion.relationships.transformation.RelationshipTransformation;

/**
 * @author Evgeny
 *
 */
public class ColfusionResultSet {
	
	private final List<Map<String, String>> resultSet;
	
	public ColfusionResultSet(final List<Map<String, String>> resultSet) {
		this.resultSet = resultSet;
	}
	
	
	public String getStringFromTransformaion(final RelationshipTransformation transformation) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public int size() {
		return 2; //IMPLEMENT resultSet.size();
	}
}
