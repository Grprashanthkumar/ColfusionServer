/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.hibernate.HibernateException;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;

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
	 * @throws Exception 
	 */
	public ColfusionSourceinfo findDatasetInfoBySid(int sid, boolean includeDraft) throws HibernateException;
	
	/**
	 * Find stories which titles contain search term.
	 * @param searchTerm the term to look for in the title of stories.
	 * @param limit the up to number of stories to return.
	 * @return the list of stories which satisfy the conditions.
	 * @throws Exception 
	 */
	public List<ColfusionSourceinfo> lookupStories(String searchTerm, int limit) throws HibernateException;

	/**
	 * Find all sourceinfos which don't have relationships
	 * Edited by haoyu Wang
	*/
	
	public List<ColfusionSourceinfo> findOneSidsTrue() throws HibernateException;
	
	/**
	 * Find all sourceinfos that are published, with a status of "queued"
	 * Edited by haoyu Wang
	*/
	public List<ColfusionSourceinfo> findSourceInfoByStatus(SourceInfoStatus status) throws HibernateException;
	
	public static enum SourceInfoStatus {
		DRAFT("draft"),
		QUEUED("queued"),
		//modified by Shruti
		PRIVATE("private");
		
		private final String databaseStatusValue;
		
		SourceInfoStatus(final String databaseStatusValue) {
			this.databaseStatusValue = databaseStatusValue;
		}
		
		public String getDatabaseStatusValue() {
			return databaseStatusValue;
		}
	}
}
