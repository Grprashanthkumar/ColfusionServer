/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.dao;

import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionColumnTableInfo;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
/**
 * @author Evgeny
 *
 */
public class ColumnTableInfoDAOImpl extends GenericDAOImpl<ColfusionColumnTableInfo, Integer> implements
		ColumnTableInfoDAO {

	@Override
	public ColfusionColumnTableInfo findBySidAndOriginalTableName(final int sid,
			final String tableName) {
		String hql = "SELECT t FROM ColfusionColumnTableInfo t join t.colfusionDnameinfo di WHERE t.tableName = :tableName AND di.colfusionSourceinfo.sid = :sid";
		Query query = HibernateUtil.getSession().createQuery(hql);
		
		query.setParameter("sid", sid);
		query.setParameter("tableName", tableName);
		query.setMaxResults(1);
		
		ColfusionColumnTableInfo result = findOne(query);
		
		return result;
	}


}
