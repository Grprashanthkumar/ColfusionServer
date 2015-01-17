package edu.pitt.sis.exp.colfusion.visualization;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class createNewCanvasTest {
	
	public String vid = "";
	public String canvasName = "";
	public String owner = "";
	public int authorization, privilege = 0;
	public Date cdate = new Date();
	
	Canvas result;
	Canvas test;

	@Before
	public void setUp() throws Exception {
		result = new Canvas(vid, canvasName, owner, cdate, privilege, authorization);
	}

	@Test
	public void test() {
		test = result.createNewCanvas(vid, canvasName, owner, cdate, privilege, authorization);
		assertNotNull("should not be null", result);
		assertNotNull("should not be null", test);
	}

}
