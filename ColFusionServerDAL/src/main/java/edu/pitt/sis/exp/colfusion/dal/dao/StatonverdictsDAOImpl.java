package edu.pitt.sis.exp.colfusion.dal.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.orm.Statonverdicts;
import edu.pitt.sis.exp.colfusion.dal.orm.StatonverdictsId;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

public class StatonverdictsDAOImpl implements StatonverdictsDAO{
	
	public Map<Integer,BigDecimal> getAvgConfidence(){
		Session session = HibernateUtil.getSession();
		String sql = "from Statonverdicts";	
		Query query = null;
		List<Statonverdicts> list; 
		try {
			HibernateUtil.beginTransaction();
			query = session.createQuery(sql);
			list = query.list();
			System.out.println("***"+list.size()+"****");
			Map<Integer,BigDecimal> resultMap = new HashMap<Integer,BigDecimal>();
			for (int i = 0;i<list.size();i++){
				System.out.println("-----"+list.get(i).getId().getRelId()+"----");
				resultMap.put(list.get(i).getId().getRelId(),list.get(i).getId().getAvgConfidence());
				System.out.println("----*"+list.get(i).getId().getAvgConfidence().toString()+"---*");
			}
			return resultMap;
		} catch (Exception e) {
			throw e;
		}
	}
	

	
}
