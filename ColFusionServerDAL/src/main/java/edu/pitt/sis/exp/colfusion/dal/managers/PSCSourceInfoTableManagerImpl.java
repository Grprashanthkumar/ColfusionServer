package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.PSCSourceInfoTableDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.PSCSourceInfoTableDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionPscSourceinfoTable;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionPscSourceinfoTableId;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

public class PSCSourceInfoTableManagerImpl extends
		GeneralManagerImpl<PSCSourceInfoTableDAO, ColfusionPscSourceinfoTable, ColfusionPscSourceinfoTableId> implements PSCSourceInfoTableManager {

	Logger logger = LogManager.getLogger(PSCSourceInfoTableManagerImpl.class.getName());
	
	public PSCSourceInfoTableManagerImpl() {
		super(new PSCSourceInfoTableDAOImpl(), ColfusionPscSourceinfoTable.class);
	}
	
	@Override
	public ColfusionPscSourceinfoTable findByID(final ColfusionPscSourceinfoTableId id) throws Exception {
		checkIfDaoSet();
		
		ColfusionPscSourceinfoTable result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = _dao.findByID(ColfusionPscSourceinfoTable.class, id);
            
            Hibernate.initialize(result.getColfusionProcesses());
            Hibernate.initialize(result.getColfusionSourceinfo());
            Hibernate.initialize(result.getColfusionSourceinfo().getColfusionSourceinfoDb());
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed HibernateException", ex);
        	throw ex;
        }
		return result;	
	}
	
	
	@Override
	public List<SourceInfoAndTable> findAllNotReplicated() {
		try{
			HibernateUtil.beginTransaction();
			
			String hql = "SELECT DISTINCT si.sid, ti.dbTableName "
					+ "FROM ColfusionSourceinfo si JOIN si.colfusionDnameinfos dni JOIN dni.colfusionColumnTableInfo ti "
					+ "WHERE NOT EXISTS (SELECT 'anything here' "
					+ "					FROM ColfusionPscSourceinfoTable psc "
					+ "					WHERE psc.id.sid = si.sid AND psc.id.tableName = ti.dbTableName)";
			
			Query query = HibernateUtil.getSession().createQuery(hql);
			
			List<SourceInfoAndTable> result = new ArrayList<SourceInfoAndTable>();
			Iterator<Object[]> iterator = query.iterate();
			while (iterator.hasNext()) {
				Object[] resultTupleColumns = iterator.next();
				
				result.add(new SourceInfoAndTable((int)resultTupleColumns[0], resultTupleColumns[1].toString()));
			}
			
			HibernateUtil.commitTransaction();
			
			return result;
		} catch (HibernateException ex) {
			
	    	HibernateUtil.rollbackTransaction();
	    	
	    	this.logger.error("findAllNotReplicated failed HibernateException", ex);
	    	throw ex;
	    }	
	}
	
	public static class SourceInfoAndTable {
		private final int sourceInfoId;
		private final String tableName;
		
		public SourceInfoAndTable(final int sourceInfoId, final String tableName) {
			this.sourceInfoId = sourceInfoId;
			this.tableName = tableName;
		}

		/**
		 * @return the sourceInfoId
		 */
		public int getSourceInfoId() {
			return sourceInfoId;
		}

		/**
		 * @return the tableName
		 */
		public String getTableName() {
			return tableName;
		}
	}
}
