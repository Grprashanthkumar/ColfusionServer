/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.bll.process.ProcessManager;
import edu.pitt.sis.exp.colfusion.bll.relationships.ColumnToColumnDataMatchingProcess;
import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponse;
import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.bll.responseModels.GeneralResponseGenImpl;
import edu.pitt.sis.exp.colfusion.bll.responseModels.RelationshipLinksResponse;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.transformation.RelationshipTransofmationUtil;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.dal.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.dal.managers.GeneralManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.ProcessPersistantManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ProcessPersistantManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsColumnsDataMathingRatiosManager;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsColumnsDataMathingRatiosManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsManager;
import edu.pitt.sis.exp.colfusion.dal.managers.RelationshipsManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionProcesses;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumns;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipLinkViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipMiningViewModel;

/**
 * @author Evgeny
 *
 * Logic to deal with relationships.
 */
public class RelationshipBL {
	
	final Logger logger = LogManager.getLogger(RelationshipBL.class.getName());

	private static final RelationshipsManager relationshipsManager =  new RelationshipsManagerImpl();
	
	public GeneralResponse mineRelationshipsFor(final int sid) {
	
		GeneralResponseGen<RelationshipMiningViewModel> result = new GeneralResponseGenImpl<RelationshipMiningViewModel>();
	
		//result.setPayload("Started");
		
		return result;
	}

	/**
	 * Triggers background process for data matching calculations for all relationships of a given story that were not yet and are
	 * not being calculated.
	 * 
	 * @param sid is the id of the story.
	 * @param similarityThreshold 
	 * @return simple message if processes has been started.
	 * @throws Exception 
	 */
	public GeneralResponse triggerDataMatchingRatiosCalculationsForAllNotCalculatedBySid(final int sid, final BigDecimal similarityThreshold) throws Exception {
		
		SourceInfoManager storyMng = new SourceInfoManagerImpl();
		
		ColfusionSourceinfo story = null;
		
		try {
			story = storyMng.findByID(sid);			
		} catch (Exception e) {
			String msg = String.format("triggerDataMatchingRatiosCalculationsForAllNotCalculated FAILED to find story by sid %d", sid);
			
			logger.error(msg, e);
			
			throw e;
		}
		
		if (story == null) {
			String msg = String.format("triggerDataMatchingRatiosCalculationsForAllNotCalculated found a story but it is null for sid %d", sid);
			
			logger.error(msg);
			
			throw new NullPointerException(msg);
		}
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
		story = GeneralManagerImpl.initializeField(story, "colfusionRelationshipsesForSid1");
		
		if (story.getColfusionRelationshipsesForSid1().size() != 0 ){
			triggerDataMatchingRatiosCalculationsForAllNotCalculatedByRelationshipsSet(
					sid, story.getColfusionRelationshipsesForSid1(), similarityThreshold);
		}
		
		story = GeneralManagerImpl.initializeField(story, "colfusionRelationshipsesForSid2");
		
		if (story.getColfusionRelationshipsesForSid2().size() != 0 ){
			triggerDataMatchingRatiosCalculationsForAllNotCalculatedByRelationshipsSet(
					sid, story.getColfusionRelationshipsesForSid2(), similarityThreshold);
		}
		
		result.setMessage("OK");
		result.setPayload("Started" + sid);
		result.setSuccessful(true);
	
		return result;
	}

	/**
	 * @param sid
	 * @param similarityThreshold 
	 * @param relsForSid1
	 * @throws Exception 
	 */
	private void triggerDataMatchingRatiosCalculationsForAllNotCalculatedByRelationshipsSet(
			final int sid, final Set<?> relsForSid, final BigDecimal similarityThreshold) throws Exception {
		for (Object object : relsForSid) {
			if (object instanceof ColfusionRelationships) {
				ColfusionRelationships rel = (ColfusionRelationships) object;
				
				rel = GeneralManagerImpl.initializeField(rel, "colfusionRelationshipsColumnses");
				
				triggerDataMatchingRatiosCalculationsByRel(rel, similarityThreshold);
			}
			else {
				
				logger.error("triggerDataMatchingRatiosCalculationsForAllNotCalculatedBySid: was supposed to "
						+ "iterate over set of ColfusionRelationship but could not cast object to ColfusionRelationship. Sid = " + sid);
				
				break;
			}
			
		}
	}

	private void triggerDataMatchingRatiosCalculationsByRel(
			final ColfusionRelationships rel, final BigDecimal similarityThreshold) throws Exception {
		
		for (Object object : rel.getColfusionRelationshipsColumnses()) {
			if (object instanceof ColfusionRelationshipsColumns) {
				ColfusionRelationshipsColumns relationshipColumns = (ColfusionRelationshipsColumns) object;
				
				RelationshipTransformation transformationFrom = RelationshipTransofmationUtil.makeRelationshipTransformation(rel.getRelId(), relationshipColumns.getId().getClFrom()); 
				RelationshipTransformation transformationTo = RelationshipTransofmationUtil.makeRelationshipTransformation(rel.getRelId(), relationshipColumns.getId().getClTo());
				
				//TODO: Once we add ability to chain processes or make some processes depend on other processes
				//TODO: indices should be also created in a background process.
				
				SourceInfoManager sourceMng = new SourceInfoManagerImpl();
				
				ColfusionSourceinfoDb srouceInfoDBFrom = sourceMng.getColfusionSourceinfoDbFrom(rel.getRelId());
				ColfusionSourceinfoDb srouceInfoDBTo = sourceMng.getColfusionSourceinfoDbTo(rel.getRelId());
				
				createIndeces(srouceInfoDBFrom, transformationFrom);
				createIndeces(srouceInfoDBTo, transformationTo);
				
				for (double i = 0; i < 1.1; i+= 0.1) {
					triggerDataMatchingRatiosCalculationsByRelationshipsColumns(relationshipColumns, new BigDecimal(new DecimalFormat("#.###").format(i)));
				}
			}
			else {
				
				logger.error("triggerDataMatchingRatiosCalculationsForAllNotCalculatedByRel: was supposed to "
						+ "iterate over set of ColfusionRelationshipsColumns but could not cast object to ColfusionRelationshipsColumns. RelId = " + rel.getRelId());
				
				break;
			}
		}
	}

