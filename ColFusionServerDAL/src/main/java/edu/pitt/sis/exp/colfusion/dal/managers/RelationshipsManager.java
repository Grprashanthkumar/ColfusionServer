/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import java.math.BigDecimal;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.viewmodels.RelationshipLinkViewModel;

/**
 * @author Evgeny
 *
 */
public interface RelationshipsManager extends GeneralManager<ColfusionRelationships, Integer> {

	List<ColfusionRelationships> findByTables(int sid1, String tableName1,
			int sid2, String tableName2) throws Exception;

	//TODO:should it be here or just use ColfusionStory which should have reference to relationships
	/**
	 * Find relationships by given sid. The found relationships are those that have a story with given id at either end.
	 * @param sid is id of the story for which to find relationships.
	 * @return all relationships of given story.
	 */
	//List<ColfusionRelationships> findRelationshipsBySid(int sid);
	
	/**
     * Find only one entity by id.
     * 
     * @param clazz of entity to be found.
     * @param id to search for.
     * @return found entity.
     * @throws Exception 
     */
     @Override
     public ColfusionRelationships findByID(Integer id) throws Exception;

     /**
      * Get all links of a relationships including data matching ratios at given similarity threshold.
      *  
      * @param relId
      * @param similarityThreshold
      * @return
     * @throws Exception 
      */
     List<RelationshipLinkViewModel> getRelationshipLinks(int relId,
			BigDecimal similarityThreshold) throws Exception;
}
