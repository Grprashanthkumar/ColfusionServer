/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.ColumnTableInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.ColumnTableInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.DNameInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.DNameInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.DNameInfoMetadataEditHistoryDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.DNameInfoMetadataEditHistoryDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionColumnTableInfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfoMetadataEditHistory;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.dal.utils.MappingUtils;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.BasicTableInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DatasetVariableViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataHistoryLogRecordViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.WorksheetViewModel;

/**
 * @author Evgeny
 *
 */
public class DNameInfoManagerImpl extends GeneralManagerImpl<DNameInfoDAO, ColfusionDnameinfo, Integer> implements DNameInfoManager {

	Logger logger = LogManager.getLogger(DNameInfoManagerImpl.class.getName());
	
	public DNameInfoManagerImpl() {
		super(new DNameInfoDAOImpl(), ColfusionDnameinfo.class);
	}
	
	private final DNameInfoDAO dNameInfoDao = new DNameInfoDAOImpl();
	
	public enum VariableMetadataHistoryItem {
	    CHOSEN_NAME("chosen name"), ORIGINAL_NAME("original name"), DATA_TYPE("data type"), VALUE_UNIT("value unit"), 
	    DESCRIPTION("description"), FORMAT("format"), MISSING_VALUE("missing value"), IS_CONSTANT("isConstant"), CONSTANT_VALUE("constant value");	    
	    
	    private String value;

	    private VariableMetadataHistoryItem(final String value) {
	            this.value = value;
	    }
	    
	    public String getValue(){
	    	return this.value;
	    }
	    
	    static public boolean isMember(final String enumValueToTest) {
	    	VariableMetadataHistoryItem[] enumValues = VariableMetadataHistoryItem.values();
	        for (VariableMetadataHistoryItem enumValue : enumValues) {
				if (enumValue.value.equals(enumValueToTest)) {
					return true;
				}
			}
	        return false;
	    }
	};
	
	
	//***************************************
	// From Interface
	//***************************************
	
