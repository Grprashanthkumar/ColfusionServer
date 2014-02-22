package edu.pitt.sis.exp.colfusion.persistence.dao;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionTags;

public interface TagsDAO extends GenericDAO<ColfusionTags, Integer> {

	/**
	 * Delete all tags from the tags table of a specific story specified by sid.
	 * 
	 * @param sid of the story for which to delete all tags.
	 */
	void deleteAllBySid(int sid);

}
