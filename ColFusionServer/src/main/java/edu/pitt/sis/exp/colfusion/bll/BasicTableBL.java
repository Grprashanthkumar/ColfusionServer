package edu.pitt.sis.exp.colfusion.bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.managers.AttachmentManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.ColumnTableInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ColumnTableInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.ExecutionInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionColumnTableInfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionExecuteinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.AttachmentListViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.BasicTableInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DnameViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryListViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryStatusViewModel;
import edu.pitt.sis.exp.colfusion.responseModels.AttachmentListResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.BasicTableResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.DnameResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.responseModels.RelationshipsResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.StoryListResponseModel;
import edu.pitt.sis.exp.colfusion.responseModels.StoryStatusResponseModel;

public class BasicTableBL {

	private static Logger logger = LogManager.getLogger(BasicTableBL.class.getName());
	
	public BasicTableResponseModel getTableInfo(final int sid, final String tableName ){
		
		BasicTableResponseModel result = new BasicTableResponseModel();
		try{
			DNameInfoManagerImpl dNameInfoManagerImpl = new DNameInfoManagerImpl();
			List<BasicTableInfoViewModel> contents = dNameInfoManagerImpl.getTableInfo(sid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(Exception e) {
			result.isSuccessful=false;
			result.message = "Get DNameInfo failed";
		}
		return result;
	
		
	}
	
	//Resturn Table Response according to sid and tableName.
	public JointTableByRelationshipsResponeModel getTableDataBySidAndName(final int sid, final String tableName,
			final int perPage, final int pageNumber) {
		SourceInfoManagerImpl sourceInfo = new SourceInfoManagerImpl();
		
		JointTableByRelationshipsResponeModel result = new JointTableByRelationshipsResponeModel(); 
		
		try {
			ColfusionSourceinfoDb storyTargetDB = sourceInfo.getStorySourceInfoDB(sid);
			
			DatabaseHandlerBase databaseHandlerBase = DatabaseHandlerFactory.getDatabaseHandler(storyTargetDB);
			
			ColumnTableInfoManager columnTableMng = new ColumnTableInfoManagerImpl();
			ColfusionColumnTableInfo columnTable = columnTableMng.findBySidAndOriginalTableName(sid, tableName);
			RelationKey relationKey = new RelationKey(tableName, columnTable.getDbTableName());
			
			Table table = databaseHandlerBase.getAll(relationKey, perPage, pageNumber);
			
			int countTuples = databaseHandlerBase.getCount(relationKey);
			int totalPage = (int) Math.ceil((double)countTuples / perPage);
			
			JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();
			payload.setJointTable(table);
			payload.setPerPage(perPage);
			payload.setPageNo(pageNumber);
			payload.setTotalPage(totalPage);
			
			result.setPayload(payload);
			
			result.isSuccessful = true;
		} catch (Exception e) {
			logger.error(e);
			result.isSuccessful = false;
			result.message = e.getMessage();
		}
		return result;
	}
	
	//TODO: need to be refactored
	//Return table relationship by sid and tableName
	public JointTableByRelationshipsResponeModel getTableDataBySidAndName(final int sid, final String tableName){
		
		logger.info(String.format("Getting table data for sid '%d' and table name '%s'", sid, tableName));
		
		SourceInfoManagerImpl sourceInfo = new SourceInfoManagerImpl();
		
		JointTableByRelationshipsResponeModel result = new JointTableByRelationshipsResponeModel(); 
		
		try {
			ColfusionSourceinfoDb storyTargetDB = sourceInfo.getStorySourceInfoDB(sid);
			
			if (storyTargetDB == null) {
				String message = String.format("getTableDataBySidAndName: is null for sid '%d'", sid);
				logger.info(message);
				throw new Exception(message);
			}
			
			DatabaseHandlerBase databaseHandlerBase = DatabaseHandlerFactory.getDatabaseHandler(storyTargetDB);
			
			ColumnTableInfoManager columnTableMng = new ColumnTableInfoManagerImpl();
			ColfusionColumnTableInfo columnTable = columnTableMng.findBySidAndOriginalTableName(sid, tableName);
			RelationKey relationKey = new RelationKey(tableName, columnTable.getDbTableName());
			
			Table table = databaseHandlerBase.getAll(relationKey);
			
			JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();
			payload.setJointTable(table);
			result.setPayload(payload);
			
			result.isSuccessful = true;
		} catch (Exception e) {
			logger.error(e);
			result.isSuccessful=false;
			result.message = "Get TableDataBySidAndName failed. " + e.getMessage();
		}
		
		return result;
	}
	
	//Return the story status response according to sid
	public StoryStatusResponseModel getStoryStatus(final int sid){
		StoryStatusResponseModel result = new StoryStatusResponseModel();
		List<StoryStatusViewModel> contents = new ArrayList<StoryStatusViewModel>();
		try {
			SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();
			
			List<RelationKey> tableNames = sourceInfoManagerImpl.getTableNames(sid);
			
			ExecutionInfoManagerImpl executionInfoManagerImpl = new ExecutionInfoManagerImpl();
			for(int i = 0; i < tableNames.size(); i++) {
				ColfusionExecuteinfo colfusionExecuteinfo = executionInfoManagerImpl.getExecutionInfo(sid, tableNames.get(i).getDbTableName());
				StoryStatusViewModel content = new StoryStatusViewModel();
				content.setEid(colfusionExecuteinfo.getEid());
				content.setSid(sid);
				content.setUserId(colfusionExecuteinfo.getUserId());
				content.setTimeStart(colfusionExecuteinfo.getTimeStart());
				content.setTimeEnd(colfusionExecuteinfo.getTimeEnd());
				content.setExitStatus(colfusionExecuteinfo.getExitStatus());
				content.setErrorMessage(colfusionExecuteinfo.getErrorMessage());
				content.setRecordsProcessed(colfusionExecuteinfo.getRecordsProcessed());
				content.setStatus(colfusionExecuteinfo.getStatus());
				content.setPanCommand(colfusionExecuteinfo.getPanCommand());
				content.setTableName(tableNames.get(i).getTableName());
				content.setLog(colfusionExecuteinfo.getLog());
				//??What is NumberProcessRecords???
				content.setNumberProcessRecords(null);
				contents.add(content);
			}
			result.setPayload(contents);
			result.isSuccessful = true;
		}
		catch(Exception e) {
			result.isSuccessful = false;
			result.message = "Get StoryStatus failed";
		}
		
		return result;	
	}
	
	
	//Return the relationship response according to sid.
	public RelationshipsResponseModel getRelationships(final int sid,
			final int perPage, final int pageNumber) {
		RelationshipsResponseModel result = new RelationshipsResponseModel();
		// The following code is to get data from the table and store it into payload.
		try {
			SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();
			List<RelationshipsViewModel> contents = sourceInfoManagerImpl.getRelationshipsViewModel(sid);
			result.setPayload(contents);
			result.getControl().setPerPage(perPage);
			result.getControl().setPageNO(pageNumber);
			result.getControl().setTotalPage(40);
//			String [] cols = {"rel_id","name","description","creator","creationTime","creatorLogin","sidFrom","sidTo","titleFrom","titleTo","tableNameFrom","tableNameTo","numberOfVerdicts","numberOfApproved","numberOfNotSure","avgConfidence"};
//			result.getControl().setCols(cols);
			//TODO FIXME: this is wrong. Hardwired column names are wrong. They should be obtained programmatically.
			if(contents.size() > 0){
				result.getControl().setCols("rel_id,name,description,creator,creationTime,creatorLogin,sidFrom,sidTo,titleFrom,titleTo,tableNameFrom,tableNameTo,numberOfVerdicts,numberOfApproved,numberOfNotSure,avgConfidence");
			}
		}
		catch(Exception e) {
			logger.error("failed to get relationshipResponseModel");
			result.isSuccessful=false;
			result.message = "Get MineRelationships failed";
		}

		return result;
	}
	
	public AttachmentListResponseModel getAttachmentList(final int sid){
		AttachmentListResponseModel result = new AttachmentListResponseModel();
		try{
			AttachmentManagerImpl attachmentManagerImpl = new AttachmentManagerImpl();
			List<AttachmentListViewModel> contents = attachmentManagerImpl.getAttachmentListViewModel(sid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(Exception e) {
			result.isSuccessful=false;
			result.message = "Get AttachmentList failed";
		}
		return result;
	}
	
	
	
	public StoryListResponseModel getStoryList(final int pageNo, final int perPage){
		StoryListResponseModel result = new StoryListResponseModel();
		try{
			SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			List<StoryListViewModel> contents = SourceInfoManagerImpl.getStoryListViewModel(pageNo, perPage);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(Exception e) {
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}
	
	public StoryListResponseModel getAllStoryList(){
		StoryListResponseModel result = new StoryListResponseModel();
		try{
			SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			List<StoryListViewModel> contents = SourceInfoManagerImpl.getStoryListViewModel();
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(Exception e) {
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}
	
	public StoryListResponseModel getStoryListBySid(final int sid){
		StoryListResponseModel result = new StoryListResponseModel();
		try{
			SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			List<StoryListViewModel> contents = SourceInfoManagerImpl.getStoryListViewModelBySid(sid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(Exception e) {
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}
	
	public DnameResponseModel getDnameListBySid(final int sid){
		DnameResponseModel result = new DnameResponseModel();
		try{
			DNameInfoManagerImpl dNameInfoManagerImpl = new DNameInfoManagerImpl();
			List<DnameViewModel> contents = dNameInfoManagerImpl.getDnameListViewModelBySid(sid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(Exception e){
			result.isSuccessful=false;
			result.message = "Get DnameList failed";
		}
		return result;
	}
	
	public DnameResponseModel getDnameListByCid(final int cid){
		DnameResponseModel result = new DnameResponseModel();
		try{
			DNameInfoManagerImpl dNameInfoManagerImpl = new DNameInfoManagerImpl();
			List<DnameViewModel> contents = dNameInfoManagerImpl.getDnameListViewModelByCid(cid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(Exception e){
			result.isSuccessful=false;
			result.message = "Get DnameList failed";
		}
		return result;
	}
}
