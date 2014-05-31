/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.bll.dataSubmissionWizard;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.RelationshipBL;

/**
 * @author Evgeny
 *
 */
public class RelationshipBLTest extends TestCase {
	
	private static Logger logger = LogManager.getLogger(RelationshipBLTest.class.getName());
	
	private static int SID_FOR_TRIGGER_DATA_MATCHING = 1711;
	private static BigDecimal SIMILARITY_THRESHOLD_FOR_TRIGGER_DATA_MATCHING = new BigDecimal(1);
	
	public RelationshipBLTest(final String name) {
		super(name);
		
	}
	
	@Test
	public void testTriggerDataMatchingRatiosCalculationsForAllNotCalculated() {
		//TODO: prepare database
		
		RelationshipBL relBL = new RelationshipBL();
		
		try {
			relBL.triggerDataMatchingRatiosCalculationsForAllNotCalculatedBySid(SID_FOR_TRIGGER_DATA_MATCHING, SIMILARITY_THRESHOLD_FOR_TRIGGER_DATA_MATCHING);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testExtractCidsFromTransformation(){
		String transformationString = "cid(123)";
		
		RelationshipBL relBL = new RelationshipBL();
		
		try {
			List<Integer> result = relBL.extractCidsFromTransformation(transformationString);
			
			assertEquals(1, result.size());
			assertEquals(123, (int)result.get(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		transformationString = "cid(123) + 5";
		
		relBL = new RelationshipBL();
		
		try {
			List<Integer> result = relBL.extractCidsFromTransformation(transformationString);
			
			assertEquals(1, result.size());
			assertEquals(123, (int)result.get(0));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		transformationString = "cid(123) + 5, cid(321)";
		
		relBL = new RelationshipBL();
		
		try {
			List<Integer> result = relBL.extractCidsFromTransformation(transformationString);
			
			assertEquals(2, result.size());
			assertEquals(123, (int)result.get(0));
			assertEquals(321, (int)result.get(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		transformationString = "Date(cid(123)) + 5, cid(321)";
		
		relBL = new RelationshipBL();
		
		try {
			List<Integer> result = relBL.extractCidsFromTransformation(transformationString);
			
			assertEquals(2, result.size());
			assertEquals(123, (int)result.get(0));
			assertEquals(321, (int)result.get(1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
}
