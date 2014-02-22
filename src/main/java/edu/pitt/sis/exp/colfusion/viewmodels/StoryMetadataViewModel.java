/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class StoryMetadataViewModel {
	private int sid;
	private String title;
	private String description;
	private String status;
	private String sourceType;
	private String tags;
	private Date dateSubmitted;
	
	private StoryAuthorViewModel storySubmitter;
	private ArrayList<StoryAuthorViewModel> storyAuthors;
	
	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}
	/**
	 * @param sid the sid to set
	 */
	public void setSid(int sid) {
		this.sid = sid;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the source_type
	 */
	public String getSourceType() {
		return sourceType;
	}
	/**
	 * @param source_type the source_type to set
	 */
	public void setSourceType(String source_type) {
		this.sourceType = source_type;
	}
	/**
	 * @return the tags
	 */
	public String getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}
	/**
	 * @return the dateSubmitted
	 */
	public Date getDateSubmitted() {
		return dateSubmitted;
	}
	/**
	 * @param dateSubmitted the dateSubmitted to set
	 */
	public void setDateSubmitted(Date dateSubmitted) {
		this.dateSubmitted = dateSubmitted;
	}
	/**
	 * @return the storySubmitter
	 */
	public StoryAuthorViewModel getStorySubmitter() {
		return storySubmitter;
	}
	/**
	 * @param storySubmitter the storySubmitter to set
	 */
	public void setStorySubmitter(StoryAuthorViewModel storySubmitter) {
		this.storySubmitter = storySubmitter;
	}
	/**
	 * @return the storyAuthors
	 */
	public ArrayList<StoryAuthorViewModel> getStoryAuthors() {
		return storyAuthors;
	}
	/**
	 * @param storyAuthors the storyAuthors to set
	 */
	public void setStoryAuthors(ArrayList<StoryAuthorViewModel> storyAuthors) {
		this.storyAuthors = storyAuthors;
	}
	
}
