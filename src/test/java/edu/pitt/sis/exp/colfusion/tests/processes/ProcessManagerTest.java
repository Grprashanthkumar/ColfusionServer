/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.processes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.process.ProcessBase;
import junit.framework.TestCase;

/**
 * @author Evgeny
 *
 */
public class ProcessManagerTest extends TestCase {
	
	Logger logger = LogManager.getLogger(ProcessManagerTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	public ProcessManagerTest(String name) {
		super(name);
	}
	
	public void testProcessSerialization() {
		
		//Gson gson = new Gson();
		String json = "";
		
		try
        {
			TestProcess testProcess = new TestProcess();
			
			testProcess.setSid(123);
			testProcess.setUniqueName("UniqName123");

			// convert java object to JSON format,
			// and returned as JSON formatted string
			json = ProcessBase.toJson(testProcess);
        }
        catch (Exception ex)
        {
            fail("Exception thrown during test: " + ex.toString());
        }
        
        try
        {
        	TestProcess testProcess = ProcessBase.fromJson(json, TestProcess.class);
            
            assertEquals(testProcess.getSid(), 123);
            assertEquals(testProcess.getUniqueName(), "UniqName123");
        }
        catch (Exception ex)
        {
            fail("Exception thrown during test: " + ex.toString());
        }
	}
}