		/**
	 * Creates indexed in the target database on the columns provided by their ids (cid).
	 * @param cidFromOneStory a list of cids. Note all cids should be from one story only (from one target database/table).
	 * @throws Exception 
	 */
	private void createIndeces(final ColfusionSourceinfoDb targetDBInfo, final RelationshipTransformation transformation) throws Exception {
		
		try (DatabaseHandlerBase dbHandler = DatabaseHandlerFactory.getDatabaseHandler(targetDBInfo)) {
		
			//TODO FIXME: should indeces be created on all columns at once?
			for (String columnDbName : transformation.getColumnDbNames()) {
				try {
					dbHandler.createIndecesIfNotExist(transformation.getRelationKey(), columnDbName);
				} catch (Exception e) {
					logger.error("createIndeces FAILED to initialize colfusionSourceinfoDb field.");
					throw e;
				}
			}
		}
	}

	/**
	 * Adds a {@link ColumnToColumnDataMatchingProcess} process to process queue for given relationships columns {@link ColfusionRelationshipsColumns} and 
	 * a similarity threshold, but only if data matching was not calculated already.
	 * 
	 * @param relationshipColumns columns for which to calculate data matching values.
	 * @param similarityThreshold at which similarity threshold to calculate data matching.
	 * @throws Exception 
	 */
	private void triggerDataMatchingRatiosCalculationsByRelationshipsColumns(
			final ColfusionRelationshipsColumns relationshipColumns, final BigDecimal similarityThreshold) throws Exception {
		
		RelationshipsColumnsDataMathingRatiosManager dataMatingRatiosMng = new RelationshipsColumnsDataMathingRatiosManagerImpl();
		
		// Check if data matching for given columns was already calculated or not but searching for a record in colfusion_relationships_columns_dataMathing_ratios table
		ColfusionRelationshipsColumnsDataMathingRatiosId columnsDataMathingRatiosId = new ColfusionRelationshipsColumnsDataMathingRatiosId(relationshipColumns.getId().getClFrom(), 
				relationshipColumns.getId().getClTo(), similarityThreshold);
		
		ColfusionRelationshipsColumnsDataMathingRatios colfusionRelationshipsColumnsDataMathingRatios = 
				dataMatingRatiosMng.findColfusionRelationshipsColumnsDataMathingRatios(columnsDataMathingRatiosId);
		// if a record found in colfusion_relationships_columns_dataMathing_ratios for given columns and sim threshold, then data matchng values are known.
		if (colfusionRelationshipsColumnsDataMathingRatios != null) {
			logger.info(String.format("triggerDataMatchingRatiosCalculationsByRelationshipsColumns: don't need to trigger calculations because a record "
					+ "about data matching for givel columns (from: %s and to: %s) and similarity threshold (%f) was found in db.", 
					relationshipColumns.getId().getClFrom(), relationshipColumns.getId().getClTo(), similarityThreshold));
			
			
			//TODO: we might check the status of the associated process and if it is a failure, then we can probably restart the calculations.
		}
		else {

			ColumnToColumnDataMatchingProcess process = new ColumnToColumnDataMatchingProcess(relationshipColumns.getId().getRelId(), 
					relationshipColumns.getId().getClFrom(), relationshipColumns.getId().getClTo(), similarityThreshold);
			
			int processId = -1;
			
			try {
				processId = ProcessManager.getInstance().queueProcess(process);
			} catch (Exception e) {
				logger.error(String.format("triggerDataMatchingRatiosCalculationsByRelationshipsColumns: FAILED to add a process "
						+ "to calculate data matching for givel columns (from: %s and to: %s) and similarity threshold (%f) was found in db.", 
					relationshipColumns.getId().getClFrom(), relationshipColumns.getId().getClTo(), similarityThreshold), e);
				
				throw e;
			}
			
			ProcessPersistantManager processMng = new ProcessPersistantManagerImpl();
			ColfusionProcesses colfusionProcess = processMng.findByID(processId);
			
			if (colfusionProcess != null) {
				ColfusionRelationshipsColumnsDataMathingRatios dataMathingRatios = new 
						ColfusionRelationshipsColumnsDataMathingRatios(columnsDataMathingRatiosId, colfusionProcess);
				
				dataMatingRatiosMng.saveOrUpdate(dataMathingRatios);
			}
			
		}		
	}

	public RelationshipLinksResponse getDataMatchingRatios(final int relId, final BigDecimal similarityThreshold) throws Exception {
		
		RelationshipLinksResponse result = new RelationshipLinksResponse();
		
		RelationshipsManager relMng = new RelationshipsManagerImpl();
		
		List<RelationshipLinkViewModel> payload = relMng.getRelationshipLinks(relId, similarityThreshold);
		
		result.isSuccessful = true;
		result.message = "OK";
		result.setPayload(payload);
		
		return result;
	}
	
	
	/**
	 * Do the Relationship Mining procedure.
	 * @param sid
	 * @throws Exception 
	 */
	public void doRelationshipMining(final int sid){
		try{
			SourceInfoManager sourceInfoManagerImpl = new SourceInfoManagerImpl();
			sourceInfoManagerImpl.doRelationshipMining(sid);
		}
		catch(Exception e) {
			logger.error("failed to doRelationshipMining");
		}
	}
}
