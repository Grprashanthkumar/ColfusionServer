/**
 * 
 */
package edu.pitt.sis.exp.colfusion.tests.persistence.databaseHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;


/**
 * @author Evgeny
 *
 */
public class DatabaseHandlerFactoryTest extends DatabaseUnitTestBase {
	Logger logger = LogManager.getLogger(DatabaseHandlerFactoryTest.class.getName());
	ConfigManager configManager = ConfigManager.getInstance();
	
	//TODO: redo DatabaseHandlerFactoryTest.testOpenConnections
	@Ignore
	@Test
	public void testOpenConnections() {
		//TODO: do the test for all datahandler
		
//		DatabaseHandlerBase dbHandler1 = null;
//		DatabaseHandlerBase dbHandler2 = null;
//		
//		try {
//			dbHandler1 = DatabaseHandlerFactory.getDatabaseHandler(123, configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
//					Integer.valueOf(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
//					configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
//					configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
//					"", DatabaseHanderType.MYSQL, null, 0);
//			
//			dbHandler2 = DatabaseHandlerFactory.getDatabaseHandler(123, configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
//					Integer.valueOf(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
//					configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
//					configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
//					"", DatabaseHanderType.MYSQL, null, 0);
//			
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			logger.error("testOpenConnections failed", e);
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			logger.error("testOpenConnections failed", e);
//			e.printStackTrace();
//		}
//		finally {
//			if (dbHandler1 != null) {
//				dbHandler1.close(); 
//			}
//			
//			if (dbHandler2 != null) {
//				dbHandler2.close(); 
//			}
//		}
	}
	
	//TODO: redo DatabaseHandlerFactoryTest.testCreateDeleteDatabase
	@Ignore
	@Test
	public void testCreateDeleteDatabase() {
		//TODO: do the test for all datahandler
		//TODO: add asserts the databases were really created and deleted.
		
//		DatabaseHandlerBase dbHandler1 = null;
//		
//		try {
//			dbHandler1 = DatabaseHandlerFactory.getDatabaseHandler(123, configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
//					Integer.valueOf(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
//					configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
//					configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
//					"", DatabaseHanderType.MYSQL, null, 0);
//			
//			//Should not fail even though the database does not exist yet.
//			dbHandler1.deleteDatabaseIfExists(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
//			
//			
//			dbHandler1.createDatabaseIfNotExist(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
//			
//			// Should not fail even though the database already exists.
//			dbHandler1.createDatabaseIfNotExist(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
//			
//			dbHandler1.deleteDatabaseIfExists(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
//			
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			logger.error("testOpenConnections failed", e);
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			logger.error("testOpenConnections failed", e);
//			e.printStackTrace();
//		}
//		finally {
//			if (dbHandler1 != null) {
//					dbHandler1.close();
//			}
//		}
	}
	
	//TODO: redo DatabaseHandlerFactoryTest.testCreateTableAndDeleteDatabase
	@Ignore
	@Test
	public void testCreateTableAndDeleteDatabase() {
		//TODO: do the test for all datahandler
		//TODO: add asserts the databases were really created and deleted.
		
//		DatabaseHandlerBase dbHandler1 = null;
//		
//		try {
//			dbHandler1 = DatabaseHandlerFactory.getDatabaseHandler(123, configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Server), 
//					Integer.valueOf(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Port)), 
//					configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_UserName), 
//					configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_Password), 
//					"", DatabaseHanderType.MYSQL, null, 0);
//			
//			
//			dbHandler1.createDatabaseIfNotExist(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
//			
//			String tableName = "testTable";
//			List<String> variables = new ArrayList<String>();
//			variables.add("A a");
//			variables.add("123");
//						
//			dbHandler1.createTableIfNotExist(tableName, variables);
//			
//			dbHandler1.deleteDatabaseIfExists(configManager.getProperty(PropertyKeysTest.testTargetFileToDBDatabase_DatabaseNamePrefix));
//			
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			logger.error("testOpenConnections failed", e);
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			logger.error("testOpenConnections failed", e);
//			e.printStackTrace();
//		}
//		finally {
//			if (dbHandler1 != null) {
//				dbHandler1.close(); 
//			}
//		}
	}

}
