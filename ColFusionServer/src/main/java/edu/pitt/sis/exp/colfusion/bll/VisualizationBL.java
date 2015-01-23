package edu.pitt.sis.exp.colfusion.bll;


import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.managers.CanvasesManager;
import edu.pitt.sis.exp.colfusion.dal.managers.CanvasesManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManager;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;

public class VisualizationBL {

	public ColfusionCanvases createCanvase(int userId, String canvasName) throws Exception{
		UserManager userMng = new UserManagerImpl();
		ColfusionUsers user = userMng.findByID(userId);
		
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		ColfusionCanvases newCanvas = canvasMng.createNewCanvas(user, canvasName);
	   
		return newCanvas;
	}
	
	public List<ColfusionCanvases> findByName(String name) throws Exception{
		
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		List<ColfusionCanvases> canvasesList = canvasMng.findByName(name);
		
		return canvasesList;
	}
}
