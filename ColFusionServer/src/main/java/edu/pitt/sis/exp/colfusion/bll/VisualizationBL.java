package edu.pitt.sis.exp.colfusion.bll;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.managers.CanvasesManager;
import edu.pitt.sis.exp.colfusion.dal.managers.CanvasesManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.ChartsManager;
import edu.pitt.sis.exp.colfusion.dal.managers.ChartsManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.GeneralManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.StoryManager;
import edu.pitt.sis.exp.colfusion.dal.managers.StoryManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManager;
import edu.pitt.sis.exp.colfusion.dal.managers.UserManagerImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCharts;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionStory;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.CanvasViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.ChartViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryViewModel;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.UserViewModel;

public class VisualizationBL {

private UserViewModel convertUsersViewModel(ColfusionUsers users) {
		
		HashSet<CanvasViewModel> canvasVMSet = new HashSet<CanvasViewModel>();

//		try {
//			GeneralManagerImpl.initializeField(users, "canvasSet");
//			for(Canvases canvas: users.getCanvasSet()){
//				CanvasViewModel canvasVM = convertCanvasViewModel(canvas);
//				canvasVMSet.add(canvasVM);
//			}
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}
		
		return new UserViewModel(users.getUserId(),users.getUserNames());
	}
	


	/************************ canvas ************************/
	private CanvasViewModel convertCanvasViewModel(ColfusionCanvases canvas) {
	
		UserViewModel userVM = convertUsersViewModel(canvas.getUsers());
		
		return new CanvasViewModel(canvas.getVid(), canvas.getName(), canvas.getCdate(), 
				canvas.getMdate(), canvas.getPrivilege(), canvas.getNote(), userVM);
	}
	
	
	public CanvasViewModel createCanvase(int userId, String canvasName) throws Exception{
		UserManager userMng = new UserManagerImpl();
		ColfusionUsers user = userMng.findByID(userId);
		
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		ColfusionCanvases newCanvas = canvasMng.createNewCanvas(user, canvasName);
		
//		GeneralManagerImpl.initializeField(user, "canvasSet");
//		user.getCanvasSet().add(newCanvas);
//		userMng.merge(user);
		
		CanvasViewModel canvasVM = convertCanvasViewModel(newCanvas);
		
		return canvasVM;
	}
	

	public List<CanvasViewModel> findCanvasByUser(int userId) throws Exception{
		UserManager userMng = new UserManagerImpl();
		ColfusionUsers user = userMng.findByID(userId);
		
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		List<ColfusionCanvases> canvasesList = canvasMng.findCanvasByUser(user);
		
		Iterator<ColfusionCanvases> itCanvases = canvasesList.iterator();
		List<CanvasViewModel> canvasVMList = new ArrayList<CanvasViewModel>();
		
		while(itCanvases.hasNext()){
			ColfusionCanvases canvas = itCanvases.next();
			
			CanvasViewModel canvasVM = convertCanvasViewModel(canvas);
			
			canvasVMList.add(canvasVM);
		}
		
		return canvasVMList;
		
	}
	
	public int deleteCanvas(int canvasId) throws Exception{
		
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		try {
			canvasMng.delete(canvasMng.findByID(canvasId));
			
			return 1;
		} catch (Exception e){
			
		}
		
		return 0;
		
	}
	

	
	/************************ story ************************/
	private StoryViewModel convertStoryViewModel(ColfusionStory story) {
		CanvasViewModel canvasVM = new CanvasViewModel();
		try {
			story = GeneralManagerImpl.initializeField(story, "canvases");
			ColfusionCanvases canvas = story.getCanvases();
			 canvasVM = convertCanvasViewModel(canvas);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return new StoryViewModel(story.getSid(), story.getDid(), 
				story.getDname(), story.getTname(), canvasVM);
	}
	
	private StoryViewModel convertStoryViewModel2(ColfusionStory story) {
		CanvasViewModel canvasVM = new CanvasViewModel();
		try {
			//story = GeneralManagerImpl.initializeField(story, "canvases");
			ColfusionCanvases canvas = story.getCanvases();
			 canvasVM = convertCanvasViewModel(canvas);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		StoryViewModel storyVM = new StoryViewModel(story.getSid(), story.getDid(), 
				story.getDname(), story.getTname(), canvasVM);
		return storyVM;
	}
	
	public StoryViewModel createStory(String did, String dname, String tname, int canvasId) throws Exception {
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		ColfusionCanvases canvas = canvasMng.findByID(canvasId); 
		
		StoryManager storyMng = new StoryManagerImpl();
		ColfusionStory newStory = storyMng.createNewStory(did, dname, tname, canvas);
		StoryViewModel storyVM = convertStoryViewModel(newStory);
		
		return storyVM;
	}
	
	public List<StoryViewModel> showAllByCanvasId(int canvasId) throws Exception{
		CanvasesManager canvasMng = new CanvasesManagerImpl();
		ColfusionCanvases canvas = canvasMng.findByID(canvasId);
		
		StoryManager storyMng = new StoryManagerImpl();
		List<ColfusionStory> storyList = storyMng.showAllByCanvasId(canvas);
		Iterator<ColfusionStory> itStory = storyList.iterator();
		List<StoryViewModel> storyVMList = new ArrayList<StoryViewModel>();
		
		while(itStory.hasNext()){
			ColfusionStory story = itStory.next();
			
			StoryViewModel storyVM = convertStoryViewModel(story);
			
			storyVMList.add(storyVM);
		}
		
		return storyVMList;
	}
	
	
	public int deleteStory(int storyId) throws Exception{
		StoryManager storyMng = new StoryManagerImpl();
		try {
			storyMng.delete(storyMng.findByID(storyId));
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}
	

	
	/************************ chart ************************/
	public ChartViewModel createChart(int sid, String type, String did, String dname, String tname, String columns) throws Exception{
		
		StoryManager storyMng = new StoryManagerImpl();
		ColfusionStory story = storyMng.findByID(sid);
		
		ChartsManager chartMng = new ChartsManagerImpl();
		ColfusionCharts newChart = chartMng.createNewChart(type, did, dname, tname, columns, story);
		//newChart = GeneralManagerImpl.initializeField(newChart, "canvases"); // lazy
		
		ChartViewModel chartVM = convertChartViewModel(newChart);
		
		return chartVM;
		
	}
	
	public int deleteChart(int chartId) throws Exception{
		
		ChartsManager chartMng = new ChartsManagerImpl();
		try {
			chartMng.delete(chartMng.findByID(chartId));
			return 1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
		
	}
	
	public List<ChartViewModel> showAllByStoryId(int sid) throws Exception{
		StoryManager storyMng = new StoryManagerImpl();
		ChartsManager chartMng = new ChartsManagerImpl();
		ColfusionStory story = storyMng.findByID(sid);
		
		List<ColfusionCharts> chartsList = chartMng.showAllByStoryId(story);
		Iterator<ColfusionCharts> itCharts = chartsList.iterator();
		List<ChartViewModel> chartsVMList = new ArrayList<ChartViewModel>();
		
		while(itCharts.hasNext()){
			ColfusionCharts chart = itCharts.next();
			
			ChartViewModel chartsVM = convertChartViewModel(chart);
			
			chartsVMList.add(chartsVM);
		}
	
		return chartsVMList;
	}
	
	private ChartViewModel convertChartViewModel(ColfusionCharts chart){
	
		ColfusionStory story = chart.getStory();
		StoryViewModel storyVM = convertStoryViewModel2(story);
			
		ChartViewModel chartVM = new ChartViewModel(chart.getCid(), chart.getType(),chart.getDid(), chart.getDname(), chart.getTname(), chart.getColumns(), storyVM);
			
		return chartVM;
	}
}
