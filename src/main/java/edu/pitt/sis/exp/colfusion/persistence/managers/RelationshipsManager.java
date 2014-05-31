/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;

/**
 * @author Evgeny
 *
 */
public interface RelationshipsManager extends GeneralManager<ColfusionRelationships, Integer> {

	//TODO:should it be here or just use ColfusionStory which should have reference to relationships
	/**
	 * Find relationships by given sid. The found relationships are those that have a story with given id at either end.
	 * @param sid is id of the story for which to find relationships.
	 * @return all relationships of given story.
	 */
	//List<ColfusionRelationships> findRelationshipsBySid(int sid);
	
	ColfusionRelationshipsColumnsDataMathingRatios findColfusionRelationshipsColumnsDataMathingRatios(ColfusionRelationshipsColumnsDataMathingRatiosId colfusionRelationshipsColumnsDataMathingRatiosId);
}
