package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCharts;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionStory;

public interface ChartsManager extends GeneralManager<ColfusionCharts, Integer>{
	public ColfusionCharts createNewChart(String type, String did, String dname, String tname, String columns, ColfusionStory story);
	public List<ColfusionCharts> showAllByStoryId(ColfusionStory story);
}
