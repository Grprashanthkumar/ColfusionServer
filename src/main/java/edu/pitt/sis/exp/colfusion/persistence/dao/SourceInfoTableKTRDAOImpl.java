/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.util.ArrayList;

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

	@Override
	public ArrayList<ColfusionSourceinfoTableKtr> getKTRLocationsBySid(int sid) {
		String sql = "SELECT stk FROM ColfusionSourceinfoTableKtr stk where stk.colfusionSourceinfo.sid = :sid";
		
		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sid);
		
		return (ArrayList<ColfusionSourceinfoTableKtr>) findMany(query);
	}

	

}
