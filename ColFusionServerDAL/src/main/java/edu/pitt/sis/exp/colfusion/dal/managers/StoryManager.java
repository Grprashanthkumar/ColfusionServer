package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.StoryListViewModel;
/**
 * @author Weichuan
 *
 */
public interface StoryManager extends GeneralManager<ColfusionSourceinfo, Integer> {
	/**
	 * @param int pageNo, int perPage
	 * @return Story list table info.
	 */
	public List<StoryListViewModel> getStoryListViewModel(int pageNo, int perPage);
}
