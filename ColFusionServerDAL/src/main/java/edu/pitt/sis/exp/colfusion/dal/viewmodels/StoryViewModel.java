package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCharts;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;

@XmlRootElement
public class StoryViewModel {
	private Integer sid;
	private Date cdate;
	private String did;
	private String dname;
	private String tname;
	private CanvasViewModel canvasVM;
	
	private Set<ChartViewModel> chartVMSet = new HashSet(0);
	
	public StoryViewModel(){
		
	}
	
	public StoryViewModel(Integer sid, String did, String dname, String tname, CanvasViewModel canvasVM) {
		super();
		this.sid = sid;
		this.did = did;
		this.dname = dname;
		this.tname = tname;
		this.canvasVM = canvasVM;
	}
	
	public StoryViewModel(int sid, Date cdate, String did, String dname,
			String tname, CanvasViewModel canvasVM, Set<ChartViewModel> chartVMSet) {
		super();
		this.sid = sid;
		this.cdate = cdate;
		this.did = did;
		this.dname = dname;
		this.tname = tname;
		this.canvasVM = canvasVM;
		this.chartVMSet = chartVMSet;
	}
	
	public int getSid() {
		return sid;
	}
	public void setSid(final int sid) {
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
	public CanvasViewModel getCanvasVM() {
		return canvasVM;
	}
	public void setCanvasVM(CanvasViewModel canvasVM) {
		this.canvasVM = canvasVM;
	}

	public Set<ChartViewModel> getChartVMSet() {
		return chartVMSet;
	}

	public void setChartVMSet(Set<ChartViewModel> chartVMSet) {
		this.chartVMSet = chartVMSet;
	}
}
