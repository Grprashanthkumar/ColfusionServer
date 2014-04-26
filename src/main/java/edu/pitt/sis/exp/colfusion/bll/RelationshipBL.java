/**
 * 
 */
package edu.pitt.sis.exp.colfusion.bll;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponse;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGen;
import edu.pitt.sis.exp.colfusion.responseModels.GeneralResponseGenImpl;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataResponse;
import edu.pitt.sis.exp.colfusion.viewmodels.RelationshipMiningViewModel;

/**
 * @author Evgeny
 *
 * Logic to deal with relationships.
 */
public class RelationshipBL {
	
	final Logger logger = LogManager.getLogger(RelationshipBL.class.getName());

	public GeneralResponse mineRelationshipsFor(int sid) {
	
		GeneralResponseGen<RelationshipMiningViewModel> result = new GeneralResponseGenImpl<RelationshipMiningViewModel>();
	
		//result.setPayload("Started");
		
		return result;
	}
}
