package edu.pitt.sis.exp.colfusion.persistence.orm;

// Generated Feb 17, 2014 8:18:50 PM by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionCharts generated by hbm2java
 */
public class ColfusionCharts implements java.io.Serializable {

	private Integer cid;
	private ColfusionCanvases colfusionCanvases;
	private String name;
	private String type;
	private Integer left;
	private Integer top;
	private Integer depth;
	private Integer height;
	private Integer width;
	private String datainfo;
	private String note;

	public ColfusionCharts() {
	}

	public ColfusionCharts(ColfusionCanvases colfusionCanvases, String name,
			String type, Integer left, Integer top, Integer depth,
			Integer height, Integer width, String datainfo, String note) {
		this.colfusionCanvases = colfusionCanvases;
		this.name = name;
		this.type = type;
		this.left = left;
		this.top = top;
		this.depth = depth;
		this.height = height;
		this.width = width;
		this.datainfo = datainfo;
		this.note = note;
	}

	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public ColfusionCanvases getColfusionCanvases() {
		return this.colfusionCanvases;
	}

	public void setColfusionCanvases(ColfusionCanvases colfusionCanvases) {
		this.colfusionCanvases = colfusionCanvases;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLeft() {
		return this.left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getTop() {
		return this.top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getDepth() {
		return this.depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getDatainfo() {
		return this.datainfo;
	}

	public void setDatainfo(String datainfo) {
		this.datainfo = datainfo;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}