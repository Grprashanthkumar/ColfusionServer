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
public class NormalizedDistance implements SimilarityDistanceMeasure {

	static Logger logger = LogManager.getLogger(NormalizedDistance.class.getName());
	
	private final SimilarityDistanceMeasure simDisMeasure;
	
	public NormalizedDistance(final SimilarityDistanceMeasure simDisMeasure) {
		this.simDisMeasure = simDisMeasure;
	}

	@Override
	public double computeSimilarity(final String value1, final String value2) {
		return 1 - computeDistance(value1, value2);
	}

	@Override
	public double computeDistance(final String value1, final String value2) {
		return simDisMeasure.computeDistance(value1, value2) / Math.max(value1.length(), value2.length());
	}
}
