/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.databaseHandlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTargetDBViewModel;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

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
			try{
				Pattern pattern = Pattern.compile("/([A-Za-z0-9.]+):([0-9]+)/");
				Matcher matcher = pattern.matcher(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_URL));
				
				if (matcher.find()) {
					String host = matcher.group(1);
			        int port = Integer.valueOf(matcher.group(2));
			        
			        String user = ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_USERNAME); 
			        String password = ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_HIBERNATE_CONNECTION_PASSWORD); 
			        String database = ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_HIBERNATE_DEFAULT_CATALOG); 
			    	
			        //TODO:　Read host,port, etc. from config file and/or system properties
			        DatabaseConnectionInfo connectioInfo = new DatabaseConnectionInfo(host, port, user, password, database);
		      
		        	metadataDbHandler = new MetadataDbHandler(new MySQLDatabaseHandler(connectioInfo));
				}
				else {
					throw new Exception("Couldn't parse host and port from connection url.");
				}
	        }
	        catch(Exception e)
	        {
	        	logger.error("Couldn't intinialize meatdata db handler", e);
	        }
		}
		
		return metadataDbHandler;
	}
}
