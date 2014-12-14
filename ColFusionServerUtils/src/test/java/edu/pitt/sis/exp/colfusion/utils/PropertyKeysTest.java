package edu.pitt.sis.exp.colfusion.utils;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

public class PropertyKeysTest {
	
	@Test
	public void testGetPropertiesForPrefixInternal() {
		{
			Set<PropertyKeys> propForPrefix = PropertyKeys.getPropertiesForPrefix("colfusion");
		
			// The expectation is that all properties have colfusion prefix
			assertEquals("Wrong # of properties that has specified prefix", PropertyKeys.values().length, propForPrefix.size());
		}
		
		{
			Set<PropertyKeys> propForPrefix = PropertyKeys.getPropertiesForPrefix("");
		
			// If not prefix is specified then all properties shuould be returned
			assertEquals("Wrong # of properties that has specified prefix", PropertyKeys.values().length, propForPrefix.size());
		}
		
		{
			Set<PropertyKeys> propForPrefix = PropertyKeys.getPropertiesForPrefix("definatelly not existing prefix");
		
			// If not prefix is specified then all properties shuould be returned
			assertEquals("Wrong # of properties that has specified prefix", 0, propForPrefix.size());
		}
	}
}
