/**
 * 
 */
package edu.pitt.sis.exp.colfusion.viewmodels;

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
	
	public StoryMetadataHistoryViewModel(int sid, String historyItem, ArrayList<StoryMetadataHistoryLogRecordViewModel> historyLogRecords) {
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
	public void setSid(int sid) {
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
	public void setHistoryItem(String historyItem) {
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
	public void setHistoryLogRecords(ArrayList<StoryMetadataHistoryLogRecordViewModel> historyLogRecords) {
		this.historyLogRecords = historyLogRecords;
	}
}
