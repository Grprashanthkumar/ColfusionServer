package edu.pitt.sis.exp.colfusion.bll;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import com.google.refine.ProjectMetadata;
//import com.google.refine.model.Project;

import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;

public class OpenRefineTest {
final Logger logger = LogManager.getLogger(OpenRefineBL.class.getName());
	
	

	/**
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 */
	public GeneralResponseGen<String> createProjectTest(int sid, String tableName) {
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
//		TestClass  ttClass = new TestClass();
//		ttClass.testValue();
//		ttClass.testSid(sid, tableName);
//		ttClass.testOpenRefine();
//		test_1 myTest = new test_1();
//		myTest.testProject1(sid, tableName);
//		Project newProject = new Project();
//		ProjectMetadata ppm = new ProjectMetadata();
		
//		String cmd="yourfile.exe"m;
//		//如需多个参数，可以改成String[]cmd,然后cmd[i]放置命令参数。
//		Runtime rt = Runtime.getRuntime();
//		try {
//			Process proc = rt.exec(cmd);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		result.setMessage("OK");
		result.setPayload("Hey! You win bbbbbb!");
		result.setSuccessful(true);
		
		return result;
	}
}
