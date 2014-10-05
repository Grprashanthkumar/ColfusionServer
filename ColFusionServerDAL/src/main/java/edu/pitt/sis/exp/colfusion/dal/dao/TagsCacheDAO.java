package edu.pitt.sis.exp.colfusion.dal.dao;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionTagCache;

public interface TagsCacheDAO extends GenericDAO<ColfusionTagCache, Integer> {

	/**
	 * Deletes all tag cached records from tag cache table.
	 * @throws Exception 
	 */
	void deleteAll() throws Exception;

}
