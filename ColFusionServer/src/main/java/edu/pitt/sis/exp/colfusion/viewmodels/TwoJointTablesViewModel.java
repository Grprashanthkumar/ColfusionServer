/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dataModels.tableDataModel.Table;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class TwoJointTablesViewModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose private int sid1;
	@Expose private String tableName1;
	@Expose private int sid2;
	@Expose private String tableName2;
	
	@Expose private double similarityThreshold;
	
	@Expose private Table jointTable;
	
	public TwoJointTablesViewModel() {
		
	}
	
	public TwoJointTablesViewModel(final int sid1, final String tableName1, final int sid2, final String tableName2,
			final double similarityThreshold, final Table jointTable) {
		this.sid1 = sid1;
		this.sid2 = sid2;
		this.tableName1 = tableName1;
		this.tableName2 = tableName2;
		this.similarityThreshold = similarityThreshold;
		this.setJointTable(jointTable);
	}
	
	/**
	 * @return the sid1
	 */
	public int getSid1() {
		return sid1;
	}
	/**
	 * @param sid1 the sid1 to set
	 */
	public void setSid1(final int sid1) {
		this.sid1 = sid1;
	}
	/**
	 * @return the tableName1
	 */
	public String getTableName1() {
		return tableName1;
	}
	/**
	 * @param tableName1 the tableName1 to set
	 */
	public void setTableName1(final String tableName1) {
		this.tableName1 = tableName1;
	}
	/**
	 * @return the sid2
	 */
	public int getSid2() {
		return sid2;
	}
	/**
	 * @param sid2 the sid2 to set
	 */
	public void setSid2(final int sid2) {
		this.sid2 = sid2;
	}
	/**
	 * @return the tableName2
	 */
	public String getTableName2() {
		return tableName2;
	}
	/**
	 * @param tableName2 the tableName2 to set
	 */
	public void setTableName2(final String tableName2) {
		this.tableName2 = tableName2;
	}

	/**
	 * @return the jointTable
	 */
	public Table getJointTable() {
		return jointTable;
	}

	/**
	 * @param jointTable the jointTable to set
	 */
	public void setJointTable(final Table jointTable) {
		this.jointTable = jointTable;
	}

	/**
	 * @return the similarityThreshold
	 */
	public double getSimilarityThreshold() {
		return similarityThreshold;
	}

	/**
	 * @param similarityThreshold the similarityThreshold to set
	 */
	public void setSimilarityThreshold(final double similarityThreshold) {
		this.similarityThreshold = similarityThreshold;
	}
}
