package edu.pitt.sis.exp.colfusion.dal.viewmodels;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

/**
 * @author Weichuan Hong
 *
 */

@XmlRootElement
public class LicenseViewModel {
	@Expose private int licenseId;
	@Expose private String licenseName;
	@Expose private String licenseURL;
	@Expose private String licenseDescription;
	
	public LicenseViewModel() {
		
	}
	
	public LicenseViewModel(final Integer licenseId, final String licenseName,
			final String licenseUrl, final String licenseDescription) {
		setLicenseId(licenseId);
		setLicenseName(licenseName);
		setLicenseURL(licenseUrl);
		setLicenseDescription(licenseDescription);
	}

	public int getLicenseId() {
		return licenseId;
	}
	
	public void setLicenseId(final int licenseId) {
		this.licenseId = licenseId;
	}
	
	public String getLicenseName() {
		return licenseName;
	}
	
	public void setLicenseName(final String licenseName) {
		this.licenseName = licenseName;
	}
	
	public String getLicenseURL() {
		return licenseURL;
	}
	
	public void setLicenseURL(final String licenseURL) {
		this.licenseURL = licenseURL;
	}

	public String getLicenseDescription() {
		return licenseDescription;
	}
	
	public void setLicenseDescription(final String licenseDescription) {
		this.licenseDescription = licenseDescription;
	}
}
