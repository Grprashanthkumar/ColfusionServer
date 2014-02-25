/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.List;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;

/**
 * The interface which describes the functionality for searching for datasets (source infos) by several parameters like userid, sid and title.
 * 
 * @author Evgeny
 *
 * From DatasetFinder.php.
 */
public interface SourceInfoDAO extends GenericDAO<ColfusionSourceinfo, Integer> {
	
	
	/**
	 * 
	 * Finds all datasets (source infos) which were submitted by given userid.
	 * 
	 * @param userId by which the search should be performed.
	 * @return all found datasets which conform to the search criteria. 
	 */
	public List<ColfusionSourceinfo> findDatasetsInfoByUserId(int userId);
	
	/**
	 * Finds all datasets (source infos) by either source id (sid) or by a keywords/phrase in dataset title.
	 * 
	 * @param searchTerm could be either dataset id (sid) or keyword/phrase which should be contained in the dataset title.
	 * @return all found datasets which conform to the search criteria. 
	 */
	public List<ColfusionSourceinfo> findDatasetInfoBySidOrTitle(String searchTerm);
	
	/**
	 * Find one dataset or none by dataset id (sid).
	 * 
	 * @param sid the id of the dataset to search for.
	 * @param includeDraft indicates whether a draft dataset be returned or not.
	 * @return all found datasets which conform to the search criteria. 
	 */
	public ColfusionSourceinfo findDatasetInfoBySid(int sid, boolean includeDraft);
	
	/**
	 * Finds all datasets that have title containing given search term as a keyword/phrase.
	 * 
	 * @param searchTerm to search to be contained in the datasets title.
	 * @return all found datasets which conform to the search criteria. 
	 */
	public List<ColfusionSourceinfo> findDatasetsInfoByTitle(String searchTerm);
}
