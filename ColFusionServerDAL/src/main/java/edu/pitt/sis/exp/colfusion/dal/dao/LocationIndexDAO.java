package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionIndexlocation;

public interface LocationIndexDAO extends GenericDAO<ColfusionIndexlocation, Integer>{
	/**
	 * 
	 * Finds all location index
	 * 
	 * @return all found datasets which conform to the search ColfusionDesAttachments. 
	 */
	public List<ColfusionIndexlocation> findAllLocation();
	
	/**
	 * 
	 * Finds location index which were submitted by given sid.
	 * 
	 * @param sid by which the search should be performed.
	 * @return all found location index
	 */
	public List<ColfusionIndexlocation> findLocationBySid(int sid);
}