	@Override
	//TODO: maybe all should happen in one transaction, right now each variable is written in its own transaction
	public void createOrUpdateSheetMetadata(final WorksheetViewModel worksheet, final int sid, final int userId) throws Exception {
		for (DatasetVariableViewModel variable : worksheet.getVariables()) {
			
			if (!variable.isChecked()) { // if user didn't select the variable then we don't need to save it, at last for now.
				continue;
			}
			
			ColfusionDnameinfo variableMetadata = null;
			
			if (variable.getCid() > 0) {
				//need to update, the variable already stored in the db.
				variableMetadata = this.findByID(variable.getCid());	
			}
			
			if (variableMetadata == null) {
				//could not find the variable in the db, should we try to create it?					
				variableMetadata = this.addVariable(variable, sid, userId);					
			}
			else {
				//perform the update here and record the history					
				variableMetadata = this.updateVariable(variable, sid, userId);					
			}
			
			this.addOrUpdateTableNameRecordForNewVariable(variableMetadata, worksheet.getSheetName(), worksheet.getUniqueShortName());
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
	private ColfusionDnameinfo updateVariable(final DatasetVariableViewModel variable, final int sid, final int userId) throws Exception {
		
		ColfusionDnameinfo variableMeta = this.createVariableFromViewModel(variable, sid);
		variableMeta.setCid(variable.getCid());
		
		try {
            HibernateUtil.beginTransaction();
            
            this.handleHistoryEdits(variableMeta, userId);
            variableMeta = this._dao.merge(variableMeta);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
        	
        	HibernateUtil.rollbackTransaction();
            
        	this.logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	
        	HibernateUtil.rollbackTransaction();
        	
        	this.logger.error("findByID failed HibernateException", ex);
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
	private void handleHistoryEdits(final ColfusionDnameinfo newVariable, final int userId) throws Exception {
		DNameInfoMetadataEditHistoryDAO editHistorDAO = new DNameInfoMetadataEditHistoryDAOImpl();
		
		ColfusionDnameinfo oldVariable = this._dao.findByID(ColfusionDnameinfo.class, newVariable.getCid());
		
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
			this.logger.error(String.format("handleHistoryEdits failed due to one of many saveHistoryIfChanged calls for userId = %d", userId), e);
			
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
	private ColfusionDnameinfo addVariable(final DatasetVariableViewModel variable, final int sid, final int userId) throws Exception {
		
		ColfusionDnameinfo variableMeta = this.createVariableFromViewModel(variable, sid);
		
		//TODO:make sure cid is updated for the object)
		this.save(variableMeta);
		this.CreateMetadataHistory(variableMeta, userId);
		return variableMeta;
	}

	private ColfusionDnameinfo createVariableFromViewModel(final DatasetVariableViewModel variable, final int sid) throws Exception {
		//TODO: test maybe we don't need to search for the story, we can just create transparent object that might be enough
		SourceInfoManager sourceInfoMgr = new SourceInfoManagerImpl();
		
		ColfusionSourceinfo story = sourceInfoMgr.findByID(sid);
		
		if (story == null) {
			this.logger.error(String.format("addVariable fialed because the story with %d sid could be found", sid));
			
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
	 * @param 	column 
	 * 				{@link ColfusionDnameinfo} for which to add/update record about parent table.
	 * @param 	tableName 
	 * 				new/updated table name.
	 * @param	dbTableName
	 * 				name of the target database table that hold the data. This field should not be updated once it is set.
	 */
	private void addOrUpdateTableNameRecordForNewVariable(final ColfusionDnameinfo column, final String tableName, final String dbTableName) {
		
		try {
            HibernateUtil.beginTransaction();
            
            ColumnTableInfoDAO columnTableInfoDAO = new ColumnTableInfoDAOImpl();
            
            ColfusionColumnTableInfo existingRecord = columnTableInfoDAO.findByID(ColfusionColumnTableInfo.class, column.getCid());
            
            if (existingRecord != null && existingRecord.getDbTableName().equals(dbTableName)) {
            	String message = String.format("Currently cannot update dbTableName in the colfumnTableInfo table. "
            			+ "Cid '%d', Table Name '%s'. dbTableName '%s'", column.getCid(), tableName, dbTableName);
            	
            	logger.error(message);
            	throw new RuntimeException(message);
            }
            
            ColfusionColumnTableInfo columnTableInfo = new ColfusionColumnTableInfo(column, tableName, dbTableName);
            
            columnTableInfoDAO.saveOrUpdate(columnTableInfo);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
        	
        	HibernateUtil.rollbackTransaction();
            
        	this.logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	
        	HibernateUtil.rollbackTransaction();
        	
        	this.logger.error("findByID failed HibernateException", ex);
        }
	}

	@Override
	public List<ColfusionDnameinfo> getColumnsMetadata(final int sid, final String tableName) {
		try {
            HibernateUtil.beginTransaction();
            SourceInfoDAO storyDAO = new SourceInfoDAOImpl();
            ColfusionSourceinfo story = storyDAO.findByID(ColfusionSourceinfo.class, sid);
            
            Query query = HibernateUtil.getSession().createQuery("SELECT di FROM ColfusionDnameinfo di join di.colfusionColumnTableInfo where di.colfusionSourceinfo =:sid AND "
            		+ "di.colfusionColumnTableInfo.tableName = :tableName");
            
            query.setParameter("sid", story);
            query.setParameter("tableName", tableName);
            
            List<ColfusionDnameinfo> columnsMetadata = this._dao.findMany(query);
                        
            HibernateUtil.commitTransaction();
            
            return columnsMetadata;
		
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	this.logger.error("getColumnsMetadata failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	this.logger.error("getColumnsMetadata failed HibernateException", ex);
        	throw ex;
        }	
	}
	
	
	private void CreateMetadataHistory(final ColfusionDnameinfo variable, final int userId){
		 HibernateUtil.beginTransaction();
		 ColfusionUsers uid = (ColfusionUsers) HibernateUtil.getSession().get(ColfusionUsers.class, userId);
		 String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		 String reason = "";
		 
		 ColfusionDnameinfoMetadataEditHistory chosenName = new ColfusionDnameinfoMetadataEditHistory(variable, uid, new Date(), "chosen name", reason, variable.getDnameChosen());
		 HibernateUtil.getSession().save(chosenName);
		 
		 ColfusionDnameinfoMetadataEditHistory valueType = new ColfusionDnameinfoMetadataEditHistory(variable, uid, new Date(), "data type", reason, variable.getDnameValueType());
		 HibernateUtil.getSession().save(valueType);
		 
		 ColfusionDnameinfoMetadataEditHistory description = new ColfusionDnameinfoMetadataEditHistory(variable, uid, new Date(), "description", reason, variable.getDnameValueDescription()==null?"":variable.getDnameValueDescription());
		 HibernateUtil.getSession().save(description);
		
		 
		 ColfusionDnameinfoMetadataEditHistory variableMeasuringUnit = new ColfusionDnameinfoMetadataEditHistory(variable, uid, new Date(), "value unit", reason, variable.getDnameValueUnit()==null?"":variable.getDnameValueUnit());
		 HibernateUtil.getSession().save(variableMeasuringUnit);
		 
		 ColfusionDnameinfoMetadataEditHistory variableValueFormat = new ColfusionDnameinfoMetadataEditHistory(variable, uid, new Date(), "format", reason, variable.getDnameValueFormat()==null?"":variable.getDnameValueFormat());
		 HibernateUtil.getSession().save(variableValueFormat);
		 
		 ColfusionDnameinfoMetadataEditHistory missingValue = new ColfusionDnameinfoMetadataEditHistory(variable, uid, new Date(), "missing value", reason, variable.getMissingValue()==null?"":variable.getMissingValue());
		 HibernateUtil.getSession().save(missingValue);
		 
		 HibernateUtil.commitTransaction();
	}
	
	@Override
	public void addColumnMetaEditHistory(final int cid,final int userid,final String editAttribute,final String reason,final String editValue){
		HibernateUtil.beginTransaction();
		ColfusionDnameinfo Cid = (ColfusionDnameinfo)  HibernateUtil.getSession().get(ColfusionDnameinfo.class, cid);
		ColfusionUsers Uid = (ColfusionUsers) HibernateUtil.getSession().get(ColfusionUsers.class, userid);
		ColfusionDnameinfoMetadataEditHistory newhistory = new ColfusionDnameinfoMetadataEditHistory(Cid,Uid,new Date(),editAttribute,reason=="null"?"":reason,editValue=="null"?"":editValue);
		HibernateUtil.getSession().save(newhistory);
		HibernateUtil.commitTransaction();
		
		
	}
	
	@Override
	public List<StoryMetadataHistoryLogRecordViewModel> getColumnMetaEditHistory(final int cid, final String editAttribute){
		 HibernateUtil.beginTransaction();
		
		 ColfusionDnameinfo Cid = (ColfusionDnameinfo)  HibernateUtil.getSession().get(ColfusionDnameinfo.class, cid);
		
		 Query query = HibernateUtil.getSession().createQuery("SELECT di FROM ColfusionDnameinfoMetadataEditHistory di where di.colfusionDnameinfo = :cid AND di.editedAttribute = :editAttribute ORDER BY di.hid DESC");
		 query.setParameter("cid", Cid);
		 query.setParameter("editAttribute", editAttribute);

         List<ColfusionDnameinfoMetadataEditHistory> edithistory = query.list();
         
         List<StoryMetadataHistoryLogRecordViewModel> result = new ArrayList<StoryMetadataHistoryLogRecordViewModel>();
         for (ColfusionDnameinfoMetadataEditHistory onehistory : edithistory){
        
        	 Hibernate.initialize(onehistory.getColfusionUsers());
        	 
        	 result.add(new StoryMetadataHistoryLogRecordViewModel(onehistory.getHid(),MappingUtils.getInstance().mapColfusionUserToStoryAuthorViewModel(onehistory.getColfusionUsers()),onehistory.getWhenSaved(),onehistory.getEditedAttribute(),onehistory.getReason(),onehistory.getValue()));
         }
         
         HibernateUtil.commitTransaction();
         return result;         
	}

	@Override
	public List<BasicTableInfoViewModel> getTableInfo(final int sid) {
		try{
			HibernateUtil.beginTransaction();
	        
	        List<ColfusionDnameinfo> DNameInfoObjs = dNameInfoDao.findBySid(sid);
	        
	        List<BasicTableInfoViewModel> result = new ArrayList<>();
	        for(ColfusionDnameinfo DNameInfoObj : DNameInfoObjs){
	        	BasicTableInfoViewModel basicTableInfoViewModel =  new BasicTableInfoViewModel();
	        	basicTableInfoViewModel.setCid(DNameInfoObj.getCid());
	        	basicTableInfoViewModel.setDname_chosen(DNameInfoObj.getDnameChosen());
	        	basicTableInfoViewModel.setDname_original_name(DNameInfoObj.getDnameOriginalName());
	        	basicTableInfoViewModel.setDname_value_description(DNameInfoObj.getDnameValueDescription());
	        	basicTableInfoViewModel.setDname_value_type(DNameInfoObj.getDnameValueType());
	        	basicTableInfoViewModel.setDname_value_unit(DNameInfoObj.getDnameValueUnit());
	        	result.add(basicTableInfoViewModel);
	        }
	                               
	        return result;
	    } catch (NonUniqueResultException ex) {
	
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("getTableInfo failed NonUniqueResultException", ex);
	        throw ex;
	    } catch (HibernateException ex) {
	
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("getTableInfo failed HibernateException", ex);
	    	throw ex;
	    }	
	}
}


