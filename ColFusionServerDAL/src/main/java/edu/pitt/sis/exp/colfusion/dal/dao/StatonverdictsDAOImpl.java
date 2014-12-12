package edu.pitt.sis.exp.colfusion.dal.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.pitt.sis.exp.colfusion.dal.orm.Statonverdicts;
import edu.pitt.sis.exp.colfusion.utils.HibernateUtil;

public class StatonverdictsDAOImpl implements StatonverdictsDAO{
	
	//TODO FIXME:
	// 1) put spaces
	// 2) get rid of transactions here, all DAOs should be without transactions. See other examples
	@Override
	public Map<Integer,BigDecimal> getAvgConfidence(){
		Session session = HibernateUtil.getSession();
		String sql = "from Statonverdicts";	
		Query query = null;
		List<Statonverdicts> list; 
		try {
			HibernateUtil.beginTransaction();
			query = session.createQuery(sql);
			list = query.list();
			Map<Integer,BigDecimal> resultMap = new HashMap<Integer,BigDecimal>();
			for (int i = 0; i < list.size(); i++){
				resultMap.put(list.get(i).getId().getRelId(),list.get(i).getId().getAvgConfidence());
			}
			HibernateUtil.commitTransaction();
			return resultMap;
		} catch (Exception e) {
			throw e;
		}
	}
	

	
}
