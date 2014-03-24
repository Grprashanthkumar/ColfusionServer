/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.persistence.databaseHandlers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		
		DatabaseHandlerBase dbHandler1 = null;
		DatabaseHandlerBase dbHandler2 = null;
		
		try {
			dbHandler1 = DatabaseHandlerFactory.getDatabaseHandler(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
					Integer.valueOf(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
					"", DatabaseHanderType.MYSQL, null, 0);
			
			dbHandler2 = DatabaseHandlerFactory.getDatabaseHandler(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
					Integer.valueOf(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
					"", DatabaseHanderType.MYSQL, null, 0);
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error("testOpenConnections failed", e);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("testOpenConnections failed", e);
			e.printStackTrace();
		}
		finally {
			if (dbHandler1 != null) {
				try { 
					dbHandler1.close(); 
				} 
				catch (SQLException ignore) {
					
				}
			}
			
			if (dbHandler2 != null) {
				try { 
					dbHandler2.close(); 
				} 
				catch (SQLException ignore) {
					
				}
			}
		}
	}
	
	public void testCreateDeleteDatabase() {
		//TODO: do the test for all datahandler
		//TODO: add asserts the databases were really created and deleted.
		
		DatabaseHandlerBase dbHandler1 = null;
		
		try {
			dbHandler1 = DatabaseHandlerFactory.getDatabaseHandler(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
					Integer.valueOf(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
					"", DatabaseHanderType.MYSQL, null, 0);
			
			//Should not fail even though the database does not exist yet.
			dbHandler1.deleteDatabaseIfExists(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
			
			
			dbHandler1.createDatabaseIfNotExist(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
			
			// Should not fail even though the database already exists.
			dbHandler1.createDatabaseIfNotExist(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
			
			dbHandler1.deleteDatabaseIfExists(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error("testOpenConnections failed", e);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("testOpenConnections failed", e);
			e.printStackTrace();
		}
		finally {
			if (dbHandler1 != null) {
				try { 
					dbHandler1.close(); 
				} 
				catch (SQLException ignore) {
					
				}
			}
		}
	}
	
	public void testCreateTableAndDeleteDatabase() {
		//TODO: do the test for all datahandler
		//TODO: add asserts the databases were really created and deleted.
		
		DatabaseHandlerBase dbHandler1 = null;
		
		try {
			dbHandler1 = DatabaseHandlerFactory.getDatabaseHandler(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
					Integer.valueOf(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
					configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
					"", DatabaseHanderType.MYSQL, null, 0);
			
			
			dbHandler1.createDatabaseIfNotExist(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
			
			String tableName = "testTable";
			List<String> variables = new ArrayList<String>();
			variables.add("A a");
			variables.add("123");
						
			dbHandler1.createTableIfNotExist(tableName, variables);
			
			dbHandler1.deleteDatabaseIfExists(configManager.getPropertyByName(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			logger.error("testOpenConnections failed", e);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("testOpenConnections failed", e);
			e.printStackTrace();
		}
		finally {
			if (dbHandler1 != null) {
				try { 
					dbHandler1.close(); 
				} 
				catch (SQLException ignore) {
					
				}
			}
		}
	}
	
}
