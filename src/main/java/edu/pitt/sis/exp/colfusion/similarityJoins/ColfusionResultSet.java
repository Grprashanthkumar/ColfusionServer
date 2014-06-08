/**
 * 
 */
package edu.pitt.sis.exp.colfusion.similarityJoins;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Evgeny
 *
 */
public class ColfusionResultSet {
	
	private final ResultSet resultSet;
	
	public ColfusionResultSet(final ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
	public boolean next() throws SQLException {
		return resultSet.next();
	}
	
	
	public String getStringFromTransformaion(final String transformation) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}
}
