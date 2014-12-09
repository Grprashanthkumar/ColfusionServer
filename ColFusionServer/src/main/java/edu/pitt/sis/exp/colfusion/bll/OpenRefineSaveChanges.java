package edu.pitt.sis.exp.colfusion.bll;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.MetadataDbHandler;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;

public class OpenRefineSaveChanges {
	private static final String PROPERTY_LOCK_TIME = "lock_time";
	
	final Logger logger = LogManager.getLogger(OpenRefineSaveChanges.class.getName());
	
	/**
	 * 
	 * @param sid
	 * @param tableName
	 * @return
	 * @throws Exception 
	 */
	public GeneralResponseGen<String> saveChanges(final String projectId, final String colfusionUserId) throws Exception {
		
        int lockTime = Integer.valueOf(ConfigManager.getInstance().getPropertyByName(PROPERTY_LOCK_TIME));// Integer.valueOf(p.getProperty("lock_time"));
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
		result.setSuccessful(false);
		
		MetadataDbHandler metadataDbHandler = DatabaseHandlerFactory.getMetadataDbHandler();
        int sid = metadataDbHandler.getSid(projectId);
        DatabaseHandlerBase databaseHandler = DatabaseHandlerFactory.getTargetDatabaseHandler(sid);
				
        String tableName = metadataDbHandler.getTableNameByProjectId(projectId);
		
		String msg = "No change needs to be saved!";
		if(!metadataDbHandler.isTimeOutForCurrentUser(sid, tableName, Integer.valueOf(colfusionUserId), lockTime)) {
			if(databaseHandler.tempTableExist(sid, tableName)) {
				databaseHandler.removeTable(tableName);
				databaseHandler.createTableFromTable(tableName, "temp_" + tableName);
				databaseHandler.removeTable("temp_" + tableName);
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
