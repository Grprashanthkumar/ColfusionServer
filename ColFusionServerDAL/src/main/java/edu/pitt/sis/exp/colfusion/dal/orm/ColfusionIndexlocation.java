package edu.pitt.sis.exp.colfusion.dal.orm;



public class ColfusionIndexlocation {
	
	private Integer cid;
	private Integer sid;
	private Integer lid;
	private String locationSearchKey;
	private ColfusionDnameinfo colfusionDnameinfo;
	
	public ColfusionIndexlocation() {
		// TODO Auto-generated constructor stub
	}
	
	public ColfusionIndexlocation(String locationSearchKey,Integer cid,Integer sid){
		this.setCid(cid);
		this.setSid(sid);
		this.setLocationSearchKey(locationSearchKey);
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getLid() {
		return lid;
	}

	public void setLid(Integer lid) {
		this.lid = lid;
	}

	public String getLocationSearchKey() {
		return locationSearchKey;
	}

	public void setLocationSearchKey(String locationSearchKey) {
		this.locationSearchKey = locationSearchKey;
	}

	public ColfusionDnameinfo getColfusionDnameinfo() {
		return colfusionDnameinfo;
	}

	public void setColfusionDnameinfo(ColfusionDnameinfo colfusionDnameinfo) {
		this.colfusionDnameinfo = colfusionDnameinfo;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}
	
	

}
