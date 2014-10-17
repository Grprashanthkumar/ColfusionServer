package edu.pitt.sis.exp.colfusion.dal.dao;

import java.util.List;

import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionDesAttachments;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

public class AttachmentDAOImpl extends GenericDAOImpl<ColfusionDesAttachments, Integer> implements AttachmentDAO {

	@Override
	public List<ColfusionDesAttachments> findBySid(int sid) {
		String hql = "SELECT attachment FROM ColfusionDesAttachments attachment WHERE attachment.colfusionSourceinfo.sid = :sid";
        
        Query query = HibernateUtil.getSession().createQuery(hql);
        query.setParameter("sid", sid);
        List<ColfusionDesAttachments> results = findMany(query);
		return results;
	}

}
