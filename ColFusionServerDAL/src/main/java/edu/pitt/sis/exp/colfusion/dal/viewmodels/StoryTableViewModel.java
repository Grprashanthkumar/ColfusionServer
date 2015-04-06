package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

@XmlRootElement
public class StoryTableViewModel {
	@Expose private ArrayList<String> ColNames;
	@Expose private ArrayList<ArrayList<String>> Rows;
	public ArrayList<String> getColNames() {
		return ColNames;
	}
	public void setColNames(final ArrayList<String> colNames) {
		ColNames = colNames;
	}
	public ArrayList<ArrayList<String>> getRows() {
		return Rows;
	}
	public void setRows(final ArrayList<ArrayList<String>> rows) {
		Rows = rows;
	}
}
