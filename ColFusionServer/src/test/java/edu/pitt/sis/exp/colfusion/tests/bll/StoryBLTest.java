package edu.pitt.sis.exp.colfusion.tests.bll;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.StoryBL;
import edu.pitt.sis.exp.colfusion.responseModels.StoryMetadataResponse;

public class StoryBLTest {

	@Test
	public void testGetStoryMetadata() {
		int sid=1751;
		StoryMetadataResponse result=new StoryMetadataResponse();
		StoryBL storyBL=new StoryBL();
		result=storyBL.getStoryMetadata(sid);
		System.out.println(result.getPayload());
		try {
			
			
			assertEquals(true, result.isSuccessful);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			fail(e.getMessage());
		}	
	}

}
