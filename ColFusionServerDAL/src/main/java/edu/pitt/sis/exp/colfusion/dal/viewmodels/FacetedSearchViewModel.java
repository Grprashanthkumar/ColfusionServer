package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

@XmlRootElement
public class FacetedSearchViewModel {
	@Expose private String datasetName;
	@Expose private int sid;
	@Expose private ArrayList<String> locationList;

	public int getSid() {
		return sid;
	}

	public void setSid(final int sid) {
		this.sid = sid;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(final String datasetName) {
		this.datasetName = datasetName;
	}

	public ArrayList<String> getLocationList() {
		return locationList;
	}

	public void setLocationList(final ArrayList<String> locationList) {
		this.locationList = locationList;
	}
	
}
