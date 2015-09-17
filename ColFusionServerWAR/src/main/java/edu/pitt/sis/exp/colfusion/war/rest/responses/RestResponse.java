package edu.pitt.sis.exp.colfusion.war.rest.responses;

import edu.pitt.sis.exp.colfusion.utils.Gsonazable;

public interface RestResponse<T> extends Gsonazable {
	public T getPayload();

	public boolean isSuccessful();

	public String userMessage();
}
