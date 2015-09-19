/**
 *
 */
package edu.pitt.sis.exp.colfusion.bll.relationships.transformation;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.RelationshipBL;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransofmationUtil;
import junit.framework.TestCase;

//TODO: This class might need to be moved to the DAL project or the TransformationUtil should be moved here.
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
			final List<Integer> result = RelationshipTransofmationUtil.extractCids(transformationString);

			assertEquals(1, result.size());
			assertEquals(123, (int)result.get(0));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		transformationString = "cid(123) + 5";

		relBL = new RelationshipBL();

		try {
			final List<Integer> result = RelationshipTransofmationUtil.extractCids(transformationString);

			assertEquals(1, result.size());
			assertEquals(123, (int)result.get(0));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		transformationString = "cid(123) + 5, cid(321)";

		relBL = new RelationshipBL();

		try {
			final List<Integer> result = RelationshipTransofmationUtil.extractCids(transformationString);

			assertEquals(2, result.size());
			assertEquals(123, (int)result.get(0));
			assertEquals(321, (int)result.get(1));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}

		transformationString = "Date(cid(123)) + 5, cid(321)";

		relBL = new RelationshipBL();

		try {
			final List<Integer> result = RelationshipTransofmationUtil.extractCids(transformationString);

			assertEquals(2, result.size());
			assertEquals(123, (int)result.get(0));
			assertEquals(321, (int)result.get(1));
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
}
