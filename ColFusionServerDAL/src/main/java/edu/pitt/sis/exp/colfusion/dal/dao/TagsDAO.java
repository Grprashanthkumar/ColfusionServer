package edu.pitt.sis.exp.colfusion.dal.dao;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionTags;

public interface TagsDAO extends GenericDAO<ColfusionTags, Integer> {

	/**
	 * Delete all tags from the tags table of a specific story specified by sid.
	 * 
	 * @param sid of the story for which to delete all tags.
	 * @throws Exception 
	 */
	void deleteAllBySid(int sid) throws Exception;

}
