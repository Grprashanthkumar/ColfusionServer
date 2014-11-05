package edu.pitt.sis.exp.colfusion.utils;

import junit.framework.TestCase;

import org.junit.Test;

public class StringUtilsTest extends TestCase {
	
	@Test
	public void testIsSpecified() {
		assertEquals(false, StringUtils.isSpecified(""));
		assertEquals(false, StringUtils.isSpecified(null));
		
		assertEquals(true, StringUtils.isSpecified("abc"));
	}
	
	@Test
	public void testIsNullOrEmpty() {
		assertEquals(true, StringUtils.isNullOrEmpty(""));
		assertEquals(true, StringUtils.isNullOrEmpty(null));
		
		assertEquals(false, StringUtils.isNullOrEmpty("abc"));
	}
	
	@Test
	public void testReplaceSpaces() {
		assertEquals("", StringUtils.replaceSpaces(""));
		assertEquals("abc", StringUtils.replaceSpaces("abc"));
		
		assertEquals("a_b_c", StringUtils.replaceSpaces("a b c"));
		
		assertEquals("a_b_c", StringUtils.replaceSpaces("a      b c"));
		
		assertEquals("_a_b_c_", StringUtils.replaceSpaces(" a      b c  "));
		assertEquals("_a___b_c_", StringUtils.replaceSpaces(" a  _   b c  "));
	}
}
