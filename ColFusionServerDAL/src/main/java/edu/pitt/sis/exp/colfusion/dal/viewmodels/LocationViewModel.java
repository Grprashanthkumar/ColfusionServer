package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

@XmlRootElement
public class LocationViewModel {
	@Expose private String locationIndex;
	@Expose private ArrayList<SourceInfoViewModel> soureinfosList;
	public String getLocationIndex() {
		return locationIndex;
	}
	public void setLocationIndex(final String locationIndex) {
		this.locationIndex = locationIndex;
	}
	public ArrayList<SourceInfoViewModel> getSoureinfosList() {
		return soureinfosList;
	}
	public void setSoureinfosList(final ArrayList<SourceInfoViewModel> soureinfosList) {
		this.soureinfosList = soureinfosList;
	}
}
