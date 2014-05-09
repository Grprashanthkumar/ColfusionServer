/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.ColumnTableInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.ColumnTableInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.DNameInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.DNameInfoMetadataEditHistoryDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.DNameInfoMetadataEditHistoryDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionColumnTableInfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.WorksheetViewModel;

/**
 * @author Evgeny
 *
 */
public class DNameInfoManagerImpl extends GeneralManagerImpl<ColfusionDnameinfo, Integer> implements DNameInfoManager {

	Logger logger = LogManager.getLogger(DNameInfoManagerImpl.class.getName());
	
	public DNameInfoManagerImpl() {
		super(new DNameInfoDAOImpl(), ColfusionDnameinfo.class);
	}
	
	public enum VariableMetadataHistoryItem {
	    CHOSEN_NAME("chosen name"), ORIGINAL_NAME("original name"), DATA_TYPE("data type"), VALUE_UNIT("value unit"), 
	    DESCRIPTION("description"), FORMAT("format"), MISSING_VALUE("missing value"), IS_CONSTANT("isConstant"), CONSTANT_VALUE("constant value");	    
	    
	    private String value;

	    private VariableMetadataHistoryItem(String value) {
	            this.value = value;
	    }
	    
	    public String getValue(){
	    	return this.value;
	    }
	    
	    static public boolean isMember(String enumValueToTest) {
	    	VariableMetadataHistoryItem[] enumValues = VariableMetadataHistoryItem.values();
	        for (VariableMetadataHistoryItem enumValue : enumValues)
	            if (enumValue.value.equals(enumValueToTest))
	                return true;
	        return false;
	    }
	};
	
	
	//***************************************
	// From Interface
	//***************************************
	
	@Override
	//TODO: maybe all should happen in one transaction, righ now each variable is written in its own transaction
	public void createOrUpdateSheetMetadata(WorksheetViewModel worksheet, String tableNamePrefix, int sid, int userId) throws Exception {
		for (DatasetVariableViewModel variable : worksheet.getVariables()) {
			
			if (!variable.isChecked()) { // if user didn't select the variable then we don't need to save it, at last for now.
				continue;
			}
			
			if (variable.getCid() > 0) {
				//need to update, the variable already stored in the db.
				ColfusionDnameinfo variableMeta = this.findByID(variable.getCid());
				
				if (variableMeta == null) {
					//could not find the variable in the db, should we try to create it?
					
					ColfusionDnameinfo addedColumn = addVariable(variable, sid);
					addOrUpdateTableNameRecordForNewVariable(addedColumn, tableNamePrefix + worksheet.getSheetName());
				}
				else {
					//perform the update here and record the history
					
					ColfusionDnameinfo updatedColumn = updateVariable(variable, sid, userId);
					addOrUpdateTableNameRecordForNewVariable(updatedColumn, tableNamePrefix + worksheet.getSheetName());
				}
			}
			else if (variable.getCid() == 0) {
				//variable has not been saved to the db yet, need to save
				
				ColfusionDnameinfo addedColumn = addVariable(variable, sid);
				addOrUpdateTableNameRecordForNewVariable(addedColumn, tableNamePrefix + worksheet.getSheetName());
			}
			else {
				//TODO: implement, throw an error, cid cannot be less than 0.
				
				logger.error(String.format("createOrUpdateSheetMetadata fialed because the cid is less than 0 - %d", variable.getCid()));
				
				throw new Exception("createOrUpdateSheetMetadata fialed because the cid is less than 0.");
			}
		}
	}

