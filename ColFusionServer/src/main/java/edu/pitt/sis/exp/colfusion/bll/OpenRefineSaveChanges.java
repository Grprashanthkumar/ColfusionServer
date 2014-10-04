package edu.pitt.sis.exp.colfusion.bll;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandler;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.MetadataDbHandler;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.TargetDatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;

public class OpenRefineSaveChanges {
final Logger logger = LogManager.getLogger(OpenRefineSaveChanges.class.getName());
	
	

	/**
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public GeneralResponseGen<String> saveChanges(final String projectId, final String colfusionUserId) throws SQLException, ClassNotFoundException, IOException {
		
		Properties p = new Properties();
        String fileName="/config.properties";
        InputStream in = OpenRefineSaveChanges.class.getResourceAsStream(fileName);
        p.load(in);  
        in.close();
        
        int lockTime = Integer.valueOf(p.getProperty("lock_time"));
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
		result.setSuccessful(false);
		
		MetadataDbHandler metadataDbHandler = TargetDatabaseHandlerFactory.getMetadataDbHandler();
        int sid = metadataDbHandler.getSid(projectId);
        DatabaseHandler databaseHandler = TargetDatabaseHandlerFactory.getTargetDatabaseHandler(sid);
        String tableName = metadataDbHandler.getTableNameByProjectId(projectId);
		
		String msg = "No change needs to be saved!";
		if(!metadataDbHandler.isTimeOutForCurrentUser(sid, tableName, Integer.valueOf(colfusionUserId), lockTime)) {
			if(databaseHandler.tempTableExist(sid, tableName)) {
				databaseHandler.removeTable(sid, tableName);
				databaseHandler.createTable(sid, tableName);
				databaseHandler.removeTable(sid, "temp_" + tableName);
				msg = "Changes have been saved!";
			}
		} else {
			msg = "Time is out, cannot save!";
		}
		
		result.setMessage("OK");
		result.setPayload(msg);
		result.setSuccessful(true);
			
		
		return result;
	}
}
