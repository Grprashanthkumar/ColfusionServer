/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.bll;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Evgeny
 *
 */
public class RelationshipBLTest extends TestCase {
	
	private static Logger logger = LogManager.getLogger(RelationshipBLTest.class.getName());
	
	private static int SID_FOR_TRIGGER_DATA_MATCHING = 1714;
	private static BigDecimal SIMILARITY_THRESHOLD_FOR_TRIGGER_DATA_MATCHING = new BigDecimal(1);
	
	public RelationshipBLTest(final String name) {
		super(name);
		
	}
	
	//TODO: redo RelationshipBLTest.testTriggerDataMatchingRatiosCalculationsForAllNotCalculated
	@Ignore
	@Test
	public void testTriggerDataMatchingRatiosCalculationsForAllNotCalculated() {
		//TODO: prepare database
		
//		RelationshipBL relBL = new RelationshipBL();
//		
//		try {
//			relBL.triggerDataMatchingRatiosCalculationsForAllNotCalculatedBySid(SID_FOR_TRIGGER_DATA_MATCHING, SIMILARITY_THRESHOLD_FOR_TRIGGER_DATA_MATCHING);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			fail(e.getMessage());
//		}	
	}
}
