/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Evgeny
 *
 */
@XmlRootElement
public class StoryMetadataHistoryViewModel {
	
	private int sid;
	private String historyItem;
	
	private ArrayList<StoryMetadataHistoryLogRecordViewModel> historyLogRecords;
	
	
	public StoryMetadataHistoryViewModel() {
		this.setHistoryLogRecords(new ArrayList<StoryMetadataHistoryLogRecordViewModel>());
	}
	
	public StoryMetadataHistoryViewModel(final int sid, final String historyItem, final ArrayList<StoryMetadataHistoryLogRecordViewModel> historyLogRecords) {
		setSid(sid);
		setHistoryItem(historyItem);
		setHistoryLogRecords(historyLogRecords);
	}


	/**
	 * @return the sid
	 */
	public int getSid() {
		return sid;
	}


	/**
	 * @param sid the sid to set
	 */
	public void setSid(final int sid) {
		this.sid = sid;
	}


	/**
	 * @return the historyItem
	 */
	public String getHistoryItem() {
		return historyItem;
	}


	/**
	 * @param historyItem the historyItem to set
	 */
	public void setHistoryItem(final String historyItem) {
		this.historyItem = historyItem;
	}


	/**
	 * @return the historyLogRecords
	 */
	public ArrayList<StoryMetadataHistoryLogRecordViewModel> getHistoryLogRecords() {
		return historyLogRecords;
	}


	/**
	 * @param historyLogRecords the historyLogRecords to set
	 */
	public void setHistoryLogRecords(final ArrayList<StoryMetadataHistoryLogRecordViewModel> historyLogRecords) {
		this.historyLogRecords = historyLogRecords;
	}
}
