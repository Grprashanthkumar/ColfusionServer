package edu.pitt.sis.exp.colfusion.psc.server.business;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHanderType;
import edu.pitt.sis.exp.colfusion.dal.managers.PSCSourceInfoTableManager;
import edu.pitt.sis.exp.colfusion.dal.managers.PSCSourceInfoTableManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.PSCSourceInfoTableManagerImpl.SourceInfoAndTable;
import edu.pitt.sis.exp.colfusion.dal.managers.ProcessPersistantManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ProcessPersistantManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionProcesses;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionPscSourceinfoTable;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionPscSourceinfoTableId;
import edu.pitt.sis.exp.colfusion.process.ProcessManager;
import edu.pitt.sis.exp.colfusion.utils.StringUtils;


public class DataReplicationBL {
	
	transient static Logger logger = LogManager.getLogger(DataReplicationBL.class.getName());
	
	//TODO: move it to properties
	private final static String PSC_HOST = "tioga.psc.edu";
	private final static int PSC_DATABASE_PORT = 3306;
	private final static String PSC_DATABASE_NAME = "colfusion";
	private final static String PSC_DATABASE_USER = "evgeny";
	private final static String PSC_DATABASE_PASSWORD = "^&BdZYI[e]UB";
	private final static DatabaseHanderType PSC_DATABASE_VENDOR = DatabaseHanderType.MYSQL;
	
	public int doDataReplication() throws Exception {
		PSCSourceInfoTableManager pscSourceInfoTableMng = new PSCSourceInfoTableManagerImpl();
		List<SourceInfoAndTable> notReplicatedStories = pscSourceInfoTableMng.findAllNotReplicated();
		
		ProcessPersistantManager processMng = new ProcessPersistantManagerImpl();
		
		int count = 0;
		for (SourceInfoAndTable sourceInfoAndTable : notReplicatedStories) {
			
			try {
				ColfusionPscSourceinfoTable pscSourceInfoTable = createAndSavePSCSourceIntoTable(
						pscSourceInfoTableMng, sourceInfoAndTable);
			
			
				ColfusionProcesses colfusionProcess = queueReplicationProcess(processMng, sourceInfoAndTable);
				
				if (colfusionProcess != null) {
					pscSourceInfoTable.setColfusionProcesses(colfusionProcess);
					
					pscSourceInfoTableMng.saveOrUpdate(pscSourceInfoTable);
				}
			} catch (Exception e) {
				logger.error(String.format("FAILED to trigger replication for  "
						+ "SourceInfo id: %d; Table name '%s'.", 
						sourceInfoAndTable.getSourceInfoId(), sourceInfoAndTable.getTableName()), e);
				
				throw e;
			}		
			
			count++;
		}
		
		return count;
	}

	/**
	 * @param processMng
	 * @param sourceInfoAndTable
	 * @return
	 * @throws Exception
	 */
	private ColfusionProcesses queueReplicationProcess(
			final ProcessPersistantManager processMng,
			final SourceInfoAndTable sourceInfoAndTable) throws Exception {
		ReplicationProcess process = new ReplicationProcess(sourceInfoAndTable.getSourceInfoId(), sourceInfoAndTable.getTableName());
		
		int processId = -1;
		
		try {
			processId = ProcessManager.getInstance().queueProcess(process);
		} catch (Exception e) {
			logger.error(String.format("FAILED to add a replication process to the queue. "
					+ "SourceInfo id: %d; Table name '%s'.", 
					sourceInfoAndTable.getSourceInfoId(), sourceInfoAndTable.getTableName()), e);
			
			throw e;
		}
							
		ColfusionProcesses colfusionProcess = processMng.findByID(processId);
		return colfusionProcess;
	}

	/**
	 * @param pscSourceInfoTableMng
	 * @param sourceInfoAndTable
	 * @return
	 * @throws Exception
	 */
	private ColfusionPscSourceinfoTable createAndSavePSCSourceIntoTable(
			final PSCSourceInfoTableManager pscSourceInfoTableMng,
			final SourceInfoAndTable sourceInfoAndTable) throws Exception {
		SourceInfoManager sourceInfoMng = new SourceInfoManagerImpl();
		String pscTableName = generateUniquePSCTableName(sourceInfoAndTable.getSourceInfoId(), sourceInfoAndTable.getTableName());
		
		ColfusionPscSourceinfoTableId id = new ColfusionPscSourceinfoTableId(sourceInfoAndTable.getSourceInfoId(), sourceInfoAndTable.getTableName());
		ColfusionPscSourceinfoTable pscSourceInfoTable = new ColfusionPscSourceinfoTable(id, 
				sourceInfoMng.findByID(sourceInfoAndTable.getSourceInfoId()), PSC_DATABASE_NAME, 
				pscTableName, PSC_HOST, PSC_DATABASE_PORT, PSC_DATABASE_USER, PSC_DATABASE_PASSWORD, PSC_DATABASE_VENDOR.getValue()); 
		
		pscSourceInfoTableMng.saveOrUpdate(pscSourceInfoTable);
		return pscSourceInfoTable;
	}

	private String generateUniquePSCTableName(final int sourceInfoId, final String tableName) {
		return String.format("%d_%s", sourceInfoId, StringUtils.makeShortUnique(tableName)); //TODO: sourceInfoId takes several chars, but the table name lengths is 64, so need to take sid into consideration
	}
}
