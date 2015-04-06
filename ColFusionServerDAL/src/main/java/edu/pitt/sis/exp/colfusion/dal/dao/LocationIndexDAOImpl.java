package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionIndexlocation;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

public class LocationIndexDAOImpl extends GenericDAOImpl<ColfusionIndexlocation, Integer> implements LocationIndexDAO {

	@Override
	public List<ColfusionIndexlocation> findAllLocation() {
		Session session = null;
		List<ColfusionIndexlocation> results = null;
		String hql = "From ColfusionIndexlocation";
        try {
			session = HibernateUtil.getSession();
			session.beginTransaction();
			Query query = session.createQuery(hql);
			results = findMany(query);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		}finally{
			if(session != null){
				if(session.isOpen()){
					session.close();
				}
			}
		}
		return results;
	}

}
