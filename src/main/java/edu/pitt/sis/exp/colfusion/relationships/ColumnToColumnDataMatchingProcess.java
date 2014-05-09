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
	
	private String clFrom;
	private String clTo;
	private BigDecimal similarityThreshold;
	
	
	public ColumnToColumnDataMatchingProcess() {
	
	}
	
	public ColumnToColumnDataMatchingProcess(String clFrom, String clTo, BigDecimal similarityThreshold) {
		this.clFrom = clFrom;
		this.clTo = clTo;
		this.similarityThreshold = similarityThreshold;
	}

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.process.ProcessBase#getRunnable()
	 */
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
	public void setClFrom(String clFrom) {
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
	public void setClTo(String clTo) {
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
	public void setSimilarityThreshold(BigDecimal similarityThreshold) {
		this.similarityThreshold = similarityThreshold;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
