package edu.pitt.sis.exp.colfusion.war.rest.responses;

import edu.pitt.sis.exp.colfusion.utils.Gsonizer;

public class RestResponseImpl<T> implements RestResponse<T> {

	private final T payload;
	private boolean isSuccessful = false;
	private String userMessage = "";

	public RestResponseImpl(final T payload) {
		this.payload = payload;
	}

	public RestResponseImpl(final T payload, final boolean isSuccessful, final String userMessage) {
		this.payload = payload;
		this.isSuccessful = isSuccessful;
		this.userMessage = userMessage;
	}

	@Override
	public T getPayload() {
		return this.payload;
	}

	@Override
	public boolean isSuccessful() {
		return this.isSuccessful;
	}

	@Override
	public String userMessage() {
		return this.userMessage;
	}

	@Override
	public String toJson() {
		return Gsonizer.toJson(this, false);
	}

	@Override
	public void fromJson() {

	}
}
