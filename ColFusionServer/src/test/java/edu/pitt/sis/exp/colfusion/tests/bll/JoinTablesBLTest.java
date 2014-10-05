package edu.pitt.sis.exp.colfusion.tests.bll;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.JoinTablesBL;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;

public class JoinTablesBLTest extends TestCase {
	private static Logger logger = LogManager.getLogger(JoinTablesBLTest.class.getName());
	
	private static Integer[] REL_IDS = new Integer[] {565, 566};
	
	@Test
	public void testJoinTablesByRelationships() {
		//TODO: prepare database
		
		JoinTablesBL joinTableBL = new JoinTablesBL();
		
		JoinTablesByRelationshipsViewModel joinTablesViewModel = new JoinTablesByRelationshipsViewModel();
		joinTablesViewModel.setRelationshipIds(Arrays.asList(REL_IDS));
		joinTablesViewModel.setSimilarityThreshold(1.0);
		
		try {
			JointTableByRelationshipsResponeModel result = joinTableBL.joinTablesByRelationships(joinTablesViewModel);
			
			assertEquals(true, result.isSuccessful);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			fail(e.getMessage());
		}	
	}
}
