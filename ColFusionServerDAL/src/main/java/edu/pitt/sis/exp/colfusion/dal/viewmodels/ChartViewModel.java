package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionStory;

@XmlRootElement
public class ChartViewModel {
	private Integer cid;
	private StoryViewModel storyVM;
	private String name;
	private String type;
	private String did;
	private String dname;
	private String tname;
	private String columns;
	
	
	public ChartViewModel() {
		
	}
	
	
	public ChartViewModel(Integer cid, String type, String did, String dname, String tname, String columns, StoryViewModel storyVM){
		this.cid = cid;
		this.type = type;
		this.did = did;
		this.dname = dname;
		this.tname = tname;
		this.columns = columns;
		this.storyVM = storyVM;
	}
	
	public ChartViewModel(Integer cid, StoryViewModel storyVM, String name, String type,
			String did, String dname, String tname, String columns) {
		super();
		this.cid = cid;
		this.storyVM = storyVM;
		this.name = name;
		this.type = type;
		this.did = did;
		this.dname = dname;
		this.tname = tname;
		this.columns = columns;
	}

	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public StoryViewModel getStoryVM() {
		return storyVM;
	}
	public void setStoryVM(StoryViewModel storyVM) {
		this.storyVM = storyVM;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
 

}
