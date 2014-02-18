/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.math.BigDecimal;
import java.util.List;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;

/**
 * @author Evgeny
 *
 */
public interface SourceInfoManager {
	
	//**********************
	// Generic methods
	//**********************
	
	/**
	 * Finds all datasets (source infos).
	 * 
	 * @return all the datasets from the database with no filtering.
	 */
	public List<ColfusionSourceinfo> loadAllSourceinfos();
 
	/**
	 * Save new source info (dataset) into the database.
	 * 
	 * @param sourceInfo the new source info object which needs to be saved.
	 */
    public void saveNewSourceinfo(ColfusionSourceinfo sourceInfo);
 
    /**
     * Finds one source info which has given sid.
     * 
     * @param sid id of the source info to be search for.
     * @return
     */
    public ColfusionSourceinfo findSourceinfoById(BigDecimal sid);
 
    /**
     * Delete given source info.
     * 
     * @param sourceInfo to be deleted.
     */
    public void deleteSourceinfo(ColfusionSourceinfo sourceInfo);
    
    
    //****************************
    // Custom
    //****************************
    
    //TODO, FIXME:seems like this is just a copy of the SourceinfoDAO interface, if it will be repeated for other manages, maybe they should be inherited from those interfaces
    
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
