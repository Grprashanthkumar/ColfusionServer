/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class StoryMetadataHistoryLogRecordViewModel {
	
	private int hid;
	private StoryAuthorViewModel author;
	private Date whenSaved;
	private String item;
	private String reason;
	private String itemValue;
	
	public StoryMetadataHistoryLogRecordViewModel() {
		this.setAuthor(new StoryAuthorViewModel());
	}
	
	public StoryMetadataHistoryLogRecordViewModel(final int hid, final StoryAuthorViewModel author, final Date whenSaved, final String item, final String reason, final String itemValue) {
		setHid(hid);
		setAuthor(author);
		setWhenSaved(whenSaved);
		setItem(item);
		setReason(reason);
		setItemValue(itemValue);
	}

	/**
	 * @return the hid
	 */
	public Integer getHid() {
		return hid;
	}

	/**
	 * @param hid the hid to set
	 */
	public void setHid(final Integer hid) {
		this.hid = hid;
	}

	/**
	 * @return the author
	 */
	public StoryAuthorViewModel getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(final StoryAuthorViewModel author) {
		this.author = author;
	}

	/**
	 * @return the whenSaved
	 */
	public Date getWhenSaved() {
		return whenSaved;
	}

	/**
	 * @param whenSaved the whenSaved to set
	 */
	public void setWhenSaved(final Date whenSaved) {
		this.whenSaved = whenSaved;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(final String item) {
		this.item = item;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(final String reason) {
		this.reason = reason;
	}

	/**
	 * @return the itemValue
	 */
	public String getItemValue() {
		return itemValue;
	}

	/**
	 * @param itemValue the itemValue to set
	 */
	public void setItemValue(final String itemValue) {
		this.itemValue = itemValue;
	}
}
