package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionStory;

public interface StoryManager extends GeneralManager<ColfusionStory, Integer> {

	   public ColfusionStory createNewStory(String did, String dname, String tname, ColfusionCanvases canvases);
	   public List<ColfusionStory> showAllByCanvasId(ColfusionCanvases canvses); //add, may need to delete
	   

	}