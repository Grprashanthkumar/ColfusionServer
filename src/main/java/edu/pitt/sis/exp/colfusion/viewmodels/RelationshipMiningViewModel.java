/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.relationships.RelationshipMiningStatusEnum;

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
	
	public RelationshipMiningViewModel(RelationshipMiningStatusEnum miningStatus, Date miningStartTime, int processId) {
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
	public void setMiningStatus(RelationshipMiningStatusEnum miningStatus) {
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
	public void setMiningStartTime(Date miningStartTime) {
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
	public void setProcessId(int processId) {
		this.processId = processId;
	}
}
