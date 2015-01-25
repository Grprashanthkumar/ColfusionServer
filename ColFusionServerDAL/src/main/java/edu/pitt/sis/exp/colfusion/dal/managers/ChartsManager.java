package edu.pitt.sis.exp.colfusion.dal.managers;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCharts;

public interface ChartsManager extends GeneralManager<ColfusionCharts, Integer>{
	public ColfusionCharts createNewChart(ColfusionCanvases canvas, String name, String type);
}
