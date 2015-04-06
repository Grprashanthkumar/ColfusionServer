package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

@XmlRootElement
public class SourceInfoViewModel {
	@Expose private int sid;
	@Expose private String title;
	public int getSid() {
		return sid;
	}
	public void setSid(final int sid) {
		this.sid = sid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}
	
}
