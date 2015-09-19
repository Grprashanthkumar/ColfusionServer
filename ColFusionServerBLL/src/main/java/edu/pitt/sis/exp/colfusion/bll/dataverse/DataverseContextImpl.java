package edu.pitt.sis.exp.colfusion.bll.dataverse;

public class DataverseContextImpl implements DataverseContext {

	//TODO:figure out if this is always like this, or it should be provided by user
	private final String PROTOCOL = "https";
	private final String API_BASE = "api";

	private final String serverAddress;
	private final String tokenKey;

	public DataverseContextImpl(final String serverAddress, final String tokenKey) {
		this.serverAddress = serverAddress;
		this.tokenKey = tokenKey;
	}

	@Override
	public String getRestApiBase() {
		return String.format("%s://%s/%s", this.PROTOCOL, this.serverAddress, this.API_BASE);
	}

	@Override
	public String getTokenKey() {
		return this.tokenKey;
	}
}
