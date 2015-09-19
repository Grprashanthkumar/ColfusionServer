/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.similarityMeasures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public abstract class FilterBase {
	
	static Logger logger = LogManager.getLogger(FilterBase.class.getName());
	
	protected FilterBase() {
		
	}
	
	public abstract double calculate(final String value1, final String value2);
}
