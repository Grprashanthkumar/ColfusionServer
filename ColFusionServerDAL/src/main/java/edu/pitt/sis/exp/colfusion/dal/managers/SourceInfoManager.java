/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.pitt.sis.exp.colfusion.importers.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.dal.managers.SourceInfoManagerImpl.HistoryItem;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUserroles;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataHistoryViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryMetadataViewModel;
import edu.pitt.sis.exp.colfusion.viewmodels.StoryTargetDBViewModel;

/**
 * @author Evgeny
 *
 */
public interface SourceInfoManager extends GeneralManager<ColfusionSourceinfo, Integer> {
	
    /**
	 * 
	 * Finds all datasets (source infos) which were submitted by given userid.
	 * 
	 * @param userId by which the search should be performed.
	 * @return all found datasets which conform to the search criteria. 
	 */
	public List<ColfusionSourceinfo> findByUserId(int userId);
	
	/**
	 * Finds all datasets (source infos) by either source id (sid) or by a keywords/phrase in dataset title.
	 * 
	 * @param searchTerm could be either dataset id (sid) or keyword/phrase which should be contained in the dataset title.
	 * @return all found datasets which conform to the search criteria. 
	 */
	public List<ColfusionSourceinfo> findBySidOrTitle(String searchTerm);
	
	/**
	 * Find one dataset or none by dataset id (sid).
	 * 
	 * @param sid the id of the dataset to search for.
	 * @param includeDraft indicates whether a draft dataset be returned or not.
	 * @return all found datasets which conform to the search criteria. 
	 */
	public ColfusionSourceinfo findBySid(int sid, boolean includeDraft);
	
	/**
	 * Finds all datasets that have title containing given search term as a keyword/phrase.
	 * 
	 * @param searchTerm to search to be contained in the datasets title.
	 * @return all found datasets which conform to the search criteria. 
	 */
	public List<ColfusionSourceinfo> findByTitle(String searchTerm, int limit);

	/**
	 * Creates new story with given input parameters and stores it in the database and the resulting entity has automatically generated sid.
	 * 
	 * @param userId is the id of the user who is creating the new story.
	 * @param date when the new story is created.
	 * @param source_type type of the source from which the data will be imported.
	 * @return newly created story which is stored in the db. Has auto generated sid.
	 * @throws Exception 
	 */
	public ColfusionSourceinfo newStory(int userId, Date date, DataSourceTypes source_type) throws Exception;

	/**
	 * Updates both sourceinfo and links table with story metadata.
	 * 
	 * @param metadata the metadata to be used to update the tables in database.
	 * @throws Exception 
	 */
	public void updateStory(StoryMetadataViewModel metadata) throws Exception;

	/**
	 * Transforms referenced field ColfusionSourceinfoUsers of a given story into map where keys are user ids and values are records from StoryUserRoles table which
	 * describes in which role each user is participating in the given story.
	 * 
	 * @param newStory for which the information need to be transformed.
	 * @return the map as described in the description. They key is UserId and the value is ColfusionUserroles.
	 */
	public Map<Integer, ColfusionUserroles> getUsersInRolesForStory(ColfusionSourceinfo newStory);

	/**
	 * Find all authors of type {@link ColfusionUsers} of a given story {@link ColfusionSourceinfo}.
	 * 
	 * @param storyInfo the story for which to find all authors.
	 * @return the {@link List} of authors.
	 */
	public List<ColfusionUsers> findStoryAuthors(ColfusionSourceinfo storyInfo);

	/**
	 * Fetches the metadata edit history for given story and history item.
	 * 
	 * @param sid id of the story for which to fetch the history.
	 * @param historyItem the history item should be one of the values of the {@link HistoryItem} provided as string.
	 */
	public StoryMetadataHistoryViewModel getStoryMetadataHistory(int sid, String historyItem);

	/**
	 * Fetches the story type field from the sourceinfo record by sid.
	 * @param sid id of the story.
	 * @return the type of the source.
	 */
	public DataSourceTypes getStorySourceType(int sid) throws Exception;
	
	/**
	 * Adds or updates record about target database for a story. The linked server is also created or updated.
	 * 
	 * @param sourceDBInfo the info about the target database.
	 */
	public void saveOrUpdateSourceInfoDB(StoryTargetDBViewModel sourceDBInfo) throws Exception;

	/**
	 * Get locations of KTR files associated with a given sourceinfo record;
	 * @param sid is of the sourceinfo record/story.
	 * @return locations of KTRs files associated with the story.
	 * @throws Exception 
	 */
	public ArrayList<String> getStoryKTRLocations(int sid) throws Exception;

	/**
	 * Return info about target database connection for given story.
	 * 
	 * @param sid is of the story.
	 * @return the target database info.
	 */
	public StoryTargetDBViewModel getStorySourceInfoDB(int sid);

	/**
	 * Find a story by the id of one of the columns.
	 * @param integer column id.
	 * @return story that contains that column.
	 */
	public ColfusionSourceinfo findStoryByCid(Integer integer);
}
