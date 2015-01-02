package edu.pitt.sis.exp.colfusion.tests.relationships;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

public class ColumnToColumnDataMatchingProcessTest extends TestCase {
	
	private static Logger logger = LogManager.getLogger(ColumnToColumnDataMatchingProcessTest.class.getName());
	
	private static int RELATIONSHIP_ID = 562;
	private static String TRANSFORMATION_FROM = "cid(2372)";
	private static String TRANSFORMATION_TO = "cid(2369)";
	private static BigDecimal SIMILARITY_THRESHOLD_FOR_TRIGGER_DATA_MATCHING = new BigDecimal(1);
	private static int PROCESS_ID = 83;
	
	public ColumnToColumnDataMatchingProcessTest(final String name) {
		super(name);		
	}

	//TODO: redo ColumnToColumnDataMatchingProcessTest.testExecute
	@Ignore
	@Test
	public void testExecute() {
//		ColumnToColumnDataMatchingProcess pr = new ColumnToColumnDataMatchingProcess(RELATIONSHIP_ID, TRANSFORMATION_FROM, TRANSFORMATION_TO, SIMILARITY_THRESHOLD_FOR_TRIGGER_DATA_MATCHING);
//		pr.setID(PROCESS_ID);
//		
//		try {
//			pr.execute();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			fail();
//		}		
	}
	
}
