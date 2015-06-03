package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class CanvasViewModel {
   
	private Integer vid;
	private String name;
	private Date cdate;
	private Date mdate;
	private Integer privilege;
	private String note;
	private UserViewModel user;
	
	public CanvasViewModel() {
		
	}
	
	public CanvasViewModel(Integer vid, String name, Date cdate, Date mdate, Integer privilege, String note, UserViewModel user) {
		this.vid = vid;
		this.name = name;
		this.cdate = cdate;
		this.mdate = mdate;
		this.privilege = privilege != null ? privilege : 0;
		this.note = note;
		this.user = user;
	}
	
	public int getVid(){
		return vid;
	}
	
	public void setVid(final int vid){
		this.vid = vid;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(final String name){
		this.name = name;
	}
	
	public Date getCdate(){
		return cdate;
	}
	
	public void setCdate(final Date cdate){
		this.cdate = cdate;
	}
	
	public Date getMdate(){
		return mdate;
	}
	
	public void setMdate(final Date mdate){
		this.mdate = mdate;
	}
	
	public int getPrivilege(){
		return privilege;
	}
	
	public void setPrivilege(final int privilege){
		this.privilege = privilege;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public UserViewModel getUser() {
		return user;
	}

	public void setUser(UserViewModel user) {
		this.user = user;
	}
}
