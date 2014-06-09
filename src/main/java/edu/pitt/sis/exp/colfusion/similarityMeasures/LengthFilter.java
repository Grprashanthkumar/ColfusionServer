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
public class LengthFilter extends FilterBase {

	static Logger logger = LogManager.getLogger(LengthFilter.class.getName());
	
	protected LengthFilter() {
	
	}

	@Override
	public double calculate(final String value1, final String value2) {
		throw new UnsupportedOperationException("isToBeFiltered is not implemented yet.");
	}

	
}
