package edu.pitt.sis.exp.colfusion.utils;

/**
 * Utility functions to work with strings.
 * @author Evgeny
 *
 */
public final class StringUtils {
	
	public static final String SPACE_REPLACEMENT_CHARACTER = "_";
	
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

	/**
	 * Replaces all occurrences of ' ' (space) character with {@value #SPACE_REPLACEMENT_CHARACTER}
	 * 
	 * Two or more consecutive spaces are replaces with only one {@value #SPACE_REPLACEMENT_CHARACTER}
	 * 
	 * If null value is provided as input, then null will be returned.
	 * 
	 * @param 	value
	 * 				the {@link String} in which to do the replacements
	 * @return
	 * 			{@link String} with all spaces replaced with {@value #SPACE_REPLACEMENT_CHARACTER}
	 * 			null if input was null
	 */
	public static String replaceSpaces(final String value) {
		return value == null ? null : value.replaceAll(" +", SPACE_REPLACEMENT_CHARACTER);
	}
}
