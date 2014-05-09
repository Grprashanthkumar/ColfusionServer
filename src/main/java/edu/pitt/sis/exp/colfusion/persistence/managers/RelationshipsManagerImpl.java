/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.pitt.sis.exp.colfusion.persistence.dao.RelationshipsDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationships;

/**
 * @author Evgeny
 *
 */
public class RelationshipsManagerImpl extends GeneralManagerImpl<ColfusionRelationships, Integer> implements RelationshipsManager {

	Logger logger = LogManager.getLogger(RelationshipsManagerImpl.class.getName());
	
	public RelationshipsManagerImpl() {
		super(new RelationshipsDAOImpl(), ColfusionRelationships.class);
	}
}
