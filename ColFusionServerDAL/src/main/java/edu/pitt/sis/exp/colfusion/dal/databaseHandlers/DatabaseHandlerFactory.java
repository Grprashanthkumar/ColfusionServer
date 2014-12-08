/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.databaseHandlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTargetDBViewModel;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;

/**
 * @author Evgeny
 *
 */
public class DatabaseHandlerFactory {
	final static Logger logger = LogManager.getLogger(DatabaseHandlerFactory.class.getName());
	
	//TODO FIXME
	 private static MetadataDbHandler metadataDbHandler = null;
	/**
	 * Creates a database handler of specific vendor depending on the database handler type value.
	 * @param host the url of the server.
	 * @param port the port number on which the database is running.
	 * @param user the name of the user to use for the connection.
	 * @param password the password of the user.
	 * @param database the database name which should be used. This parameter can be empty string if the database is doesn't exist yet.
	 * @param databaseHanderType the type of the database (vendor).
	 * @return the vendor specific implementation of the database handler.
	 * @throws Exception
	 */
	public static DatabaseHandlerBase getDatabaseHandler(final int sid, final String host, final int port, final String user, final String password, final String database, 
			final DatabaseHanderType databaseHanderType, final ExecutionInfoManager executionInfoMgr, final int executionLogId) throws Exception {
		switch (databaseHanderType) {
		case MYSQL:
			return new MySQLDatabaseHandler(sid, host, port, user, password, database, databaseHanderType, executionInfoMgr, executionLogId);
			
		default:
			
			if (executionInfoMgr != null) {
				executionInfoMgr.appendLog(executionLogId, String.format("[ERROR] getDatabaseHandler failed: DatabaseHandler type not found for %s", databaseHanderType.getValue()));
			}
			
			logger.error("DatabaseHandler type not found", databaseHanderType);
			
			//TODO, FIXME: throw proper exception
			throw new Exception("DatabaseHandler type not found");
		}
	}
	
	public static DatabaseHandlerBase getDatabaseHandler(final ColfusionSourceinfoDb storyDbInfo) throws Exception {
		return DatabaseHandlerFactory.getDatabaseHandler(storyDbInfo.getSid(), storyDbInfo.getServerAddress(), storyDbInfo.getPort(), 
				storyDbInfo.getUserName(), storyDbInfo.getPassword(), storyDbInfo.getSourceDatabase(), DatabaseHanderType.fromString(storyDbInfo.getDriver()), 
				null, -1);
	}
	
	public static DatabaseHandlerBase getDatabaseHandler(final StoryTargetDBViewModel storyDbInfo) throws Exception {
		return DatabaseHandlerFactory.getDatabaseHandler(storyDbInfo.getSid(), storyDbInfo.getServerAddress(), storyDbInfo.getPort(), 
				storyDbInfo.getUserName(), storyDbInfo.getPassword(), storyDbInfo.getDatabaseName(), DatabaseHanderType.fromString(storyDbInfo.getDriver()), 
				null, -1);
	}
	
    public static DatabaseHandlerBase getTargetDatabaseHandler(final int sid) throws Exception {
        
    	SourceInfoManager storyMng = new SourceInfoManagerImpl();
    	return getDatabaseHandler(storyMng.getStorySourceInfoDB(sid));
    	
//    	DatabaseConnectionInfo connectioInfo = metadataDbHandler.getTargetDbConnectionInfo(sid);
//      
//        return new MySQLDatabaseHandler(connectioInfo);
    }
    
	public static MetadataDbHandler getMetadataDbHandler() {
		//TODO FIXME: this is wrong
		if (metadataDbHandler == null) {
			String host = ConfigManager.getInstance().getPropertyByName("mysql_host");
	        int port = Integer.valueOf(ConfigManager.getInstance().getPropertyByName("mysql_port"));
	        String user = ConfigManager.getInstance().getPropertyByName("mysql_user"); 
	        String password = ConfigManager.getInstance().getPropertyByName("mysql_password"); 
	        String database = ConfigManager.getInstance().getPropertyByName("mysql_database"); 
	    	
	        //TODO:　Read host,port, etc. from config file and/or system properties
	        DatabaseConnectionInfo connectioInfo = new DatabaseConnectionInfo(host, port, user, password, database);
	        try{
	        	metadataDbHandler = new MetadataDbHandler(new MySQLDatabaseHandler(connectioInfo));
	        }
	        catch(Exception e)
	        {
	        	logger.error("Couldn't intinialize meatdata db handler", e);
	        }
		}
		
		return metadataDbHandler;
	}
}
