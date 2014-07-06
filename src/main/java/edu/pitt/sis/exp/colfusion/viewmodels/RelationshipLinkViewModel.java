package edu.pitt.sis.exp.colfusion.viewmodels;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RelationshipLinkViewModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fromPart;
	private String toPart;
	private double fromRatio;
	private double toRatio;

	public RelationshipLinkViewModel() {
		
	}
	
	public RelationshipLinkViewModel(final String fromPart, final String toPart, final double fromRatio, final double toRatio) {
		this.setFromPart(fromPart);
		this.setToPart(toPart);
		this.setFromRatio(fromRatio);
		this.setToRatio(toRatio);
	}

	/**
	 * @return the fromPart
	 */
	public String getFromPart() {
		return fromPart;
	}

	/**
	 * @param fromPart the fromPart to set
	 */
	public void setFromPart(final String fromPart) {
		this.fromPart = fromPart;
	}

	/**
	 * @return the toPart
	 */
	public String getToPart() {
		return toPart;
	}

	/**
	 * @param toPart the toPart to set
	 */
	public void setToPart(final String toPart) {
		this.toPart = toPart;
	}

	/**
	 * @return the fromRatio
	 */
	public double getFromRatio() {
		return fromRatio;
	}

	/**
	 * @param fromRatio the fromRatio to set
	 */
	public void setFromRatio(final double fromRatio) {
		this.fromRatio = fromRatio;
	}

	/**
	 * @return the toRation
	 */
	public double getToRatio() {
		return toRatio;
	}

	/**
	 * @param toRation the toRation to set
	 */
	public void setToRatio(final double toRatio) {
		this.toRatio = toRatio;
	}
	
}
