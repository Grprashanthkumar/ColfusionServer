package edu.pitt.sis.exp.colfusion.dal.orm;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ColfusionStory {
	private Integer sid;
	private Date cdate;
	private String did;
	private String dname;
	private String tname;
	private ColfusionCanvases canvases;
	
	private Set<ColfusionCharts> chartSet = new HashSet(0);
	
	public ColfusionStory(){
		
	}
	
	public ColfusionStory(Date cdate){
		this.cdate = cdate;
	}
	
	public ColfusionStory(String did, String dname, String tname, ColfusionCanvases canvas) {
		super();
		this.did = did;
		this.dname = dname;
		this.tname = tname;
		this.canvases = canvas;
	}
	
	public ColfusionStory(Integer sid, Date cdate, String did, String dname,
			String tname, ColfusionCanvases canvas, Set<ColfusionCharts> chartSet) {
		super();
		this.sid = sid;
		this.cdate = cdate;
		this.did = did;
		this.dname = dname;
		this.tname = tname;
		this.canvases = canvas;
		this.chartSet = chartSet;
	}
	
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public Date getCdate() {
		return cdate;
	}
	public void setCdate(Date cdate) {
		this.cdate = cdate;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public ColfusionCanvases getCanvases() {
		return canvases;
	}
	public void setCanvases(ColfusionCanvases canvas) {
		this.canvases = canvas;
	}

	public Set<ColfusionCharts> getChartSet() {
		return chartSet;
	}

	public void setChartSet(Set<ColfusionCharts> chartSet) {
		this.chartSet = chartSet;
	}

}
