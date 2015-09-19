package edu.pitt.sis.exp.colfusion.dal.orm;

// Generated 2014-6-10 16:17:59 by Hibernate Tools 3.4.0.CR1

/**
 * ColfusionVisualization generated by hbm2java
 */
public class ColfusionVisualization implements java.io.Serializable {

	private String vid;
	private ColfusionSourceinfo colfusionSourceinfo;
	private String type;
	private int userid;
	private int top;
	private int left;
	private int width;
	private int height;
	private String setting;

	public ColfusionVisualization() {
	}

	public ColfusionVisualization(String vid,
			ColfusionSourceinfo colfusionSourceinfo, String type, int userid,
			int top, int left, int width, int height, String setting) {
		this.vid = vid;
		this.colfusionSourceinfo = colfusionSourceinfo;
		this.type = type;
		this.userid = userid;
		this.top = top;
		this.left = left;
		this.width = width;
		this.height = height;
		this.setting = setting;
	}

	public String getVid() {
		return this.vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public ColfusionSourceinfo getColfusionSourceinfo() {
		return this.colfusionSourceinfo;
	}

	public void setColfusionSourceinfo(ColfusionSourceinfo colfusionSourceinfo) {
		this.colfusionSourceinfo = colfusionSourceinfo;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getUserid() {
		return this.userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getTop() {
		return this.top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getLeft() {
		return this.left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getSetting() {
		return this.setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

}