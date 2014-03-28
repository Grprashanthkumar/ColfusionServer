/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtr;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfoTableKtrId;

/**
 * @author Evgeny
 *
 */
public class SourceInfoTableKTRDAOImpl extends GenericDAOImpl<ColfusionSourceinfoTableKtr, ColfusionSourceinfoTableKtrId> implements
		SourceInfoTableKTRDAO {

	Logger logger = LogManager.getLogger(SourceInfoTableKTRDAOImpl.class.getName());
	
	@Override
	public ArrayList<ColfusionSourceinfoTableKtr> getKTRLocationsBySid(int sid) throws Exception {
		String sql = "SELECT stk FROM ColfusionSourceinfoTableKtr stk where stk.colfusionSourceinfo.sid = :sid";
		
		Query query = null;
		try {
			query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sid);
		} catch (Exception e) {
			logger.error(String.format("getKTRLocationsBySid failed on HibernateUtil.getSession().createQuery(sql).setParameter(sid, sid); for sid = %d", sid), e);
			
			throw e;
		}
		
		return (ArrayList<ColfusionSourceinfoTableKtr>) findMany(query);
	}

	

}
