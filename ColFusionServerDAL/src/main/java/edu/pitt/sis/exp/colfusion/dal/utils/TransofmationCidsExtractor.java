package edu.pitt.sis.exp.colfusion.dal.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransofmationCidsExtractor {

	static Logger logger = LogManager.getLogger(TransofmationCidsExtractor.class.getName());
	
	private static final Pattern pattern = Pattern.compile("(\\()(\\d+)(\\))");
	
	public static Matcher match(final String transformationInCids) {
		return pattern.matcher(transformationInCids);
	}
	
	/**
	 * Extracts column ids from column transformation strings (column ids are supposed to be wrapped in cid(...));
	 * @param transformation the transformation string.
	 * @return list of ids extracted from the transformation string.
	 */
	public static List<Integer> extractCids(final String transformationInCids) {
				
		Matcher matcher = TransofmationCidsExtractor.match(transformationInCids);
		
		List<Integer> result = new ArrayList<>();
		
		try {
			while (matcher.find()) {
				result.add(Integer.parseInt(matcher.group(2)));
			}
			
			return result;
		}
		catch(Exception e) {
			logger.error(String.format("Could not extract cids from this string: %s", transformationInCids));
			throw e;
		}	
	}
}
