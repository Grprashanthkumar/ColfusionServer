/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.persistence.databaseHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.ConfigManager;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHanderType;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.tests.PropertyKeysTest;
import junit.framework.TestCase;

/**
 * @author Evgeny
 *
 */
public class DatabaseHandlerFactoryTest extends TestCase {
	Logger logger = LogManager.getLogger(DatabaseHandlerFactoryTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	public void testOpenConnections() {
		//TODO: do the test for all datahandler
		
		try {
			DatabaseHandlerBase dbHandler1 = DatabaseHandlerFactory.getDatabaseHandler(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
					Integer.valueOf(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
					"", DatabaseHanderType.MYSQL);
			
			DatabaseHandlerBase dbHandler2 = DatabaseHandlerFactory.getDatabaseHandler(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
					Integer.valueOf(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
					"", DatabaseHanderType.MYSQL);
			
			dbHandler1.close();
			dbHandler2.close();
			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error("testOpenConnections failed", e);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("testOpenConnections failed", e);
			e.printStackTrace();
		}
	}
	
}
