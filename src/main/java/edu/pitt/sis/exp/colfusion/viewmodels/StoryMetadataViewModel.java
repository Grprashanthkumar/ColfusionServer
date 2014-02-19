/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

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
	private String source_type;
	private String tags;
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
	public String getSource_type() {
		return source_type;
	}
	/**
	 * @param source_type the source_type to set
	 */
	public void setSource_type(String source_type) {
		this.source_type = source_type;
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
}
