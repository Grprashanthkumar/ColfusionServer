package edu.pitt.sis.exp.colfusion.utils;

/**
 * Utility functions to work with strings.
 * @author Evgeny
 *
 */
public final class StringUtils {
	
	/**
	 * This class cannot be instantiated
	 */
	private StringUtils() {	}
	
	/**
	 * Check whether given string value is specified.
	 * @param 	value 
	 * 				the {@link String} value to check
	 * @return
	 * 			true if given value is not null and not empty
	 * 			false if given value is either null or empty
	 */
	public static boolean isSpecified(final String value) {
		return !isNullOrEmpty(value);
	}

	/**
	 * Check whether given string value is null or empty
	 * @param 	value
	 * 				the {@link String} value to check
	 * @return	
	 * 			true if given value is null or empty
	 * 			false if given value is not null and not empty
	 */
	public static boolean isNullOrEmpty(final String value) {
		return value == null || value.isEmpty();
	}
}
