package edu.pitt.sis.exp.colfusion.tests.bll;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.VisualizationBL;
import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.dal.managers.CanvasesManager;
import edu.pitt.sis.exp.colfusion.dal.managers.CanvasesManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;

public class VisualizationTest extends DatabaseUnitTestBase{
	@Test
	public void testCreateNewCanvas() throws Exception {
		
		ColfusionUsers testUser = getTestUser();
		
		String canvasName = "testCanvas";
		
		VisualizationBL visualizationBL = new VisualizationBL();
		ColfusionCanvases canvases = visualizationBL.createCanvase(testUser.getUserId(), canvasName);
		
		assertNotNull(canvases);
		
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		ColfusionCanvases actualCanvases = canvasMng.findByID(canvases.getVid());
		
		assertEquals("Wrong canvas name", canvases.getName(), actualCanvases.getName());
	}
}
