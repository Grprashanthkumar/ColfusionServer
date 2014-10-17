package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDesAttachments;
/**
 * @author Weichuan
 *
 */
public interface AttachmentDAO extends GenericDAO<ColfusionDesAttachments, Integer>{
	/**
	 * 
	 * Finds all datasets (source infos) which were submitted by given sid.
	 * 
	 * @param sid by which the search should be performed.
	 * @return all found datasets which conform to the search ColfusionDesAttachments. 
	 */
	public List<ColfusionDesAttachments> findBySid(int sid);
}
