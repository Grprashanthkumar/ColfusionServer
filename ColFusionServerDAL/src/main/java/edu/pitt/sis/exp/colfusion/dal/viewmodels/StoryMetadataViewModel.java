/**
 *
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StoryMetadataViewModel {
	private int sid;
	private int userId;
	private String title;
	private String description;
	private String status;
	private String sourceType;
	private String tags;
	private Date dateSubmitted;
	private String editReason;
	private int licenseId;

	private StoryAuthorViewModel storySubmitter;
	private ArrayList<StoryAuthorViewModel> storyAuthors;
	private ArrayList<StoryAuthorViewModel> removedStoryAuthors;

	public StoryMetadataViewModel() {
		this.storySubmitter = new StoryAuthorViewModel();
		this.storyAuthors = new ArrayList<StoryAuthorViewModel>();
		this.removedStoryAuthors = new ArrayList<>();
	}

	public StoryMetadataViewModel(final int sid, final int userId, final String title, final String description, final String status, final String sourceType, final String tags, final Date dateSubmitted, final String editReason,
			final StoryAuthorViewModel storySubmitter, final ArrayList<StoryAuthorViewModel> storyAuthors, final ArrayList<StoryAuthorViewModel> removedStoryAuthors,final int licenseId) {
		setSid(sid);
		setUserId(userId);
		setTitle(title);
		setDescription(description);
		setStatus(status);
		setSourceType(sourceType);
		setTags(tags);
		setDateSubmitted(dateSubmitted);
		setEditReason(editReason);
		setStorySubmitter(storySubmitter);
		setStoryAuthors(storyAuthors);
		setRemovedStoryAuthors(removedStoryAuthors);
		setLicenseId(licenseId);
	}


	public void setLicenseId(final int licenseId){
		this.licenseId = licenseId;
	}

	public int getLicenseId(){
		return this.licenseId;
	}
	/**
	 * @return the sid
	 */
	public int getSid() {
		return this.sid;
	}
	/**
	 * @param sid the sid to set
	 */
	public void setSid(final int sid) {
		this.sid = sid;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(final String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return this.status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(final String status) {
		this.status = status;
	}
	/**
	 * @return the source_type
	 */
	public String getSourceType() {
		return this.sourceType;
	}
	/**
	 * @param source_type the source_type to set
	 */
	public void setSourceType(final String source_type) {
		this.sourceType = source_type;
	}
	/**
	 * @return the tags
	 */
	public String getTags() {
		return this.tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(final String tags) {
		this.tags = tags;
	}
	/**
	 * @return the dateSubmitted
	 */
	public Date getDateSubmitted() {
		return this.dateSubmitted;
	}
	/**
	 * @param dateSubmitted the dateSubmitted to set
	 */
	public void setDateSubmitted(final Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}
	/**
	 * @return the storySubmitter
	 */
	public StoryAuthorViewModel getStorySubmitter() {
		return this.storySubmitter;
	}
	/**
	 * @param storySubmitter the storySubmitter to set
	 */
	public void setStorySubmitter(final StoryAuthorViewModel storySubmitter) {
		this.storySubmitter = storySubmitter;
	}
	/**
	 * @return the storyAuthors
	 */
	public ArrayList<StoryAuthorViewModel> getStoryAuthors() {
		return this.storyAuthors;
	}
	/**
	 * @param storyAuthors the storyAuthors to set
	 */
	public void setStoryAuthors(final ArrayList<StoryAuthorViewModel> storyAuthors) {
		this.storyAuthors = storyAuthors;
	}

	/**
	 * @return the removedStoryAuthors
	 */
	public ArrayList<StoryAuthorViewModel> getRemovedStoryAuthors() {
		return this.removedStoryAuthors;
	}

	/**
	 * @param removedStoryAuthors the removedStoryAuthors to set
	 */
	public void setRemovedStoryAuthors(final ArrayList<StoryAuthorViewModel> removedStoryAuthors) {
		this.removedStoryAuthors = removedStoryAuthors;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return this.userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(final int userId) {
		this.userId = userId;
	}

	/**
	 * @return the editReason
	 */
	public String getEditReason() {
		return this.editReason;
	}

	/**
	 * @param editReason the editReason to set
	 */
	public void setEditReason(final String editReason) {
		this.editReason = editReason;
	}

}
