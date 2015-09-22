package edu.pitt.sis.exp.colfusion.bll;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponseGenImpl;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.MetadataDbHandler;
import edu.pitt.sis.exp.colfusion.utils.ConfigManager;
import edu.pitt.sis.exp.colfusion.utils.PropertyKeys;

public class OpenRefineSaveChanges {

	final Logger logger = LogManager.getLogger(OpenRefineSaveChanges.class.getName());

	/**
	 *
	 * @param sid
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public GeneralResponseGen<String> saveChanges(final String projectId, final String colfusionUserId) throws Exception {

		final int lockTime = Integer.valueOf(ConfigManager.getInstance().getProperty(PropertyKeys.COLFUSION_OPENREFINE_LOCK_TIME));

		final GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();

		result.setSuccessful(false);

		final MetadataDbHandler metadataDbHandler = DatabaseHandlerFactory.getMetadataDbHandler();
		final int sid = metadataDbHandler.getSid(projectId);
		final DatabaseHandlerBase databaseHandler = DatabaseHandlerFactory.getTargetDatabaseHandler(sid);

		final RelationKey table = metadataDbHandler.getTableNameByProjectId(projectId);

		String msg = "No change needs to be saved!";
		if(!metadataDbHandler.isTimeOutForCurrentUser(sid, table, Integer.valueOf(colfusionUserId), lockTime)) {
			if(databaseHandler.tempTableExist(sid, table)) {
				databaseHandler.removeTable(table.getDbTableName());
				databaseHandler.createTableFromTable(table.getDbTableName(), "temp_" + table.getDbTableName());
				databaseHandler.removeTable("temp_" + table.getDbTableName());
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
