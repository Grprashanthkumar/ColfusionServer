package edu.pitt.sis.exp.colfusion.bll;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.responseModels.AttachmentListResponseModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.BasicTableResponseModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.DnameResponseModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.JointTableByRelationshipsResponeModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.RelationshipsResponseModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.StoryListResponseModel;
import edu.pitt.sis.exp.colfusion.bll.responseModels.StoryStatusResponseModel;
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
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.AttachmentListViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.BasicTableInfoViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.DnameViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.JoinTablesByRelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.LicenseViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryListViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryStatusViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.UserViewModel;

public class BasicTableBL {

	private static Logger logger = LogManager.getLogger(BasicTableBL.class.getName());

	public BasicTableResponseModel getTableInfo(final int sid, final String tableName ){

		final BasicTableResponseModel result = new BasicTableResponseModel();
		try{
			final DNameInfoManagerImpl dNameInfoManagerImpl = new DNameInfoManagerImpl();
			final List<BasicTableInfoViewModel> contents = dNameInfoManagerImpl.getTableInfo(sid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			result.isSuccessful=false;
			result.message = "Get DNameInfo failed";
		}
		return result;


	}

	//Return Table Response according to sid and tableName.
	public JointTableByRelationshipsResponeModel getTableDataBySidAndName(final int sid, final String tableName,
			final int perPage, final int pageNumber) {
		final SourceInfoManagerImpl sourceInfo = new SourceInfoManagerImpl();

		final JointTableByRelationshipsResponeModel result = new JointTableByRelationshipsResponeModel();

		try {
			final ColfusionSourceinfoDb storyTargetDB = sourceInfo.getStorySourceInfoDB(sid);

			final DatabaseHandlerBase databaseHandlerBase = DatabaseHandlerFactory.getDatabaseHandler(storyTargetDB);

			final ColumnTableInfoManager columnTableMng = new ColumnTableInfoManagerImpl();
			final ColfusionColumnTableInfo columnTable = columnTableMng.findBySidAndOriginalTableName(sid, tableName);
			final RelationKey relationKey = new RelationKey(tableName, columnTable.getDbTableName());

			final Table table = databaseHandlerBase.getAll(relationKey, perPage, pageNumber);

			final int countTuples = databaseHandlerBase.getCount(relationKey);
			final int totalPage = (int) Math.ceil((double)countTuples / perPage);

			final JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();
			payload.setJointTable(table);
			payload.setPerPage(perPage);
			payload.setPageNo(pageNumber);
			payload.setTotalPage(totalPage);

			result.setPayload(payload);

			result.isSuccessful = true;
		} catch (final Exception e) {
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

		final SourceInfoManagerImpl sourceInfo = new SourceInfoManagerImpl();

		final JointTableByRelationshipsResponeModel result = new JointTableByRelationshipsResponeModel();

		try {
			final ColfusionSourceinfoDb storyTargetDB = sourceInfo.getStorySourceInfoDB(sid);

			if (storyTargetDB == null) {
				final String message = String.format("getTableDataBySidAndName: is null for sid '%d'", sid);
				logger.info(message);
				throw new Exception(message);
			}

			final DatabaseHandlerBase databaseHandlerBase = DatabaseHandlerFactory.getDatabaseHandler(storyTargetDB);

			final ColumnTableInfoManager columnTableMng = new ColumnTableInfoManagerImpl();
			final ColfusionColumnTableInfo columnTable = columnTableMng.findBySidAndOriginalTableName(sid, tableName);
			final RelationKey relationKey = new RelationKey(tableName, columnTable.getDbTableName());

			final Table table = databaseHandlerBase.getAll(relationKey);

			final JoinTablesByRelationshipsViewModel payload = new JoinTablesByRelationshipsViewModel();
			payload.setJointTable(table);
			result.setPayload(payload);

			result.isSuccessful = true;
		} catch (final Exception e) {
			logger.error(e);
			result.isSuccessful=false;
			result.message = "Get TableDataBySidAndName failed. " + e.getMessage();
		}

		return result;
	}

	//Return the story status response according to sid
	public StoryStatusResponseModel getStoryStatus(final int sid){
		final StoryStatusResponseModel result = new StoryStatusResponseModel();
		final List<StoryStatusViewModel> contents = new ArrayList<StoryStatusViewModel>();
		try {
			final SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();

			final List<RelationKey> tableNames = sourceInfoManagerImpl.getTableNames(sid);

			final ExecutionInfoManagerImpl executionInfoManagerImpl = new ExecutionInfoManagerImpl();
			for(int i = 0; i < tableNames.size(); i++) {
				final ColfusionExecuteinfo colfusionExecuteinfo = executionInfoManagerImpl.getExecutionInfo(sid, tableNames.get(i).getDbTableName());
				final StoryStatusViewModel content = new StoryStatusViewModel();
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
		catch(final Exception e) {
			result.isSuccessful = false;
			result.message = "Get StoryStatus failed";
		}

		return result;
	}

	//modified by Shruti Sabusuresh
	//Return the relationship response according to sid and userid based on the user's accessibility
	public RelationshipsResponseModel getRelationships(final int userid, final int sid,
			final int perPage, final int pageNumber) {
		final RelationshipsResponseModel result = new RelationshipsResponseModel();
		// The following code is to get data from the table and store it into payload.
		try {
			final SourceInfoManagerImpl sourceInfoManagerImpl = new SourceInfoManagerImpl();
			final List<RelationshipsViewModel> contents = sourceInfoManagerImpl.getRelationshipsViewModel(sid, userid);

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
		catch(final Exception e) {
			logger.error(BasicTableBL.class.getName(),"Failed to get relationshipResponseModel: "+e.getMessage());
			result.isSuccessful=false;
			result.message = "Get MineRelationships failed";
		}
		return result;
	}

	public AttachmentListResponseModel getAttachmentList(final int sid){
		final AttachmentListResponseModel result = new AttachmentListResponseModel();
		try{
			final AttachmentManagerImpl attachmentManagerImpl = new AttachmentManagerImpl();
			final List<AttachmentListViewModel> contents = attachmentManagerImpl.getAttachmentListViewModel(sid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			result.isSuccessful=false;
			result.message = "Get AttachmentList failed";
		}
		return result;
	}

	public StoryListResponseModel getStoryList(final int pageNo, final int perPage){
		final StoryListResponseModel result = new StoryListResponseModel();
		try{
			final SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			final List<StoryListViewModel> contents = SourceInfoManagerImpl.getStoryListViewModel(pageNo, perPage);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}

	/**
	 * this method returns all the public stories, private stories accessible to the user if any, and all drafts of the user
	 * @param userid
	 * @return StoryListResponseModel
	 * @author modified by Shruti Sabusuresh
	 */
	public StoryListResponseModel getAllStoryList(final int userid){
		final StoryListResponseModel result = new StoryListResponseModel();
		try{
			final SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			//get all stories where user is author or queued or has access to private datasets
			final List<StoryListViewModel> contents = SourceInfoManagerImpl.getStoryListViewModel(userid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			logger.error(BasicTableBL.class.getName(),"Failed to get StoryListResponseModel: "+e.getMessage());
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}

	public StoryListResponseModel getStoryListBySid(final int sid){
		final StoryListResponseModel result = new StoryListResponseModel();
		try{
			final SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			final List<StoryListViewModel> contents = SourceInfoManagerImpl.getStoryListViewModelBySid(sid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}

	public DnameResponseModel getDnameListBySid(final int sid){
		final DnameResponseModel result = new DnameResponseModel();
		try{
			final DNameInfoManagerImpl dNameInfoManagerImpl = new DNameInfoManagerImpl();
			final List<DnameViewModel> contents = dNameInfoManagerImpl.getDnameListViewModel(sid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e){
			result.isSuccessful=false;
			result.message = "Get DnameList failed";
		}
		return result;
	}

	public DnameResponseModel getDnameListByCid(final int cid){
		final DnameResponseModel result = new DnameResponseModel();
		try{
			final DNameInfoManagerImpl dNameInfoManagerImpl = new DNameInfoManagerImpl();
			final List<DnameViewModel> contents = dNameInfoManagerImpl.getDnameListViewModelByCid(cid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e){
			result.isSuccessful=false;
			result.message = "Get DnameList failed";
		}
		return result;
	}

	/**
	 * Get all draft story list by userid as OWNER and CONTRIBUTOR
	 * @param userid as CONTRIBUTOR
	 * @return StoryListResponseModel
	 * @author Shruti Sabusuresh
	 */
	public StoryListResponseModel getAllDraftStoryList(final int userid){
		final StoryListResponseModel result = new StoryListResponseModel();
		try{
			final SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			final List<StoryListViewModel> contents = SourceInfoManagerImpl.getDraftStoryListViewModelByUser(userid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			logger.error(BasicTableBL.class.getName(),"Failed to get list of drafts: "+e.getMessage());
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}

	/**
	 * Get all QUEUED story list by userid as OWNER
	 * @param userid as OWNER
	 * @return StoryListResponseModel
	 * @author Shruti Sabusuresh
	 */
	public StoryListResponseModel getAllQueuedStoryListAuthoredByUser(final int userid) {
		final StoryListResponseModel result = new StoryListResponseModel();
		try{
			final SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			final List<StoryListViewModel> contents = SourceInfoManagerImpl.getStoryListViewModelByUser(userid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			logger.error(BasicTableBL.class.getName(),"Failed to get list of queued stories: "+e.getMessage());
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}

	/**
	 * Get all PRIVATE story list by userid as OWNER
	 * @param userid as OWNER
	 * @return StoryListResponseModel
	 * @author Shruti Sabusuresh
	 */
	public StoryListResponseModel getAllPrivateStoryListAuthoredByUser(final int userid) {
		final StoryListResponseModel result = new StoryListResponseModel();
		try{
			final SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			final List<StoryListViewModel> contents = SourceInfoManagerImpl.getPrivateStoryListViewModelByUser(userid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			logger.error(BasicTableBL.class.getName(),"Failed to get list of private stories: "+e.getMessage());
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}

	/**
	 * Get all story list by userid as OWNER
	 * @param userid as OWNER
	 * @return StoryListResponseModel
	 * @author Shruti Sabusuresh
	 */
	public StoryListResponseModel getAllStoryListAuthoredByUser(final int userid) {
		final StoryListResponseModel result = new StoryListResponseModel();
		try{
			final SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			final List<ColfusionSourceinfo> sourceInfoList  = SourceInfoManagerImpl.findByUserId(userid);
			final List<StoryListViewModel> contents = new ArrayList<StoryListViewModel>();
			for(final ColfusionSourceinfo sourceInfo: sourceInfoList){
				final StoryListViewModel storyModel = new StoryListViewModel();
				storyModel.setSid(sourceInfo.getSid());
				storyModel.setTitle(sourceInfo.getTitle());
				storyModel.setDescription(sourceInfo.getDescription());
				final UserViewModel user = new UserViewModel();
				user.setUserId(sourceInfo.getColfusionUsers().getUserId());
				user.setUserLogin(sourceInfo.getColfusionUsers().getUserLogin());
				storyModel.setUser(user);
				storyModel.setPath(sourceInfo.getPath());
				storyModel.setEntryDate(sourceInfo.getEntryDate());
				storyModel.setLastUpdated(sourceInfo.getLastUpdated());
				storyModel.setStatus(sourceInfo.getStatus());
				storyModel.setRawDataPath(sourceInfo.getRawDataPath());
				storyModel.setSourceType(sourceInfo.getSourceType());
				if(storyModel.getLicense() != null){
					final LicenseViewModel license = new LicenseViewModel();
					license.setLicenseId(storyModel.getLicense().getLicenseId());
					license.setLicenseName(storyModel.getLicense().getLicenseName());
					license.setLicenseDescription(storyModel.getLicense().getLicenseDescription());
					license.setLicenseURL(storyModel.getLicense().getLicenseURL());
					storyModel.setLicense(license);
				}
				contents.add(storyModel);
			}
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			logger.error(BasicTableBL.class.getName(),"Failed to get list of all owned stories of user "+userid+": "+e.getMessage());
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}

	/**
	 * Get all story list where userid is CONTRIBUTOR
	 * @param userid as CONTRIBUTOR
	 * @return StoryListResponseModel
	 * @author Shruti Sabusuresh
	 */
	public StoryListResponseModel getAllStoryListSharedToUser(final int userid) {
		final StoryListResponseModel result = new StoryListResponseModel();
		try{
			final SourceInfoManagerImpl SourceInfoManagerImpl = new SourceInfoManagerImpl();
			final List<StoryListViewModel> contents = SourceInfoManagerImpl.getAllStoryListViewModelSharedToUser(userid);
			result.setPayload(contents);
			result.isSuccessful=true;
		}
		catch(final Exception e) {
			logger.error(BasicTableBL.class.getName(),"Failed to get list of stories shared to user "+userid+": "+e.getMessage());
			result.isSuccessful=false;
			result.message = "Get StoryList failed";
		}
		return result;
	}
}
