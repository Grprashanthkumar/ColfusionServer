/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.Table;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class JoinTablesByRelationshipsViewModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Expose private List<Integer> relationshipIds;
	
	@Expose private double similarityThreshold;
	
	@Expose private Table jointTable;
	
	@Expose private int perPage;
	
	@Expose private int pageNo;
	
	@Expose private int totalPage;
	
	public JoinTablesByRelationshipsViewModel() {
		
	}
	
	public JoinTablesByRelationshipsViewModel(final List<Integer> relationshipIds, 
			final double similarityThreshold, final Table jointTable,
			final int perPage, final int pageNo, final int totalPage) {
		this.relationshipIds = relationshipIds;
		this.similarityThreshold = similarityThreshold;
		this.setJointTable(jointTable);
		this.setPerPage(perPage);
		this.setPageNo(pageNo);
		this.setTotalPage(totalPage);
		
	}
	
	/**
	 * @return the jointTable
	 */
	public List<Integer> getRelationshipIds() {
		return relationshipIds;
	}

	/**
	 * @param jointTable the jointTable to set
	 */
	public void setRelationshipIds(final List<Integer> relationshipIds) {
		this.relationshipIds = relationshipIds;
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

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(final int perPage) {
		this.perPage = perPage;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(final int totalPage) {
		this.totalPage = totalPage;
	}
}
