/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.math.BigDecimal;
import java.util.List;

import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;

/**
 * @author Evgeny
 *
 */
public class SourceInfoDAOImpl extends GenericDAOImpl<ColfusionSourceinfo, BigDecimal> implements SourceInfoDAO {

	@Override
	public List<ColfusionSourceinfo> findDatasetsInfoByUserId(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColfusionSourceinfo> findDatasetInfoBySidOrTitle(
			String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ColfusionSourceinfo findDatasetInfoBySid(int sid, boolean includeDraft) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ColfusionSourceinfo> findDatasetsInfoByTitle(String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

}
