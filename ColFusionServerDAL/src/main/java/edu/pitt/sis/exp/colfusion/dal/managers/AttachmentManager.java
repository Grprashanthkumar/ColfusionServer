package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDesAttachments;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.AttachmentListViewModel;


/**
 * @author Weichuan
 *
 */
public interface AttachmentManager extends GeneralManager<ColfusionDesAttachments, Integer> {

	/**
	 * Find a Attachment by the sid
	 * @param integer sid.
	 * @return Attachment table info.
	 */
	public List<AttachmentListViewModel> getAttachmentListViewModel(int sid);
	
}
