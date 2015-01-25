package edu.pitt.sis.exp.colfusion.tests.bll;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.pitt.sis.exp.colfusion.bll.VisualizationBL;
import edu.pitt.sis.exp.colfusion.dal.infra.DatabaseUnitTestBase;
import edu.pitt.sis.exp.colfusion.dal.managers.CanvasesManager;
import edu.pitt.sis.exp.colfusion.dal.managers.CanvasesManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.ChartsManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ChartsManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCharts;
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
	
	@Test
	public void testFindByName() throws Exception {
		String canvasName = "testCanvas";
		
		VisualizationBL visualizationBL = new VisualizationBL();
		List<ColfusionCanvases> canvasesList = visualizationBL.findByName(canvasName);
		
		assertNotNull(canvasesList);
		
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		List<ColfusionCanvases> actualCanvasesList = canvasMng.findByName(canvasName);
		
		assertEquals("Wrong canvas list", canvasesList, actualCanvasesList);
	}
	
	@Test
	public void testDeleteCanvas() throws Exception{
		
        ColfusionUsers testUser = getTestUser();
		
		String canvasName = "testCanvas";
		
		VisualizationBL visualizationBL = new VisualizationBL();
		ColfusionCanvases canvases = visualizationBL.createCanvase(testUser.getUserId(), canvasName);
		
		assertNotNull(canvases);
		
		visualizationBL.deleteCanvas(canvases.getVid());
		
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		
        assertEquals(null, canvasMng.findByID(canvases.getVid()));
		
		
	}
	
	
	@Test
	public void testCreateNewChart() throws Exception{
		ColfusionUsers testUser = getTestUser();
		
		String canvasName = "newCanvas";
		String chartName = "newChart";
		String chartType = "pie";
		
		VisualizationBL visualizationBL = new VisualizationBL();
		ColfusionCanvases canvas = visualizationBL.createCanvase(testUser.getUserId(), canvasName);
		ColfusionCharts chart = visualizationBL.createChart(canvas.getVid(), chartName, chartType);
		
		assertNotNull(chart);
		
		ChartsManager chartMng = new ChartsManagerImpl();
		ColfusionCharts actualChart = chartMng.findByID(chart.getCid());
		
		assertEquals("Wrong chart name", chart.getName(), actualChart.getName());
	}
}
