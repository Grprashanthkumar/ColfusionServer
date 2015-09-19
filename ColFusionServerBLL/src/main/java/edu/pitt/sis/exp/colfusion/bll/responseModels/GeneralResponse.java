package edu.pitt.sis.exp.colfusion.bll.responseModels;

public interface GeneralResponse {
	/**
	 * @return the isSuccessful
	 */
	public abstract boolean isSuccessful();

	/**
	 * @param isSuccessful the isSuccessful to set
	 */
	public abstract void setSuccessful(boolean isSuccessful);

	/**
	 * @return the message
	 */
	public abstract String getMessage();

	/**
	 * @param message the message to set
	 */
	public abstract void setMessage(String message);
}