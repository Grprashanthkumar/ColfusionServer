package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfo;

public interface DNameInfoDAO extends GenericDAO<ColfusionDnameinfo, Integer> {

	/**
	 * @param int sid
	 * @return column table info.
	 */
	public List<ColfusionDnameinfo> findBySid(int sid);
	
	public List<ColfusionDnameinfo> findBySid(int sid, String tableName);
	
	/**
	 * @param int cid
	 * @return column table info.
	 */
	public List<ColfusionDnameinfo> findByCid(int cid);
}
