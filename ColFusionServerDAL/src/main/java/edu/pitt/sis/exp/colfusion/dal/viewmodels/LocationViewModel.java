package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

@XmlRootElement
public class LocationViewModel {
	@Expose private String locationIndex;
	@Expose private ArrayList<SourceInfoViewModel> sourceinfoList;
	public String getLocationIndex() {
		return locationIndex;
	}
	public void setLocationIndex(final String locationIndex) {
		this.locationIndex = locationIndex;
	}
	public ArrayList<SourceInfoViewModel> getSourceinfoList() {
		return sourceinfoList;
	}
	public void setSourceinfoList(final ArrayList<SourceInfoViewModel> soureinfosList) {
		this.sourceinfoList = soureinfosList;
	}
}
