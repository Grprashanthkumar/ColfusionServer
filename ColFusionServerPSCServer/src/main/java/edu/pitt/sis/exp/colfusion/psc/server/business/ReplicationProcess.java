package edu.pitt.sis.exp.colfusion.psc.server.business;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHanderType;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.managers.PSCSourceInfoTableManager;
import edu.pitt.sis.exp.colfusion.dal.managers.PSCSourceInfoTableManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionPscSourceinfoTable;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionPscSourceinfoTableId;
import edu.pitt.sis.exp.colfusion.process.ProcessBase;

public class ReplicationProcess extends ProcessBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(ReplicationProcess.class.getName());
	
	@Expose int sourceInfoId;
	@Expose String tableName;
	
	public ReplicationProcess(final int sourceInfoId, final String tableName) {
		this.sourceInfoId = sourceInfoId;
		this.tableName = tableName;
	}

	@Override
	public void execute() throws Exception {
		logger.info(String.format("Starting to process story sid %d and table name '%s'", sourceInfoId, tableName));
		
		PSCSourceInfoTableManager pscManager = new PSCSourceInfoTableManagerImpl();
		ColfusionPscSourceinfoTable pscSourceinfoTable = pscManager.findByID(new ColfusionPscSourceinfoTableId(sourceInfoId, tableName));
		pscSourceinfoTable.setWhenReplicationStarted(new Date());
		pscManager.saveOrUpdate(pscSourceinfoTable);
		
		DatabaseHandlerBase databaseFrom = DatabaseHandlerFactory.getDatabaseHandler(pscSourceinfoTable.getColfusionSourceinfo().getColfusionSourceinfoDb());
		
		DatabaseHandlerBase pscDatabase = DatabaseHandlerFactory.getDatabaseHandler(pscSourceinfoTable.getColfusionSourceinfo().getSid(), 
				pscSourceinfoTable.getPscHost(), pscSourceinfoTable.getPscDatabasePort(), 
				pscSourceinfoTable.getPscDatabaseUser(), pscSourceinfoTable.getPscDatabasePassword(), 
				pscSourceinfoTable.getPscDatabaseName(), DatabaseHanderType.fromString(pscSourceinfoTable.getPscDatabaseVendor()), null, -1);
		
		DatabaseHandlerBase.copyData(databaseFrom, new RelationKey(pscSourceinfoTable.getId().getTableName(), pscSourceinfoTable.getId().getTableName()),
				pscDatabase, new RelationKey(pscSourceinfoTable.getPscTableName(), pscSourceinfoTable.getPscTableName()));
		
		databaseFrom.close();
		pscDatabase.close();
		
		pscSourceinfoTable.setWhenReplicationFinished(new Date());
		pscManager.saveOrUpdate(pscSourceinfoTable);
		
		logger.info(String.format("Finished to process story sid %d and table name '%s'", sourceInfoId, tableName));
	}

	@Override
	protected Runnable getRunnable() {
		return this;
	}

}
