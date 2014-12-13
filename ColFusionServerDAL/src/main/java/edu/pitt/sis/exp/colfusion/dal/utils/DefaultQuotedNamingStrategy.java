package edu.pitt.sis.exp.colfusion.dal.utils;

import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;

public class DefaultQuotedNamingStrategy extends ImprovedNamingStrategy {

	public static final NamingStrategy INSTANCE = new DefaultQuotedNamingStrategy();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String classToTableName(final String className) {
		return addQuotes(super.classToTableName(className));
	}
	
	@Override
	public String tableName(final String tableName) {
		return addQuotes(super.tableName(tableName));
	}
	
	@Override
	public String columnName(final String columnName) {
		return addQuotes(super.tableName(columnName));
	}
	
	/**
    * Adds backticks before and after the name.
    * 
    * @param input
    *          the input to quote
    * @return the quoted input
    */
    private static String addQuotes(final String input) {
    	return new StringBuffer().append('`')
                         .append(input)
                         .append('`').toString();
    }
}
