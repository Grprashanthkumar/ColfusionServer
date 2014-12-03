/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;

/**
 * @author Evgeny
 *
 */
public interface RelationshipsDAO extends GenericDAO<ColfusionRelationships, Integer> {
	
	public List<ColfusionRelationships> findRelationshipByStatus(Integer status);

}
