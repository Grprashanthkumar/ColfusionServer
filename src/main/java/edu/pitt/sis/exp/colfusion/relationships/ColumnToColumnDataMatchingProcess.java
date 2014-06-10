/**
 * 
 */
package edu.pitt.sis.exp.colfusion.relationships;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Table;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerBase;
import edu.pitt.sis.exp.colfusion.persistence.databaseHandlers.DatabaseHandlerFactory;
import edu.pitt.sis.exp.colfusion.persistence.managers.ProcessPersistantManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.ProcessPersistantManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.managers.RelationshipsColumnsDataMathingRatiosManager;
import edu.pitt.sis.exp.colfusion.persistence.managers.RelationshipsColumnsDataMathingRatiosManagerImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionProcesses;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;
import edu.pitt.sis.exp.colfusion.process.ProcessBase;
import edu.pitt.sis.exp.colfusion.relationships.transformation.RelationshipTransformation;
import edu.pitt.sis.exp.colfusion.similarityJoins.NestedLoopSimilarityJoin;
import edu.pitt.sis.exp.colfusion.similarityMeasures.LevenshteinDistance;
import edu.pitt.sis.exp.colfusion.similarityMeasures.NormalizedDistance;

/**
 * @author Evgeny
 *
 */
public class ColumnToColumnDataMatchingProcess extends ProcessBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	transient static Logger logger = LogManager.getLogger(ColumnToColumnDataMatchingProcess.class.getName());
	
	@Expose private int relId;
	@Expose private String clFrom;
	@Expose private String clTo;
	@Expose private BigDecimal similarityThreshold;
	
	
	public ColumnToColumnDataMatchingProcess() {
	
	}
	
	//TODO: need to also accept Edit distance and an array of filters
	
	public ColumnToColumnDataMatchingProcess(final int relId, final String clFrom, final String clTo, final BigDecimal similarityThreshold) {
		this.relId = relId;
		this.clFrom = clFrom;
		this.clTo = clTo;
		this.similarityThreshold = similarityThreshold;
	}

	@Override
	protected Runnable getRunnable() {
		// TODO probabl can be just implemented in the ProcessBase class
		return this;
	}

	/**
	 * @return the clFrom
	 */
	public String getClFrom() {
		return clFrom;
	}

	/**
	 * @param clFrom the clFrom to set
	 */
	public void setClFrom(final String clFrom) {
		this.clFrom = clFrom;
	}

	/**
	 * @return the clTo
	 */
	public String getClTo() {
		return clTo;
	}

	/**
	 * @param clTo the clTo to set
	 */
	public void setClTo(final String clTo) {
		this.clTo = clTo;
	}

	/**
	 * @return the similarityThreshold
	 */
	public BigDecimal getSimilarityThreshold() {
		return similarityThreshold;
	}

	/**
	 * @param similarityThreshold the similarityThreshold to set
	 */
	public void setSimilarityThreshold(final BigDecimal similarityThreshold) {
		this.similarityThreshold = similarityThreshold;
	}

	public int getRelId() {
		return relId;
	}

	public void setRelId(final int relId) {
		this.relId = relId;
	}
	
	@Override
	public void execute() throws Exception {
		logger.info(String.format("Started execution of ColumnToColumnDataMatchingProcess process. RelId=%d, clFrom=%s, clTo=%s, similarityThreshold=%f", 
				relId, clFrom, clTo, similarityThreshold));
		
		RelationshipTransformation transformationFrom = new RelationshipTransformation(this.getClFrom());
		
		DatabaseHandlerBase dbHandlerFrom = DatabaseHandlerFactory.getDatabaseHandler(transformationFrom.getTargetDbConnectionInfo());
		
		RelationshipTransformation transformationTo = new RelationshipTransformation(this.getClTo());
		
		DatabaseHandlerBase dbHandlerTo = DatabaseHandlerFactory.getDatabaseHandler(transformationTo.getTargetDbConnectionInfo());
		
		Table allTuplesFrom = dbHandlerFrom.getAll(transformationFrom.getTableName(), transformationFrom.getColumnDbNames());
		
		Table allTuplesTo = dbHandlerTo.getAll(transformationTo.getTableName(), transformationTo.getColumnDbNames());
		
		NestedLoopSimilarityJoin simJoin = new NestedLoopSimilarityJoin(new NormalizedDistance(new LevenshteinDistance()), null, null);
		
		Table joinResult = simJoin.join(allTuplesFrom, allTuplesTo, 
				transformationFrom, transformationTo, similarityThreshold.doubleValue());
		
		double dataMathingRatioFrom = (double) joinResult.size() / allTuplesFrom.size();
		double dataMathingRatioTo = (double) joinResult.size() / allTuplesTo.size();
		
		similarityThreshold = this.similarityThreshold.setScale(3, RoundingMode.HALF_UP);
		
		ColfusionRelationshipsColumnsDataMathingRatiosId dataMathingRatioId = new ColfusionRelationshipsColumnsDataMathingRatiosId(this.getClFrom(),
				this.getClTo(), this.similarityThreshold);
		
		ProcessPersistantManager processPerMgn = new ProcessPersistantManagerImpl();
		ColfusionProcesses process = processPerMgn.findByID(this.getID());
		
		ColfusionRelationshipsColumnsDataMathingRatios dataMathingRatio = new ColfusionRelationshipsColumnsDataMathingRatios();
		dataMathingRatio.setId(dataMathingRatioId);
		dataMathingRatio.setColfusionProcesses(process);
		dataMathingRatio.setDataMatchingFromRatio(new BigDecimal(dataMathingRatioFrom));
		dataMathingRatio.setDataMatchingToRatio(new BigDecimal(dataMathingRatioTo));
		
		RelationshipsColumnsDataMathingRatiosManager dataMatchingRatioMng = new RelationshipsColumnsDataMathingRatiosManagerImpl();
		dataMatchingRatioMng.saveOrUpdate(dataMathingRatio);
		
		
		logger.info(String.format("Finished execution of ColumnToColumnDataMatchingProcess process. RelId=%d, clFrom=%s, clTo=%s, similarityThreshold=%f", 
				relId, clFrom, clTo, similarityThreshold));
	}

}