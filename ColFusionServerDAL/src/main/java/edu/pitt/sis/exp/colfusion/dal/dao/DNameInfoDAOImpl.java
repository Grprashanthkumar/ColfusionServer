package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDnameinfo;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

public class DNameInfoDAOImpl extends GenericDAOImpl<ColfusionDnameinfo, Integer> implements DNameInfoDAO {

	@Override
	public List<ColfusionDnameinfo> findBySid(final int sid) {
		String hql = "SELECT cdi FROM ColfusionDnameinfo cdi WHERE cdi.colfusionSourceinfo.sid = :sid";
        
        Query query = HibernateUtil.getSession().createQuery(hql);
        query.setParameter("sid", sid);
        List<ColfusionDnameinfo> results = findMany(query);
		return results;
	}

	@Override
	public List<ColfusionDnameinfo> findByCid(final int cid) {
		String hql = "SELECT cdi FROM ColfusionDnameinfo cdi WHERE cdi.cid = :cid";
        
        Query query = HibernateUtil.getSession().createQuery(hql);
        query.setParameter("cid", cid);
        List<ColfusionDnameinfo> results = findMany(query);
		return results;
	}

	

}
