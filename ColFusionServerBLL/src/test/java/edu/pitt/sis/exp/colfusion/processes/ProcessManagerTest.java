/**
 * 
 */
package edu.pitt.sis.exp.colfusion.processes;

import junit.framework.TestCase;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.dal.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.dataLoadExecutors.DataLoadExecutor;
import edu.pitt.sis.exp.colfusion.dataLoadExecutors.DataLoadExecutorFactory;
import edu.pitt.sis.exp.colfusion.process.ProcessBase;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;

/**
 * @author Evgeny
 *
 */
public class ProcessManagerTest extends TestCase {
	
	Logger logger = LogManager.getLogger(ProcessManagerTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	public ProcessManagerTest(final String name) {
		super(name);
	}
	
	@Test
	public void testProcessSerialization() {
		
		//Gson gson = new Gson();
		String json = "";
		
		try
        {
			TestProcess testProcess = new TestProcess();
			
			testProcess.setSid(123);
			testProcess.setID(1);

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
            assertEquals(testProcess.getID(), 1);
        }
        catch (Exception ex)
        {
            fail("Exception thrown during test: " + ex.toString());
        }
	}
	
	@Test
	public void testDataLoadExecutorProcessSerialization() {
		
		//Gson gson = new Gson();
		String json = "";
		
		Class<?> cl = null;
		
		try
        {
			DataLoadExecutor executor = DataLoadExecutorFactory.getDataLoadExecutor(DataSourceTypes.DATA_FILE);
			cl = executor.getClass();
						
			executor.setSid(123);
			
			// convert java object to JSON format,
			// and returned as JSON formatted string
			json = ProcessBase.toJson(executor);
        }
        catch (Exception ex)
        {
            fail("Exception thrown during test: " + ex.toString());
        }
        
        try
        {
        	DataLoadExecutor executor = (DataLoadExecutor) ProcessBase.fromJson(json, cl);
            
            assertEquals(executor.getSid(), 123);
        }
        catch (Exception ex)
        {
            fail("Exception thrown during test: " + ex.toString());
        }
	}
	
	//TODO: redo ProcessManagerTest.testQueueProcesses this test uses DB, so the DB might need to be prepared
	@Ignore
	@Test
	public void testQueueProcesses() {
//		TestProcess testProcess = new TestProcess();
//		
//		testProcess.setSid(123);
//		testProcess.setID(1);
//		
//		try {
//			int processId = ProcessManager.getInstance().queueProcess(testProcess);
//			
//			while (ProcessManager.getInstance().hasRunningProcesses()) {
//				logger.info(String.format("This many processes are running: %d", ProcessManager.getInstance().countRunningProcess()));
//				Thread.sleep(1000);
//			}
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			
//			logger.error(String.format("testQueueProcesses failed"), e);
//			
//			fail(String.format("testQueueProcesses failed."));
//		}
	}
}
