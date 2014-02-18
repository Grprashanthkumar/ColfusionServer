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
public interface SourceInfoDAO extends GenericDAO<ColfusionSourceinfo, BigDecimal> {
	
	/**
	 * From DatasetFinder.php.
	 * 
	 * Finds all source infos which were submitted by given userid.
	 * 
	 * @param userId
	 * @return
	 */
	public List<ColfusionSourceinfo> findDatasetsInfoByUserId(int userId);
}
