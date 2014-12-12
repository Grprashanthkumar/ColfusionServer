package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.dal.dao.ColumnTableInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.ColumnTableInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionColumnTableInfo;
import edu.pitt.sis.exp.colfusion.utils.HibernateUtil;

public class ColumnTableInfoManagerImpl extends GeneralManagerImpl<ColumnTableInfoDAO, ColfusionColumnTableInfo, Integer> implements ColumnTableInfoManager {
	
	Logger logger = LogManager.getLogger(AttachmentManagerImpl.class.getName());
	
	public ColumnTableInfoManagerImpl() {
		super(new ColumnTableInfoDAOImpl(), ColfusionColumnTableInfo.class);
	}

	@Override
	public ColfusionColumnTableInfo findBySidAndOriginalTableName(final int sid,
			final String tableName) {
		try{
			HibernateUtil.beginTransaction();
	        
			ColfusionColumnTableInfo result = _dao.findBySidAndOriginalTableName(sid, tableName);
	        
	        HibernateUtil.commitTransaction();
	        
	        return result;
	    } catch (NonUniqueResultException ex) {
	
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("getAttachmentListViewModel failed NonUniqueResultException", ex);
	        throw ex;
	    } catch (HibernateException ex) {
	
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("getAttachmentListViewModel failed HibernateException", ex);
	    	throw ex;
	    }	
	}

}
