/**
 * 
 */
package edu.pitt.sis.exp.colfusion.relationships;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.process.ProcessBase;

/**
 * @author Evgeny
 *
 */
public class ColumnToColumnDataMatchingProcess extends ProcessBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	transient Logger logger = LogManager.getLogger(ColumnToColumnDataMatchingProcess.class.getName());
	
	private int relId;
	private String clFrom;
	private String clTo;
	private BigDecimal similarityThreshold;
	
	
	public ColumnToColumnDataMatchingProcess() {
	
	}
	
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
		
	}

}
