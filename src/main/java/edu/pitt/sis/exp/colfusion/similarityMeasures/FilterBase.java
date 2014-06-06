/**
 * 
 */
package edu.pitt.sis.exp.colfusion.similarityMeasures;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Evgeny
 *
 */
public abstract class FilterBase {
	
	static Logger logger = LogManager.getLogger(FilterBase.class.getName());
	
	protected final double threshold;
	
	protected FilterBase(final double threshold) {
		this.threshold = threshold;
	}
	
	public abstract boolean isToBeFiltered(String value1, String value2);
}
