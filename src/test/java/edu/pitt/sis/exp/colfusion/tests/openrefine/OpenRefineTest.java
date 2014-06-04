/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.openrefine;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.OpenRefineBL;
import junit.framework.TestCase;

/**
 * @author Evgeny
 *
 */
public class OpenRefineTest extends TestCase {
	Logger logger = LogManager.getLogger(OpenRefineTest.class.getName());

	public void testSomething() {
		logger.info("asdf");
		
		// Simulate the passed in Parameters "sid" and "tableName"
		int sid = 1711;
		String tableName = "sheet1";
		
		CreateProject createProject = new CreateProject();
		try {
			try {
				createProject.testCreateProject(sid, tableName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("entered");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(111);
	}
}
