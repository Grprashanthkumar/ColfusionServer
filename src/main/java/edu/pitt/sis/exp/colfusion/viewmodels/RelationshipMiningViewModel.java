/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.relationships.RelationshipMiningStatusEnum;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class RelationshipMiningViewModel {
	
	private RelationshipMiningStatusEnum miningStatus;
	
	private Date miningStartTime;
	
	private int processId;
}
