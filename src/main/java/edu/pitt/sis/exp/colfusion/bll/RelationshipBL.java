/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.DNameInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.RelationshipsManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.RelationshipsManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.SourceInfoManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumns;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.process.ProcessManager;
import edu.pitt.sis.exp.colfusion.relationships.ColumnToColumnDataMatchingProcess;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;
import edu.pitt.sis.exp.colfusion.viewmodels.RelationshipMiningViewModel;

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
		
		story = GeneralManagerImpl.initializeField(story, "colfusionRelationshipsesForSid1");
		story = GeneralManagerImpl.initializeField(story, "colfusionRelationshipsesForSid2");
		
		triggerDataMatchingRatiosCalculationsForAllNotCalculatedByRelationshipsSet(
				sid, story.getColfusionRelationshipsesForSid1(), similarityThreshold);
		
		triggerDataMatchingRatiosCalculationsForAllNotCalculatedByRelationshipsSet(
				sid, story.getColfusionRelationshipsesForSid2(), similarityThreshold);
		
		GeneralResponseGen<String> result = new GeneralResponseGenImpl<>();
		
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
				
				createIndeces(relationshipColumns);
				
				triggerDataMatchingRatiosCalculationsByRelationshipsColumns(relationshipColumns, similarityThreshold);
			}
			else {
				
				logger.error("triggerDataMatchingRatiosCalculationsForAllNotCalculatedByRel: was supposed to "
						+ "iterate over set of ColfusionRelationshipsColumns but could not cast object to ColfusionRelationshipsColumns. RelId = " + rel.getRelId());
				
				break;
			}
		}
	}

	private void createIndeces(final ColfusionRelationshipsColumns relationshipColumns) throws Exception {
		List<Integer> cidFromOneStory = extractCidsFromTransformation(relationshipColumns.getId().getClFrom());
		
		createIndeces(cidFromOneStory);
		
		cidFromOneStory = extractCidsFromTransformation(relationshipColumns.getId().getClTo());
		
		createIndeces(cidFromOneStory);
	}

	/**
	 * Creates indexed in the target database on the columns provided by their ids (cid).
	 * @param cidFromOneStory a list of cids. Note all cids should be from one story only (from one target database/table).
	 * @throws Exception 
	 */
	private void createIndeces(final List<Integer> cidFromOneStory) throws Exception {
		if (cidFromOneStory.size() > 0) {
			
			SourceInfoManager sourceInfoMng = new SourceInfoManagerImpl();
			ColfusionSourceinfo story = sourceInfoMng.findStoryByCid(cidFromOneStory.get(0));
			
			try {
				story = GeneralManagerImpl.initializeField(story, "colfusionSourceinfoDb");
			} catch (NoSuchFieldException e) {
				logger.error("createIndeces FAILED to initialize colfusionSourceinfoDb field. Seems that field was not found");
				throw e;
			} catch (IllegalAccessException e) {
				logger.error("createIndeces FAILED to initialize colfusionSourceinfoDb field.");
				throw e;
			}
			
			DNameInfoManager columnManager = new DNameInfoManagerImpl();
			
			List<String> columnNames = new ArrayList<String>();
			
			for (Integer cid : cidFromOneStory) {
				ColfusionDnameinfo column = null;
				try {
					column = columnManager.findByID(cid);
				} catch (Exception e) {
					String msg = String.format("createIndeces FAILED when searching for columns by cids on cid=%d", cid);
					logger.error(msg);
					
					//TODO:maybe need custom error?
					throw new Exception(msg);
				}
				
				if (column == null) {
					String msg = String.format("createIndeces FAILED when searching for columns by cids on cid=%d", cid);
					logger.error(msg);
					
					//TODO:maybe need custom error?
					throw new Exception(msg);
				}
					
				columnNames.add(column.getDnameOriginalName());
			}
			
			try {
				DatabaseHandlerBase dbHandler = DatabaseHandlerFactory.getDatabaseHandler(story.getColfusionSourceinfoDb());
				dbHandler.createIndeces(columnNames, false);
			} catch (Exception e) {
				logger.error("createIndeces FAILED to initialize colfusionSourceinfoDb field.");
				throw e;
			}
		}
	}

	/**
	 * Extracts column ids from column transformation strings (colum id are supposed to be wrapped in cid(...));
	 * @param transformation the transformation string.
	 * @return list of ids extracted from the transformation string.
	 */
	public List<Integer> extractCidsFromTransformation(final String transformation) throws Exception {
		Pattern pattern = Pattern.compile("(\\()(\\d+)(\\))");
				
		Matcher matcher = pattern.matcher(transformation);
		
		List<Integer> result = new ArrayList<Integer>();
		
		try {
		
			while (matcher.find()) {
				result.add(Integer.parseInt(matcher.group(2)));
			}
		}
		catch(Exception e) {
			logger.error(String.format("Could not extract cids from this string: %s", transformation));
			throw e;
		}
		
		return result;		
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
		
		// Check if data matching for given columns was already calculated or not but searching for a record in colfusion_relationships_columns_dataMathing_ratios table
		ColfusionRelationshipsColumnsDataMathingRatiosId columnsDataMathingRatiosId = new ColfusionRelationshipsColumnsDataMathingRatiosId(relationshipColumns.getId().getClFrom(), 
				relationshipColumns.getId().getClTo(), similarityThreshold);
		
		ColfusionRelationshipsColumnsDataMathingRatios colfusionRelationshipsColumnsDataMathingRatios = 
				relationshipsManager.findColfusionRelationshipsColumnsDataMathingRatios(columnsDataMathingRatiosId);
		// if a record found in colfusion_relationships_columns_dataMathing_ratios for given columns and sim threshold, then data matchng values are known.
		if (colfusionRelationshipsColumnsDataMathingRatios != null) {
			logger.info(String.format("triggerDataMatchingRatiosCalculationsByRelationshipsColumns: don't need to trigger calculations because a record "
					+ "about data matching for givel columns (from: %s and to: %s) and similarity threshold (%f) was found in db.", 
					relationshipColumns.getId().getClFrom(), relationshipColumns.getId().getClTo(), similarityThreshold));
		}
		else {

			ColumnToColumnDataMatchingProcess process = new ColumnToColumnDataMatchingProcess(relationshipColumns.getId().getRelId(), 
					relationshipColumns.getId().getClFrom(), relationshipColumns.getId().getClTo(), similarityThreshold);
			
			try {
				ProcessManager.getInstance().queueProcess(process);
			} catch (Exception e) {
				logger.error(String.format("triggerDataMatchingRatiosCalculationsByRelationshipsColumns: FAILED to add a process "
						+ "to calculate data matching for givel columns (from: %s and to: %s) and similarity threshold (%f) was found in db.", 
					relationshipColumns.getId().getClFrom(), relationshipColumns.getId().getClTo(), similarityThreshold), e);
				
				throw e;
			}
		}		
	}
}
