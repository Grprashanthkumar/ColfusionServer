/**
 *
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.LicenseInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.LicenseInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.LinksDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.LinksDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAO.SourceInfoStatus;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDBDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDBDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoMetadataEditHistoryDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoMetadataEditHistoryDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoTableKTRDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoTableKTRDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceinfoUserRolesDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceinfoUserRolesDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.TagsCacheDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.TagsCacheDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.TagsDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.TagsDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.UserRolesDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.UserRolesDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.UsersDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.UsersDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLicense;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionLinks;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoDb;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoMetadataEditHistory;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoUser;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfoUserId;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionTags;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionTagsId;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUserroles;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.utils.DataSourceTypes;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.dal.utils.MappingUtils;
import edu.pitt.sis.exp.colfusion.dal.utils.StoryStatusTypes;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.LicenseViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipsViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryAuthorViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryListViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataHistoryLogRecordViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataHistoryViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryMetadataViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryTargetDBViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.UserViewModel;

public class SourceInfoManagerImpl extends GeneralManagerImpl<SourceInfoDAO, ColfusionSourceinfo, Integer> implements SourceInfoManager {

	Logger logger = LogManager.getLogger(SourceInfoManagerImpl.class.getName());

	public SourceInfoManagerImpl() {
		super(new SourceInfoDAOImpl(), ColfusionSourceinfo.class);
	}

	public enum HistoryItem {
		TITLE("title"), DESCRIPTION("description"), TAGS("tags"), STATUS("status"), LICENSE("license");


		private String value;

		private HistoryItem(final String value) {
			this.value = value;
		}

		public String getValue(){
			return this.value;
		}

		static public boolean isMember(final String enumValueToTest) {
			final HistoryItem[] enumValues = HistoryItem.values();
			for (final HistoryItem enumValue : enumValues) {
				if (enumValue.value.equals(enumValueToTest)) {
					return true;
				}
			}
			return false;
		}
	};

	//***************************************
	// General manager interface
	//***************************************

	@Override
	public ColfusionSourceinfo findByID(final Integer id) {
		ColfusionSourceinfo result = null;
		try {
			HibernateUtil.beginTransaction();

			result = this._dao.findByID(ColfusionSourceinfo.class, id);

			if (result != null) {

				Hibernate.initialize(result.getColfusionUsers());
			}

			HibernateUtil.commitTransaction();
		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findByID failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findByID failed HibernateException", ex);
			throw ex;
		}
		return result;
	}

	//************************************
	// SOURCINFOMANAGER interface
	//***********************************

	/* modified by Shruti Sabusuresh
	 */
	/**
	 * finds all stories submitted by user
	 * @param userId
	 * @return list of ColfusionSourceinfo objects
	 */
	@Override
	public List<ColfusionSourceinfo> findByUserId(final int userId) {
		try {
			HibernateUtil.beginTransaction();
			final List<ColfusionSourceinfo> result = this._dao.findDatasetsInfoByUserId(userId);
			for (final ColfusionSourceinfo sourceinfo : result) {
				Hibernate.initialize(sourceinfo.getColfusionUsers());
				Hibernate.initialize(sourceinfo.getColfusionSourceinfoUsers());
				for (final Object sourceInfoUserObj : sourceinfo.getColfusionSourceinfoUsers().toArray()) {
					final ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;
					if (sourceInfoUser != null) {
						Hibernate.initialize(sourceInfoUser.getColfusionUsers());
					}
				}
			}
			HibernateUtil.commitTransaction();
			return result;
		} catch (final HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error("findByUserId failed: HibernateException ", ex);
			throw ex;
		} catch (final Exception ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error("findByUserId failed: Exception ", ex);
			throw ex;
		}
	}

	@Override
	public List<ColfusionSourceinfo> findBySidOrTitle(
			final String searchTerm) {
		List<ColfusionSourceinfo> results = null;
		try {
			HibernateUtil.beginTransaction();

			results = this._dao.findDatasetInfoBySidOrTitle(searchTerm);

			for (final ColfusionSourceinfo sourceinfo : results) {

				Hibernate.initialize(sourceinfo.getColfusionUsers());
				Hibernate.initialize(sourceinfo.getColfusionSourceinfoUsers());

				for (final Object sourceInfoUserObj : sourceinfo.getColfusionSourceinfoUsers().toArray()) {
					final ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;

					if (sourceInfoUser != null) {
						Hibernate.initialize(sourceInfoUser.getColfusionUsers());
					}
				}
			}

			HibernateUtil.commitTransaction();
		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySidOrTitle failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySidOrTitle failed HibernateException", ex);
			throw ex;
		}
		return results;
	}

	@Override
	public ColfusionSourceinfo findBySid(final int sid, final boolean includeDraft) {
		ColfusionSourceinfo sourceinfo = null;
		try {
			HibernateUtil.beginTransaction();

			sourceinfo = this._dao.findDatasetInfoBySid(sid, includeDraft);

			if (sourceinfo != null) {

				Hibernate.initialize(sourceinfo.getColfusionUsers());
				Hibernate.initialize(sourceinfo.getColfusionSourceinfoUsers());

				for (final Object sourceInfoUserObj : sourceinfo.getColfusionSourceinfoUsers().toArray()) {
					final ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;

					if (sourceInfoUser != null) {
						Hibernate.initialize(sourceInfoUser.getColfusionUsers());
					}
				}
			}

			HibernateUtil.commitTransaction();
		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed HibernateException", ex);
			throw ex;
		}
		catch(final Exception ex){
			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed Exception", ex);
			throw ex;
		}
		return sourceinfo;
	}

	@Override
	public List<ColfusionSourceinfo> findByTitle(final String searchTerm, final int limit) {
		try {
			HibernateUtil.beginTransaction();

			final List<ColfusionSourceinfo> result = this._dao.lookupStories(searchTerm, limit);

			for (final ColfusionSourceinfo sourceinfo : result) {

				Hibernate.initialize(sourceinfo.getColfusionUsers());
				Hibernate.initialize(sourceinfo.getColfusionSourceinfoUsers());

				for (final Object sourceInfoUserObj : sourceinfo.getColfusionSourceinfoUsers().toArray()) {
					final ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;

					if (sourceInfoUser != null) {
						Hibernate.initialize(sourceInfoUser.getColfusionUsers());
					}
				}
			}

			HibernateUtil.commitTransaction();

			return result;
		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("save failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("save failed HibernateException", ex);
			throw ex;
		}
	}

	/**
	 * Creates new story with given input parameters and stores it in the database and the resulting entity has automatically generated sid.
	 *
	 * @param userId is the id of the user who is creating the new story.
	 * @param date when the new story is created.
	 * @param source_type type of the source from which the data will be imported.
	 * @return newly created story which is stored in the db. Has auto generated sid.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ColfusionSourceinfo newStory(final int userId, final Date date, final DataSourceTypes source_type) throws Exception {
		try {
			this.logger.info(String.format("Creating new story by user with id %d. Date: %s, source type:%s", userId, date.toString(), source_type.getValue()));

			this.logger.info(String.format("Creating new story by user with id %d. Date: %s, source type:%s",
					userId, date.toString(), source_type.getValue()));

			HibernateUtil.beginTransaction();

			final UsersDAO usersDAO = new UsersDAOImpl();
			final ColfusionUsers userCreator = usersDAO.findByID(ColfusionUsers.class, userId);
			if (userCreator == null) {
				this.logger.error(String.format("newStory failed: could not find user by %d id", userId));
				throw new Exception(String.format("newStory failed: could not find user by %d id", userId));
			}

			final ColfusionSourceinfo newStoryEntity = new ColfusionSourceinfo(userCreator, date, source_type.getValue());
			newStoryEntity.setStatus(StoryStatusTypes.DRAFT.getValue());
			final int sid = this._dao.save(newStoryEntity);

			//Add user as submitter/owner for newly created story.
			final UserRolesDAO userRolesDAO = new UserRolesDAOImpl();
			// 1 is for contributor/submitter, 2 for owner TODO: maybe we should use enum here not hard wired value.
			//made the submitter as owner - Shruti
			final ColfusionUserroles userRole = userRolesDAO.findByID(ColfusionUserroles.class, 2);
			if (userRole == null) {
				this.logger.error(String.format("newStory failed: could not find userRole by %d id", 2));

				throw new Exception(String.format("newStory failed: could not find userRole by %d id", 2));
			}
			final SourceinfoUserRolesDAO sourceinfoUserRoles = new SourceinfoUserRolesDAOImpl();
			final ColfusionSourceinfoUser colfusionSourceinfoUser = new ColfusionSourceinfoUser(new ColfusionSourceinfoUserId(sid, userCreator.getUserId()),
					newStoryEntity, userRole, userCreator);
			final ColfusionSourceinfoUserId c = sourceinfoUserRoles.save(colfusionSourceinfoUser);

			newStoryEntity.getColfusionSourceinfoUsers().add(colfusionSourceinfoUser);
			//sourceInfoDAO.saveOrUpdate(newStoryEntity);

			Hibernate.initialize(newStoryEntity.getColfusionUsers());
			HibernateUtil.commitTransaction();

			return newStoryEntity;
		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();
			ex.printStackTrace();
			this.logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();
			ex.printStackTrace();
			this.logger.error("findDatasetInfoBySid failed HibernateException", ex);
			throw ex;
		}
	}

	/**
	 * Updates both sourceinfo and links table with story metadata.
	 *
	 * @param metadata the metadata to be used to update the tables in database.
	 * @throws Exception
	 */
	@Override
	public void updateStory(final StoryMetadataViewModel metadata) throws Exception {
		try {
			HibernateUtil.beginTransaction();
			final ColfusionSourceinfo addedUpdatedStory = this.updateSourceInfo(metadata);
			this.updateUserRoles(addedUpdatedStory, metadata);
			this.updateLink(metadata);
			this.updateTags(metadata);

			HibernateUtil.commitTransaction();
		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed HibernateException", ex);
			throw ex;
		} catch (final Exception e) {
			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed most probably because of updateSourceInfo failed", e);
			throw e;
		}
	}

	private ColfusionSourceinfo updateSourceInfo(final StoryMetadataViewModel metadata) throws Exception {
		final UsersDAO usersDAO = new UsersDAOImpl();

		final ColfusionUsers userCreator = usersDAO.findByID(ColfusionUsers.class, metadata.getStorySubmitter().getUserId());
		final LicenseInfoDAO licensedao = new LicenseInfoDAOImpl();

		final ColfusionLicense colfusionLicense = licensedao.findByID(ColfusionLicense.class, metadata.getLicenseId());
		final ColfusionSourceinfo newStoryEntity = new ColfusionSourceinfo(userCreator, metadata.getDateSubmitted(), metadata.getSourceType());
		newStoryEntity.setSid(metadata.getSid());
		newStoryEntity.setTitle(metadata.getTitle());
		newStoryEntity.setDescription(metadata.getDescription());
		newStoryEntity.setStatus(metadata.getStatus());
		newStoryEntity.setColfusionLicense(colfusionLicense);


		try {
			this.handleHistoryEdits(newStoryEntity, metadata.getUserId(), metadata.getEditReason());
		} catch (final Exception e) {
			this.logger.error(String.format("updateSourceInfo failed on handleHistoryEdits"));

			throw e;
		}
		this._dao.merge(newStoryEntity);

		return newStoryEntity;
	}

	//TODO: should we move the next two methods to other class? together with enum?
	/**
	 * Checks if new instance of a record of sourceinfo has title and status difference from the one stored in the database already.
	 * If they do differ, then old value is copied into history of edits table before replaced by new value.
	 *
	 * @param newStory new instance of the sourceinfo record possibly updated by user during edit operation.
	 * @param userId id of the user who did edit (note, it is not the submitter, it is id of the user who was logged in and performed the edit).
	 * @throws Exception
	 */
	private void handleHistoryEdits(final ColfusionSourceinfo newStory, final int userId, final String reason) throws Exception {
		final SourceInfoMetadataEditHistoryDAO editHistorDAO = new SourceInfoMetadataEditHistoryDAOImpl();

		final ColfusionSourceinfo oldStory = this._dao.findByID(ColfusionSourceinfo.class, newStory.getSid());
		//find the original information stored in ColfusionSourceInfo table
		try {
			String oldValue = (oldStory == null) ? null : oldStory.getTitle();
			//get the value of title if null==>null
			editHistorDAO.saveHistoryIfChanged(newStory.getSid(), userId, oldValue, newStory.getTitle(), HistoryItem.TITLE,  reason);

			oldValue = (oldStory == null) ? null : oldStory.getStatus();
			editHistorDAO.saveHistoryIfChanged(newStory.getSid(), userId, oldValue, newStory.getStatus(), HistoryItem.STATUS,  reason);

			oldValue = (oldStory.getColfusionLicense() == null) ? null : oldStory.getColfusionLicense().getLicenseName();
			editHistorDAO.saveHistoryIfChanged(newStory.getSid(), userId, oldValue, newStory.getColfusionLicense().getLicenseName(), HistoryItem.LICENSE, reason);


		} catch (final Exception e) {
			this.logger.error(String.format("handleHistoryEdits for sourceinfo failed due to one of the two calls of editHistorDAO.saveHistoryIfChanged. userId = %d and reason is %s", userId, reason));

			throw e;
		}
	}

	/**
	 * Checks if new instance of a record of links has content/description and tags difference from the one stored in the database already.
	 * If they do differ, then old value is copied into history of edits table before replaced by new value.
	 *
	 * @param newLink new instance of the links record possibly updated by user during edit operation.
	 * @param userId id of the user who did edit (note, it is not the submitter, it is id of the user who was logged in and performed the edit).
	 * @throws Exception
	 */
	private void handleHistoryEdits(final ColfusionLinks newLink, final int userId, final String reason) throws Exception {
		final SourceInfoMetadataEditHistoryDAO editHistorDAO = new SourceInfoMetadataEditHistoryDAOImpl();

		final LinksDAO linksDAO = new LinksDAOImpl();
		final ColfusionLinks oldLink = linksDAO.findByID(ColfusionLinks.class, newLink.getLinkId());

		try {
			String oldValue = (oldLink == null) ? null : oldLink.getLinkContent();
			editHistorDAO.saveHistoryIfChanged(newLink.getLinkId(), userId, oldValue, newLink.getLinkContent(), HistoryItem.DESCRIPTION, reason);

			oldValue = (oldLink == null) ? null : oldLink.getLinkTags();
			editHistorDAO.saveHistoryIfChanged(newLink.getLinkId(), userId, oldValue, newLink.getLinkTags(), HistoryItem.TAGS, reason);
		} catch (final Exception e) {
			this.logger.error(String.format("handleHistoryEdits for links failed due to one of the two calls of editHistorDAO.saveHistoryIfChanged. userId = %d and reason is %s", userId, reason));

			throw e;
		}
	}


	/**
	 * Adding or updating information about roles of users for a given dataset/story.
	 * @param addedUpdatedStory {@link ColfusionSourceinfo} is the story for which information about user and their roles needs to be added/updated.
	 * @param metadata {@link StoryMetadataViewModel} holds the information which need to be inserted/updated in the db.
	 */
	private void updateUserRoles(final ColfusionSourceinfo addedUpdatedStory, final StoryMetadataViewModel metadata) {
		final UsersDAO usersDAO = new UsersDAOImpl();
		final SourceinfoUserRolesDAO userStoryRolesDAO = new SourceinfoUserRolesDAOImpl();
		final UserRolesDAO userRolesDAO = new UserRolesDAOImpl();

		this.logger.info("started updatingUserRolesFor story");

		//TODO: add checks if user or role could be found
		for (final StoryAuthorViewModel author : metadata.getStoryAuthors()) {
			final ColfusionUsers user = usersDAO.findByID(ColfusionUsers.class, author.getUserId());

			if (author.getRoleId() <= 0) {
				//TODO:handle it better.
				continue;
			}
			final ColfusionUserroles userRole = userRolesDAO.findByID(ColfusionUserroles.class, author.getRoleId());

			if (userRole == null) {
				//TODO: handle it better
				continue;
			}
			final ColfusionSourceinfoUserId colfusionSourceinfoUserId = new ColfusionSourceinfoUserId(addedUpdatedStory.getSid(), user.getUserId());

			ColfusionSourceinfoUser userRolesInStory = userStoryRolesDAO.findByID(ColfusionSourceinfoUser.class, colfusionSourceinfoUserId);
			//if user-sid combination does not exist in the database, add a new row in colfusion_sourceinfo_users table
			if(userRolesInStory == null) {
				userRolesInStory = new ColfusionSourceinfoUser(colfusionSourceinfoUserId, addedUpdatedStory, userRole, user);
			} else {
				//else update the user's role for a particular sid - modified by Shruti
				userRolesInStory.setColfusionUserroles(userRole);
			}

			userStoryRolesDAO.saveOrUpdate(userRolesInStory);
		}

		for (final StoryAuthorViewModel author : metadata.getRemovedStoryAuthors()) {
			final ColfusionUsers user = usersDAO.findByID(ColfusionUsers.class, author.getUserId());

			if (author.getRoleId() <= 0) {
				//TODO:handle it better.
				continue;
			}
			final ColfusionUserroles userRole = userRolesDAO.findByID(ColfusionUserroles.class, author.getRoleId());

			if (userRole == null) {
				//TODO: handle it better
				continue;
			}
			final ColfusionSourceinfoUserId colfusionSourceinfoUserId = new ColfusionSourceinfoUserId(addedUpdatedStory.getSid(), user.getUserId());

			final ColfusionSourceinfoUser userRolesInStory = new ColfusionSourceinfoUser(colfusionSourceinfoUserId, addedUpdatedStory, userRole, user);

			userStoryRolesDAO.delete(userRolesInStory);
		}

		this.logger.info("finished updatingUserRolesFor story");
	}

	private void updateLink(final StoryMetadataViewModel metadata) throws Exception {
		this.logger.info(String.format("Starting processing updateLink for %s", metadata.getSid()));
		final ColfusionLinks newLink = new ColfusionLinks(metadata.getSid(), metadata.getStorySubmitter().getUserId(), 0, 0, 0, 0, new BigDecimal(0.0), metadata.getDateSubmitted(), metadata.getDateSubmitted(),
				metadata.getDateSubmitted(), 0, 1, 0, metadata.getStatus(), 0);
		newLink.setLinkTitle(metadata.getTitle());
		newLink.setLinkContent(metadata.getDescription());
		newLink.setLinkSummary(metadata.getDescription());
		newLink.setLinkStatus(metadata.getStatus());
		newLink.setLinkTags(metadata.getTags());
		newLink.setLinkTitleUrl(String.valueOf(metadata.getSid()));

		try {
			this.handleHistoryEdits(newLink, metadata.getUserId(), metadata.getEditReason());
		} catch (final Exception e) {
			this.logger.error(String.format("updateLink failed on handleHistoryEdits"));

			throw e;
		}

		final LinksDAO linksDAO = new LinksDAOImpl();
		linksDAO.merge(newLink);
		this.logger.info(String.format("Ending processing updateLink for %s", metadata.getSid()));
	}

	private void updateTags(final StoryMetadataViewModel metadata) throws Exception {
		final TagsDAO tagsDAO = new TagsDAOImpl();

		try {
			tagsDAO.deleteAllBySid(metadata.getSid());
		} catch (final Exception e) {
			this.logger.error(String.format("updateTags failed on tagsDAO.deleteAllBySid(metadata.getSid()); for sid = %d", metadata.getSid()));

			throw e;
		}


		if (metadata.getTags().length() > 0) {
			final String[] tagsSplit = metadata.getTags().split(",|;");

			for(final String tag : tagsSplit) {
				final ColfusionTags colfusionTag = new ColfusionTags(new ColfusionTagsId(metadata.getSid(), "en", metadata.getDateSubmitted(), tag.trim()));
				tagsDAO.saveOrUpdate(colfusionTag);
			}
		}

		final TagsCacheDAO tagsCacheDAO = new TagsCacheDAOImpl();

		try {
			tagsCacheDAO.deleteAll();
		} catch (final Exception e) {
			this.logger.error(String.format("updateTags failed on tagsCacheDAO.deleteAll();for sid = %d", metadata.getSid()));

			throw e;
		}
	}

	/**
	 * Transforms referenced field ColfusionSourceinfoUsers of a given story into map where keys are user ids and values are records from StoryUserRoles table which
	 * describes in which role each user is participating in the given story.
	 *
	 * @param newStory for which the information need to be transformed.
	 * @return the map as described in the description.
	 */
	@Override
	public Map<Integer, ColfusionUserroles> getUsersInRolesForStory(final ColfusionSourceinfo newStory) {

		//TODO, FIXME: the key in the map should not be user id, it should be composition of userid and roleid.
		// Right now one user can have only one role, but in database we allow one user to have several roles.

		final Map<Integer, ColfusionUserroles> result = new HashMap<Integer, ColfusionUserroles>();
		if (newStory.getColfusionSourceinfoUsers().iterator().hasNext()) {

			for ( final Object userRoleObj : newStory.getColfusionSourceinfoUsers().toArray()) {

				final ColfusionSourceinfoUser userRole = (ColfusionSourceinfoUser) userRoleObj;

				if (!result.containsKey(userRole.getId().getUid())) {
					result.put(userRole.getId().getUid(), userRole.getColfusionUserroles());
				}
			}
		}

		return result;
	}

	@Override
	public List<ColfusionUsers> findStoryAuthors(final ColfusionSourceinfo story) {
		final ArrayList<ColfusionUsers> result = new ArrayList<>();

		for (final Object sourceInfoUserObj : story.getColfusionSourceinfoUsers().toArray()) {
			final ColfusionSourceinfoUser sourceInfoUser = (ColfusionSourceinfoUser) sourceInfoUserObj;

			result.add(sourceInfoUser.getColfusionUsers());
		}

		return result;
	}

	@Override
	public StoryMetadataHistoryViewModel getStoryMetadataHistory(final int sid, final String historyItem) {
		try {
			HibernateUtil.beginTransaction();


			final SourceInfoMetadataEditHistoryDAO metadataHistoryDAO = new SourceInfoMetadataEditHistoryDAOImpl();

			//TODO: this should be moved to hitory manager
			final String sql = "select h from ColfusionSourceinfoMetadataEditHistory h where h.colfusionSourceinfo = :sid and h.item = :item ORDER BY h.hid DESC";
			final ColfusionSourceinfo sourceInfo = this._dao.findByID(ColfusionSourceinfo.class, sid);
			final Query query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sourceInfo).setParameter("item", historyItem);
			final ArrayList<ColfusionSourceinfoMetadataEditHistory> historyLog = (ArrayList<ColfusionSourceinfoMetadataEditHistory>) metadataHistoryDAO.findMany(query);


			final StoryMetadataHistoryViewModel result = new StoryMetadataHistoryViewModel();
			result.setHistoryItem(historyItem);
			result.setSid(sid);

			for (final ColfusionSourceinfoMetadataEditHistory historyRecord : historyLog) {

				if (historyRecord != null) {

					Hibernate.initialize(historyRecord.getColfusionUsers());
					final StoryAuthorViewModel author = MappingUtils.getInstance().mapColfusionUserToStoryAuthorViewModel(historyRecord.getColfusionUsers());

					final StoryMetadataHistoryLogRecordViewModel historyRecordViewModel = new StoryMetadataHistoryLogRecordViewModel();
					historyRecordViewModel.setHid(historyRecord.getHid());
					historyRecordViewModel.setItem(historyRecord.getItem());
					historyRecordViewModel.setItemValue(historyRecord.getItemValue());
					historyRecordViewModel.setReason(historyRecord.getReason());
					historyRecordViewModel.setWhenSaved(historyRecord.getWhenSaved());
					historyRecordViewModel.setAuthor(author);

					result.getHistoryLogRecords().add(historyRecordViewModel);
				}
			}

			HibernateUtil.commitTransaction();

			return result;
		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed HibernateException", ex);
			throw ex;
		}
	}

	@Override
	public DataSourceTypes getStorySourceType(final int sid) throws Exception {
		final ColfusionSourceinfo story = this.findByID(sid);

		if (story != null) {
			return DataSourceTypes.fromString(story.getSourceType());
		}
		else {
			this.logger.error(String.format("getStorySourceType failed: Story with %d sid not found", sid));

			//TODO: create custom exception StoryNotFound
			throw new Exception(String.format("Story with %d sid not found", sid));
		}
	}



	@Override
	public void saveOrUpdateSourceInfoDB(final StoryTargetDBViewModel sourceDBInfo) throws Exception {
		try {
			HibernateUtil.beginTransaction();

			final ColfusionSourceinfo story = this._dao.findByID(ColfusionSourceinfo.class, sourceDBInfo.getSid());

			if (story == null) {
				this.logger.error(String.format("saveOrUpdateSourceInfoDB failed: could not find story with %d sid", sourceDBInfo.getSid()));

				throw new Exception(String.format("saveOrUpdateSourceInfoDB failed: could not find story with %d sid", sourceDBInfo.getSid()));
			}

			ColfusionSourceinfoDb storyDB = null;

			if (story.getColfusionSourceinfoDb() == null) {
				storyDB = new ColfusionSourceinfoDb(story,
						sourceDBInfo.getServerAddress(), sourceDBInfo.getPort(), sourceDBInfo.getUserName(), sourceDBInfo.getPassword(),
						sourceDBInfo.getDatabaseName(), sourceDBInfo.getDriver(), sourceDBInfo.getIsLocal(), sourceDBInfo.getLinkedServerName());
			}
			else {
				storyDB = story.getColfusionSourceinfoDb();
				storyDB.setServerAddress(sourceDBInfo.getServerAddress());
				storyDB.setPort(sourceDBInfo.getPort());
				storyDB.setUserName(sourceDBInfo.getUserName());
				storyDB.setPassword(sourceDBInfo.getPassword());
				storyDB.setSourceDatabase(sourceDBInfo.getDatabaseName());
				storyDB.setDriver(sourceDBInfo.getDriver());
				storyDB.setIsLocal(sourceDBInfo.getIsLocal());
				storyDB.setLinkedServerName(sourceDBInfo.getLinkedServerName());
			}

			story.setSid(sourceDBInfo.getSid());

			final SourceInfoDBDAO sourceInfoDBDAO = new SourceInfoDBDAOImpl();

			sourceInfoDBDAO.saveOrUpdate(storyDB);

			HibernateUtil.commitTransaction();
		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed HibernateException", ex);
			throw ex;
		}
	}

	//TODO: add tests
	@Override
	public ArrayList<String> getStoryKTRLocations(final int sid) throws Exception {
		try {
			HibernateUtil.beginTransaction();

			final SourceInfoTableKTRDAO sourceTableKTRDAO = new SourceInfoTableKTRDAOImpl();

			final ArrayList<ColfusionSourceinfoTableKtr> sourceTableKTRs = sourceTableKTRDAO.getKTRLocationsBySid(sid);

			final ArrayList<String> result = new ArrayList<>();

			for(final ColfusionSourceinfoTableKtr sourceTableKTR : sourceTableKTRs) {
				result.add(sourceTableKTR.getPathToKtrfile());
			}

			HibernateUtil.commitTransaction();

			return result;

		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed HibernateException", ex);
			throw ex;
		} catch (final Exception e) {
			this.logger.error(String.format("getStoryKTRLocations faild on most probably sourceTableKTRDAO.getKTRLocationsBySid(sid); for sid = %d", sid));

			throw e;
		}
	}

	@Override
	public ColfusionSourceinfoDb getStorySourceInfoDB(final int sid) {
		try {

			this.logger.info(String.format("Getting story source into db for sid '%d'", sid));

			HibernateUtil.beginTransaction();

			final SourceInfoDBDAO sourceInfoDBDAO = new SourceInfoDBDAOImpl();

			final ColfusionSourceinfoDb sourceinfoDB = sourceInfoDBDAO.findByID(ColfusionSourceinfoDb.class, sid);

			HibernateUtil.commitTransaction();

			return sourceinfoDB;

		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed HibernateException", ex);
			throw ex;
		}
	}

	@Override
	public ColfusionSourceinfo findStoryByCid(final Integer cid) {
		try {
			HibernateUtil.beginTransaction();

			final Query query = HibernateUtil.getSession().createQuery("SELECT di.colfusionSourceinfo FROM ColfusionDnameinfo di join di.colfusionSourceinfo where di.cid =:cid");
			query.setParameter("cid", cid);

			final ColfusionSourceinfo sourceinfo = this._dao.findOne(query);

			HibernateUtil.commitTransaction();

			return sourceinfo;

		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("findDatasetInfoBySid failed HibernateException", ex);
			throw ex;
		}
	}


	@Override
	public List<RelationKey> getTableNames(final int sid) {
		try {
			HibernateUtil.beginTransaction();

			final SourceInfoDAO storyDao = new SourceInfoDAOImpl();
			final ColfusionSourceinfo story = storyDao.findByID(ColfusionSourceinfo.class, sid);

			final String hql = "SELECT distinct new edu.pitt.sis.exp.colfusion.dal.dataModels.tableDataModel.RelationKey(ti.tableName, ti.dbTableName) FROM ColfusionDnameinfo di join di.colfusionColumnTableInfo ti where di.colfusionSourceinfo =:sid";

			final Query query = HibernateUtil.getSession().createQuery(hql);
			query.setParameter("sid", story);

			final List<RelationKey> tableNames = query.list();

			HibernateUtil.commitTransaction();

			return tableNames;

		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("getTableNames failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("getTableNames failed HibernateException", ex);
			throw ex;
		}
	}

	@Override
	public ColfusionSourceinfoDb getColfusionSourceinfoDbFrom(final int relId) throws Exception {
		final RelationshipsManager relMng = new RelationshipsManagerImpl();
		ColfusionRelationships relationship = relMng.findByID(relId);

		relationship = GeneralManagerImpl.initializeField(relationship, "colfusionSourceinfoBySid1");

		//Hibernate.initialize(relationship.getColfusionSourceinfoBySid1());
		final ColfusionSourceinfo storyFrom = relationship.getColfusionSourceinfoBySid1();

		//storyFrom = GeneralManagerImpl.initializeField(storyFrom, "colfusionSourceinfoDb");

		//Hibernate.initialize(storyFrom.getColfusionSourceinfoDb());

		return storyFrom.getColfusionSourceinfoDb();
	}

	@Override
	public ColfusionSourceinfoDb getColfusionSourceinfoDbTo(final int relId) throws Exception {
		final RelationshipsManager relMng = new RelationshipsManagerImpl();
		ColfusionRelationships relationship = relMng.findByID(relId);

		relationship = GeneralManagerImpl.initializeField(relationship, "colfusionSourceinfoBySid2");

		//Hibernate.initialize(relationship.getColfusionSourceinfoBySid2());
		final ColfusionSourceinfo storyTo = relationship.getColfusionSourceinfoBySid2();

		//storyTo = GeneralManagerImpl.initializeField(storyTo, "colfusionSourceinfoDb");

		//Hibernate.initialize(storyTo.getColfusionSourceinfoDb());

		return storyTo.getColfusionSourceinfoDb();
	}

	//modified by Shruti Sabusuresh
	@Override
	public List<RelationshipsViewModel> getRelationshipsViewModel(
			final int sid, final int userid) {
		try {
			HibernateUtil.beginTransaction();


			//            String hql = "SELECT NEW edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipsViewModel(rel.relId, rel.name, rel.description, relUser.userId, rel.creationTime, relUser.userLogin, sidFrom.sid, sidTo.sid, sidFrom.title, sidTo.title, rel.tableName1, rel.tableName2, st.id.numberOfVerdicts, st.id.numberOfApproved, st.id.numberOfReject, st.id.numberOfNotSure, st.id.avgConfidence) "
			//            		+ "FROM ColfusionRelationships rel join rel.colfusionUsers relUser "
			//            		+ "join rel.colfusionSourceinfoBySid1 sidFrom "
			//            		+ "join rel.colfusionSourceinfoBySid2 sidTo, Statonverdicts st "
			//            		+ "WHERE (rel.colfusionSourceinfoBySid1.sid = :sid or rel.colfusionSourceinfoBySid2.sid = :sid) "
			//            		+ "AND rel.status <> 1 "
			//            		+ "AND rel.relId = st.id.relId";
			//            Query query = HibernateUtil.getSession().createQuery(hql);
			//            query.setParameter("sid", sid);
			//            List<RelationshipsViewModel> result = query.list();

			//TODO: make it work with select new
			final String hql = "SELECT DISTINCT rel.relId , rel.name, rel.description, relUser.userId, rel.creationTime, relUser.userLogin, sidFrom.sid, sidTo.sid, sidFrom.title, sidTo.title, rel.tableName1, rel.tableName2, st.id.numberOfVerdicts, st.id.numberOfApproved, st.id.numberOfReject, st.id.numberOfNotSure, st.id.avgConfidence "
					+ "FROM ColfusionRelationships rel join rel.colfusionUsers relUser "
					+ "join rel.colfusionSourceinfoBySid1 sidFrom right outer join sidFrom.colfusionSourceinfoUsers csuFrom "
					+ "join rel.colfusionSourceinfoBySid2 sidTo right outer join sidTo.colfusionSourceinfoUsers csuTo, Statonverdicts st "
					+ "WHERE ((rel.colfusionSourceinfoBySid1.sid = :sid AND (sidTo.status='"+StoryStatusTypes.QUEUED.getValue()+"' OR (sidTo.status='"+StoryStatusTypes.PRIVATE.getValue()+"' AND csuTo.colfusionUsers.userId="+userid+"))) "
					+ "or (rel.colfusionSourceinfoBySid2.sid = :sid AND (sidFrom.status='"+StoryStatusTypes.QUEUED.getValue()+"' OR (sidFrom.status='"+StoryStatusTypes.PRIVATE.getValue()+"' AND csuFrom.colfusionUsers.userId="+userid+")))) "
					+ "AND rel.status <> 1 "
					+ "AND rel.relId = st.id.relId";

			final Query query = HibernateUtil.getSession().createQuery(hql);
			query.setParameter("sid", sid);

			final List<Object> relationshipsObjs = query.list();
			final List<RelationshipsViewModel> result = new ArrayList<>();
			for (final Object relationshipObj : relationshipsObjs) {
				final Object[] relationshipColumns = (Object[]) relationshipObj;
				final RelationshipsViewModel relationshipViewModel = new RelationshipsViewModel();
				relationshipViewModel.setRelid((Integer)relationshipColumns[0]);
				relationshipViewModel.setName(relationshipColumns[1].toString());
				relationshipViewModel.setDescription(relationshipColumns[2].toString());
				relationshipViewModel.setCreator((Integer)relationshipColumns[3]);
				relationshipViewModel.setCreationTime((Date) relationshipColumns[4]);
				relationshipViewModel.setCreatorLogin( relationshipColumns[5].toString());
				relationshipViewModel.setSidFrom((Integer)relationshipColumns[6]);
				relationshipViewModel.setSidTo((Integer)relationshipColumns[7]);
				//changes by Shruti
				relationshipViewModel.setTitleFrom((String)relationshipColumns[8]);
				relationshipViewModel.setTitleTo((String)relationshipColumns[9]);
				//end of changes by Shruti
				relationshipViewModel.setTableNameFrom(relationshipColumns[10].toString());
				relationshipViewModel.setTableNameTo(relationshipColumns[11].toString());
				relationshipViewModel.setNumberOfVerdicts((long) relationshipColumns[12]);
				relationshipViewModel.setNumberOfApproved( (BigDecimal) relationshipColumns[13]);
				relationshipViewModel.setNumberOfReject( (BigDecimal) relationshipColumns[14]);
				relationshipViewModel.setNumberOfNotSure((BigDecimal) relationshipColumns[15]);
				relationshipViewModel.setAvgConfidence((BigDecimal) relationshipColumns[16]);
				result.add(relationshipViewModel);
			}
			HibernateUtil.commitTransaction();
			return result;
		} catch (final NonUniqueResultException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getMineRelationshipsViewModel failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getMineRelationshipsViewModel failed HibernateException", ex);
			throw ex;
		} catch (final Exception ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getMineRelationshipsViewModel failed Exception", ex);
			throw ex;
		}
	}


	@Override
	public List<StoryListViewModel> getStoryListViewModel(final int pageNo, final int perPage) {
		try{
			HibernateUtil.beginTransaction();
			final String hql = "SELECT src.sid, src.title, cus.userId, cus.userLogin, src.path, src.entryDate, src.lastUpdated, src.status, src.rawDataPath, src.sourceType, cli.licenseId, cli.licenseName, cli.licenseUrl "
					+ "FROM ColfusionSourceinfo src join src.colfusionUsers cus join src.colfusionLicense cli";
			final Query query = HibernateUtil.getSession().createQuery(hql);
			query.setFirstResult((pageNo-1)*perPage);
			query.setMaxResults(perPage);
			final List<Object> storyListObjs = query.list();
			final List<StoryListViewModel> result = new ArrayList<>();
			StoryListToStoryViewModelList(storyListObjs, result);
			return result;

		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("getStoryListViewModel failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("getStoryListViewModel failed HibernateException", ex);
			throw ex;
		}
	}

	@Override
	public List<StoryListViewModel> getStoryListViewModel(final int userid) {
		try{
			HibernateUtil.beginTransaction();
			final String hql = "SELECT DISTINCT src.sid, src.title, src.description, cus.userId, cus.userLogin, src.path, src.entryDate, src.lastUpdated, src.status, src.rawDataPath, src.sourceType, cli.licenseId, cli.licenseName, cli.licenseUrl "
					+ "FROM ColfusionSourceinfo src join src.colfusionUsers cus left outer join src.colfusionLicense cli right outer join src.colfusionSourceinfoUsers csu "
					+ "WHERE (cus.userId="+userid+" AND src.status<>'"+StoryStatusTypes.DRAFT+"') OR src.status='"+StoryStatusTypes.QUEUED.getValue()+"' OR (src.status='"+StoryStatusTypes.PRIVATE.getValue()+"' AND csu.colfusionUsers.userId="+userid+") "
					+ "ORDER BY src.sid DESC";
			final Query query = HibernateUtil.getSession().createQuery(hql);
			final List<Object> storyListObjs = query.list();
			HibernateUtil.commitTransaction();
			final List<StoryListViewModel> result = new ArrayList<>();
			StoryListToStoryViewModelList(storyListObjs, result);
			return result;

		} catch (final NonUniqueResultException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getStoryListViewModel failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getStoryListViewModel failed HibernateException", ex);
			throw ex;
		} catch (final Exception ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getStoryListViewModel failed Exception", ex);
			throw ex;
		}
	}

	@Override
	public List<StoryListViewModel> getStoryListViewModelBySid(final int sid) {
		try{
			HibernateUtil.beginTransaction();
			final String hql = "SELECT src.sid, src.title, cus.userId, cus.userLogin, src.path, src.entryDate, src.lastUpdated, src.status, src.rawDataPath, src.sourceType, cli.licenseId, cli.licenseName, cli.licenseUrl "
					+ "FROM ColfusionSourceinfo src join src.colfusionUsers cus join src.colfusionLicense cli "
					+ "WHERE src.sid =:sid";
			final Query query = HibernateUtil.getSession().createQuery(hql);
			query.setParameter("sid", sid);
			final List<Object> storyListObjs = query.list();
			final List<StoryListViewModel> result = new ArrayList<>();
			StoryListToStoryViewModelList(storyListObjs, result);
			return result;

		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();

			this.logger.error("getStoryListViewModel failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();
			this.logger.error("getStoryListViewModel failed HibernateException", ex);
			throw ex;
		}
	}

	private void StoryListToStoryViewModelList(final List<Object> storyListObjs,
			final List<StoryListViewModel> result) {
		for (final Object storyListObj : storyListObjs) {
			final Object[] storyColumns = (Object[]) storyListObj;
			final StoryListViewModel storyListViewModel = new StoryListViewModel();
			storyListViewModel.setSid((int)storyColumns[0]);
			storyListViewModel.setTitle((String)storyColumns[1]);
			storyListViewModel.setDescription((String)storyColumns[2]);
			final UserViewModel userViewModel = new UserViewModel();
			userViewModel.setUserId((int)storyColumns[3]);
			userViewModel.setUserLogin((String)storyColumns[4]);
			storyListViewModel.setUser(userViewModel);
			storyListViewModel.setPath((String)storyColumns[5]);
			storyListViewModel.setEntryDate((Date)storyColumns[6]);
			storyListViewModel.setLastUpdated((Date)storyColumns[7]);
			storyListViewModel.setStatus((String)storyColumns[8]);
			storyListViewModel.setRawDataPath((String)storyColumns[9]);
			storyListViewModel.setSourceType((String)storyColumns[10]);
			final LicenseViewModel licenseViewModel = new LicenseViewModel();
			//changed by Shruti Sabusuresh
			if(storyColumns[11]!=null){
				licenseViewModel.setLicenseId((int)storyColumns[11]);
				licenseViewModel.setLicenseName((String)storyColumns[12]);
				licenseViewModel.setLicenseURL((String)storyColumns[13]);
			}
			//ended changes
			storyListViewModel.setLicense(licenseViewModel);
			result.add(storyListViewModel);
		}
	}

	@Override
	public List<ColfusionSourceinfo> getSourceInfoByStatus(final SourceInfoStatus status){
		try{
			HibernateUtil.beginTransaction();
			final SourceInfoDAO sourceInfoDAO = new SourceInfoDAOImpl();
			final List<ColfusionSourceinfo> returnList = sourceInfoDAO.findSourceInfoByStatus(status);
			HibernateUtil.commitTransaction();
			return returnList;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();
			this.logger.error("getSourceInfoByStatus failed HibernateException", ex);
			throw ex;
		}
	}

	@Override
	public void doRelationshipMining(final int sid) {
		try{
			HibernateUtil.beginTransaction();
			HibernateUtil.getSession().createSQLQuery("CALL doRelationshipMining('" + sid + "')").executeUpdate();
			HibernateUtil.commitTransaction();

		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();
			this.logger.error("Realitonship mining is failed", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();
			this.logger.error("Realitonship mining is failed HibernateException", ex);
			throw ex;
		}
	}

	/**
	 * Get all DRAFT story list from the database for the user as OWNER and CONTRIBUTOR
	 * @param userid int
	 * @return list of StoryListViewModel
	 * @author Shruti Sabusuresh
	 */
	//TODO test for user as CONTRIBUTOR - how?
	@Override
	public List<StoryListViewModel> getDraftStoryListViewModelByUser(final int userid) {
		try{
			HibernateUtil.beginTransaction();
			final String hql = "SELECT csi.sid, csi.title, csi.description, cu.userId, cu.userLogin, csi.path, csi.entryDate, csi.lastUpdated, csi.status, csi.rawDataPath, csi.sourceType, cl.licenseId, cl.licenseName, cl.licenseUrl "
					+ "FROM ColfusionSourceinfo csi join csi.colfusionUsers cu left outer join csi.colfusionLicense cl right outer join csi.colfusionSourceinfoUsers csu "
					+ "WHERE (cu.userId="+userid+" OR csu.colfusionUsers.userId="+userid+") AND csi.status='"+StoryStatusTypes.DRAFT.getValue()+"' "
					+ "ORDER BY csi.sid DESC";
			final Query query = HibernateUtil.getSession().createQuery(hql);
			final List<Object> storyListObjs = query.list();
			HibernateUtil.commitTransaction();
			final List<StoryListViewModel> result = new ArrayList<>();
			StoryListToStoryViewModelList(storyListObjs, result);
			return result;

		} catch (final NonUniqueResultException ex) {

			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getDraftStoryListViewModelByUser failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {

			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getDraftStoryListViewModelByUser failed HibernateException", ex);
			throw ex;
		}
		catch(final Exception ex){

			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getDraftStoryListViewModelByUser failed Exception", ex);
			throw ex;
		}
	}

	/**
	 * Get all queued story list from the database for the user as OWNER
	 * @param userid int
	 * @return list of StoryListViewModel
	 * @author Shruti Sabusuresh
	 */
	@Override
	public List<StoryListViewModel> getStoryListViewModelByUser(final int userid) {
		try{
			HibernateUtil.beginTransaction();
			final String hql = "SELECT DISTINCT csi.sid, csi.title, csi.description, cu.userId, cu.userLogin, csi.path, csi.entryDate, csi.lastUpdated, csi.status, csi.rawDataPath, csi.sourceType, cl.licenseId, cl.licenseName, cl.licenseUrl "
					+ "FROM ColfusionSourceinfo csi join csi.colfusionUsers cu left outer join csi.colfusionLicense cl right outer join csi.colfusionSourceinfoUsers csu "
					+ "WHERE cu.userId="+userid+" AND csi.status='"+StoryStatusTypes.QUEUED.getValue()+"' "
					+ "ORDER BY csi.sid DESC";
			final Query query = HibernateUtil.getSession().createQuery(hql);
			final List<Object> storyListObjs = query.list();
			HibernateUtil.commitTransaction();
			final List<StoryListViewModel> result = new ArrayList<>();
			StoryListToStoryViewModelList(storyListObjs, result);
			return result;

		} catch (final NonUniqueResultException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getStoryListViewModelByUser failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getStoryListViewModelByUser failed HibernateException", ex);
			throw ex;
		}
		catch(final Exception ex){
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getStoryListViewModelByUser failed Exception", ex);
			throw ex;
		}
	}


	/**
	 * Get all private story list from the database for the user as OWNER
	 * @param userid int
	 * @return list of StoryListViewModel
	 * @author Shruti Sabusuresh
	 */
	@Override
	public List<StoryListViewModel> getPrivateStoryListViewModelByUser(final int userid) {
		try{
			HibernateUtil.beginTransaction();
			final String hql = "SELECT DISTINCT csi.sid, csi.title, csi.description, cu.userId, cu.userLogin, csi.path, csi.entryDate, csi.lastUpdated, csi.status, csi.rawDataPath, csi.sourceType, cl.licenseId, cl.licenseName, cl.licenseUrl "
					+ "FROM ColfusionSourceinfo csi join csi.colfusionUsers cu left outer join csi.colfusionLicense cl right outer join csi.colfusionSourceinfoUsers csu "
					//get all the private dataset which the user created
					+ "WHERE cu.userId="+userid+" AND csi.status='"+StoryStatusTypes.PRIVATE.getValue()+"' "
					+ "ORDER BY csi.sid DESC";
			final Query query = HibernateUtil.getSession().createQuery(hql);
			final List<Object> storyListObjs = query.list();
			HibernateUtil.commitTransaction();
			final List<StoryListViewModel> result = new ArrayList<>();
			StoryListToStoryViewModelList(storyListObjs, result);
			return result;

		} catch (final NonUniqueResultException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getPrivateStoryListViewModelByUser failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getPrivateStoryListViewModelByUser failed HibernateException", ex);
			throw ex;
		}
		catch(final Exception ex){
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getPrivateStoryListViewModelByUser failed HibernateException", ex);
			throw ex;
		}
	}

	/**
	 * Get all story list from the database for the user as CONTRIBUTOR only
	 * @param userid int
	 * @return list of StoryListViewModel
	 * @author Shruti Sabusuresh
	 */
	@Override
	public List<StoryListViewModel> getAllStoryListViewModelSharedToUser(final int userid) {
		try{
			HibernateUtil.beginTransaction();
			final String hql = "SELECT DISTINCT csi.sid, csi.title, csi.description, cu.userId, cu.userLogin, csi.path, csi.entryDate, csi.lastUpdated, csi.status, csi.rawDataPath, csi.sourceType, cl.licenseId, cl.licenseName, cl.licenseUrl "
					+ "FROM ColfusionSourceinfo csi join csi.colfusionUsers cu left outer join csi.colfusionLicense cl right outer join csi.colfusionSourceinfoUsers csu "
					//gets all the queued and private stories where the user is contributor
					+ "WHERE csu.colfusionUsers.userId="+userid+" AND csu.colfusionUserroles.roleId=1 AND (csi.status='"+StoryStatusTypes.QUEUED.getValue()+"' OR csi.status='"+StoryStatusTypes.PRIVATE.getValue()+"') "
					+ "ORDER BY csi.sid DESC";
			final Query query = HibernateUtil.getSession().createQuery(hql);
			final List<Object> storyListObjs = query.list();
			HibernateUtil.commitTransaction();
			final List<StoryListViewModel> result = new ArrayList<>();
			StoryListToStoryViewModelList(storyListObjs, result);
			return result;

		} catch (final NonUniqueResultException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getAllStoryListViewModelSharedToUser failed NonUniqueResultException", ex);
			throw ex;
		} catch (final HibernateException ex) {
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getAllStoryListViewModelSharedToUser failed HibernateException", ex);
			throw ex;
		}
		catch(final Exception ex){
			HibernateUtil.rollbackTransaction();
			this.logger.error(SourceInfoManagerImpl.class.getName(),"getAllStoryListViewModelSharedToUser failed HibernateException: ", ex);
			throw ex;
		}
	}
}