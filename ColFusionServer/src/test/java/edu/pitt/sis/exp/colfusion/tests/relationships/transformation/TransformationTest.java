/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.relationships.transformation;

import java.util.List;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.RelationshipBL;
import edu.pitt.sis.exp.colfusion.relationships.transformation.TransofmationCidsExtractor;

/**
 * @author Evgeny
 *
 */
public class TransformationTest extends TestCase {
	
	private static Logger logger = LogManager.getLogger(TransformationTest.class.getName());
	
	public TransformationTest(final String name) {
		super(name);
		
	}
	
	@Test
	public void testExtractCidsFromTransformation(){
		String transformationString = "cid(123)";
		
		RelationshipBL relBL = new RelationshipBL();
		
		try {
			List<Integer> result = TransofmationCidsExtractor.extractCids(transformationString);
			
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
			List<Integer> result = TransofmationCidsExtractor.extractCids(transformationString);
			
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
			List<Integer> result = TransofmationCidsExtractor.extractCids(transformationString);
			
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
			List<Integer> result = TransofmationCidsExtractor.extractCids(transformationString);
			
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
