package edu.pitt.sis.exp.colfusion.tests.bll;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

public class JoinTablesBLTest extends TestCase {
	private static Logger logger = LogManager.getLogger(JoinTablesBLTest.class.getName());
	
	private static Integer[] REL_IDS = new Integer[] {565, 566};
	
	//TODO: redo with DatabaseTestBase
	@Ignore
	@Test
	public void testJoinTablesByRelationships() {
		//TODO: prepare database
		
//		JoinTablesBL joinTableBL = new JoinTablesBL();
//		
//		JoinTablesByRelationshipsViewModel joinTablesViewModel = new JoinTablesByRelationshipsViewModel();
//		joinTablesViewModel.setRelationshipIds(Arrays.asList(REL_IDS));
//		joinTablesViewModel.setSimilarityThreshold(1.0);
//		
//		try {
//			JointTableByRelationshipsResponeModel result = joinTableBL.joinTablesByRelationships(joinTablesViewModel);
//			
//			assertEquals(true, result.isSuccessful);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			fail(e.getMessage());
//		}	
	}
}