	/**
	 * 
	 * @param variable
	 * @param sid
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	private ColfusionDnameinfo updateVariable(DatasetVariableViewModel variable, int sid, int userId) throws Exception {
		
		ColfusionDnameinfo variableMeta = createVariableFromViewModel(variable, sid);
		variableMeta.setCid(variable.getCid());
		
		try {
            HibernateUtil.beginTransaction();
            
            handleHistoryEdits(variableMeta, userId);
            variableMeta = _dao.merge(variableMeta);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
        	
        	HibernateUtil.rollbackTransaction();
            
        	logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	
        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed HibernateException", ex);
        }
		 
        return variableMeta;
	}

	/**
	 * Checks if new instance of a record of dnameinfo has different values from the one stored in the database already.
	 * If they do differ, then old value is copied into history of edits table before replaced by new value.
	 * 
	 * @param newVariable new instance of the dnameinfo record possibly updated by user during edit operation.
	 * @param userId id of the user who did edit.
	 * @throws Exception 
	 */
	//TODO: add reason
	private void handleHistoryEdits(ColfusionDnameinfo newVariable, int userId) throws Exception {
		DNameInfoMetadataEditHistoryDAO editHistorDAO = new DNameInfoMetadataEditHistoryDAOImpl();
		
		ColfusionDnameinfo oldVariable = _dao.findByID(ColfusionDnameinfo.class, newVariable.getCid());
		
		try {
			String oldValue = (oldVariable == null) ? null : oldVariable.getDnameChosen();
			editHistorDAO.saveHistoryIfChanged(newVariable.getCid(), userId, oldValue, newVariable.getDnameChosen(), VariableMetadataHistoryItem.CHOSEN_NAME,  "");
		
			oldValue = (oldVariable == null) ? null : oldVariable.getDnameOriginalName();
			editHistorDAO.saveHistoryIfChanged(newVariable.getCid(), userId, oldValue, newVariable.getDnameOriginalName(), VariableMetadataHistoryItem.ORIGINAL_NAME,  "");
			
			oldValue = (oldVariable == null) ? null : oldVariable.getDnameValueType();
			editHistorDAO.saveHistoryIfChanged(newVariable.getCid(), userId, oldValue, newVariable.getDnameValueType(), VariableMetadataHistoryItem.DATA_TYPE,  "");
			
			oldValue = (oldVariable == null) ? null : oldVariable.getDnameValueUnit();
			editHistorDAO.saveHistoryIfChanged(newVariable.getCid(), userId, oldValue, newVariable.getDnameValueUnit(), VariableMetadataHistoryItem.VALUE_UNIT,  "");
			
			oldValue = (oldVariable == null) ? null : oldVariable.getDnameValueDescription();
			editHistorDAO.saveHistoryIfChanged(newVariable.getCid(), userId, oldValue, newVariable.getDnameValueDescription(), VariableMetadataHistoryItem.DESCRIPTION,  "");
			
			oldValue = (oldVariable == null) ? null : oldVariable.getDnameValueFormat();
			editHistorDAO.saveHistoryIfChanged(newVariable.getCid(), userId, oldValue, newVariable.getDnameValueFormat(), VariableMetadataHistoryItem.FORMAT,  "");
			
			oldValue = (oldVariable == null) ? null : oldVariable.getMissingValue();
			editHistorDAO.saveHistoryIfChanged(newVariable.getCid(), userId, oldValue, newVariable.getMissingValue(), VariableMetadataHistoryItem.MISSING_VALUE,  "");
			
			//TODO: isConstant is missing, need to add to check if it was changed.
			oldValue = (oldVariable == null) ? null : oldVariable.getConstantValue();
			editHistorDAO.saveHistoryIfChanged(newVariable.getCid(), userId, oldValue, newVariable.getConstantValue(), VariableMetadataHistoryItem.CONSTANT_VALUE,  "");
		
		} catch (Exception e) {
			logger.error(String.format("handleHistoryEdits failed due to one of many saveHistoryIfChanged calls for userId = %d", userId), e);
			
			throw e;
		}
	}
	
	/**
	 * Adds a new variable based on provided variable view model and sid for which story to add the variable.
	 * 
	 * @param variable metadata of the variable to be added.
	 * @param sid id of the story for which to add the variable.
	 * @return added columns as {@link ColfusionDnameinfo} object which is the id of created/inserted variable in the database.
	 * @throws Exception if they story for which new variable is added cannot be found in the database, then the exception is thrown.
	 */
	private ColfusionDnameinfo addVariable(DatasetVariableViewModel variable, int sid) throws Exception {
		
		ColfusionDnameinfo variableMeta = createVariableFromViewModel(variable, sid);
		
		//TODO:make sure cid is updated for the object)
		save(variableMeta);
		
		return variableMeta;
	}

	private ColfusionDnameinfo createVariableFromViewModel(DatasetVariableViewModel variable, int sid) throws Exception {
		//TODO: test maybe we don't need to search for the story, we can just create transparent object that might be enough
		SourceInfoManager sourceInfoMgr = new SourceInfoManagerImpl();
		
		ColfusionSourceinfo story = sourceInfoMgr.findByID(sid);
		
		if (story == null) {
			logger.error(String.format("addVariable fialed because the story with %d sid could be found", sid));
			
			throw new Exception("Could not find a story for which to create a variable");
		}
		
		//TODO:update this when update database to add format
		//TODO:update is constant when add it to the model
		//TODO: the null will be deleted once update the db
		ColfusionDnameinfo variableMeta = new ColfusionDnameinfo(story,variable.getChosenName(), variable.getOriginalName(), false); 
				
		//TODO:add setting constant value.
		variableMeta.setDnameValueType(variable.getVariableValueType());
		variableMeta.setDnameValueUnit(variable.getVariableMeasuringUnit());
		variableMeta.setDnameValueFormat(variable.getVariableValueFormat());
		variableMeta.setDnameValueDescription(variable.getDescription());
		variableMeta.setMissingValue(variable.getMissingValue());
		
		return variableMeta;
	}
	
	/**
	 * Add or updates record about the table name for given column and given table name.
	 * 
	 * @param column {@link ColfusionDnameinfo} for which to add/update record about parent table.
	 * @param tableName new/updated table name.
	 */
	private void addOrUpdateTableNameRecordForNewVariable(ColfusionDnameinfo column, String tableName) {
		
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionColumnTableInfo columnTableInfo = new ColfusionColumnTableInfo(column, tableName);
            
            ColumnTableInfoDAO columnTableInfoDAO = new ColumnTableInfoDAOImpl();
            
            columnTableInfoDAO.saveOrUpdate(columnTableInfo);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
        	
        	HibernateUtil.rollbackTransaction();
            
        	logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	
        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed HibernateException", ex);
        }
	}
}
