/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.dal.dataModels.relationships.RelationshipMiningStatusEnum;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class RelationshipMiningViewModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4930433188796776912L;

	private RelationshipMiningStatusEnum miningStatus;
	
	private Date miningStartTime;
	
	private int processId;

	public RelationshipMiningViewModel() {
		
	}
	
	public RelationshipMiningViewModel(final RelationshipMiningStatusEnum miningStatus, final Date miningStartTime, final int processId) {
		this.miningStatus = miningStatus;
		this.miningStartTime = miningStartTime;
		this.processId = processId;
	}
	
	/**
	 * @return the miningStatus
	 */
	public RelationshipMiningStatusEnum getMiningStatus() {
		return miningStatus;
	}

	/**
	 * @param miningStatus the miningStatus to set
	 */
	public void setMiningStatus(final RelationshipMiningStatusEnum miningStatus) {
		this.miningStatus = miningStatus;
	}

	/**
	 * @return the miningStartTime
	 */
	public Date getMiningStartTime() {
		return miningStartTime;
	}

	/**
	 * @param miningStartTime the miningStartTime to set
	 */
	public void setMiningStartTime(final Date miningStartTime) {
		this.miningStartTime = miningStartTime;
	}

	/**
	 * @return the processId
	 */
	public int getProcessId() {
		return processId;
	}

	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(final int processId) {
		this.processId = processId;
	}
}
