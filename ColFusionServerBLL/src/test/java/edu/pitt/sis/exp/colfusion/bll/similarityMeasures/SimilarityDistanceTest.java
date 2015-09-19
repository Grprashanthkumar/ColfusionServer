/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll.similarityMeasures;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.similarityMeasures.LevenshteinDistance;
import edu.pitt.sis.exp.colfusion.bll.similarityMeasures.NormalizedDistance;
import edu.pitt.sis.exp.colfusion.bll.similarityMeasures.SimilarityDistanceMeasure;

/**
 * @author Evgeny
 *
 */
public class SimilarityDistanceTest extends TestCase {
	static Logger logger = LogManager.getLogger(SimilarityDistanceTest.class.getName());
	
	@Test
	public void testComputeSimilarity() {
		SimilarityDistanceMeasure simMeasure = new LevenshteinDistance();
		
		double distance = simMeasure.computeDistance("BMW", "IBM");
		
		assertEquals(2, distance, 0.1);
		
		NormalizedDistance normSimMeasure = new NormalizedDistance(simMeasure);
		
		distance = normSimMeasure.computeDistance("BMW", "IBM");
		
		assertEquals(0.66666, distance, 0.00006);
	}
}
