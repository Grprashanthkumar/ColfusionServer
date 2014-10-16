package edu.pitt.sis.exp.colfusion.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StreamUtils {
	
	private static final String STRING_ENCODING_UTF8 = "UTF-8";
	
	public static InputStream fromString(final String content) {
		return fromString(content, STRING_ENCODING_UTF8);
	}
	
	public static InputStream fromString(final String content, final String encoding) {
		if (content == null) {
			throw new IllegalArgumentException("Content cannot be null");
		}
		
		try {
			return new ByteArrayInputStream(content.getBytes(encoding));
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
