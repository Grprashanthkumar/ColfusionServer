/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.RelationshipsDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.SourceInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumns;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionSourceinfo;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;
import edu.pitt.sis.exp.colfusion.dal.viewmodels.RelationshipLinkViewModel;

/**
 * @author Evgeny
 *
 */
public class RelationshipsManagerImpl extends GeneralManagerImpl<ColfusionRelationships, Integer> implements RelationshipsManager {

	Logger logger = LogManager.getLogger(RelationshipsManagerImpl.class.getName());
	
	public RelationshipsManagerImpl() {
		super(new RelationshipsDAOImpl(), ColfusionRelationships.class);
	}

	@Override
	public List<ColfusionRelationships> findByTables(final int sid1,
			final String tableName1, final int sid2, final String tableName2) throws Exception {
		
		try {
			
			this.checkIfDaoSet();
			
            HibernateUtil.beginTransaction();
            
            String sql = "select r from ColfusionRelationships r where r.colfusionSourceinfoBySid1 = :sid1 "
            		+ "and r.colfusionSourceinfoBySid2 = :sid2 and r.tableName1 = :tableName1 and r.tableName2 = :tableName2";
            		
            SourceInfoDAO storyDao = new SourceInfoDAOImpl();
            
            ColfusionSourceinfo sourceInfo1 = storyDao.findByID(ColfusionSourceinfo.class, sid1);
            ColfusionSourceinfo sourceInfo2 = storyDao.findByID(ColfusionSourceinfo.class, sid2);
            Query query = HibernateUtil.getSession().createQuery(sql)
            		.setParameter("sid1", sourceInfo2)
            		.setParameter("sid2", sourceInfo1)
            		.setParameter("tableName1", tableName2)
            		.setParameter("tableName2", tableName1);
           
            
            List<ColfusionRelationships> result = _dao.findMany(query);
            
            for (ColfusionRelationships colfusionRelationships : result) {
            	Hibernate.initialize(colfusionRelationships.getColfusionRelationshipsColumnses());
			}
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed HibernateException", ex);
        	throw ex;
        } catch (Exception e) {
        	//HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed HibernateException", e);
        	throw e;
		}
	}
	
	@Override
	public ColfusionRelationships findByID(final Integer id) throws Exception {
		try {
			
			this.checkIfDaoSet();
			
            HibernateUtil.beginTransaction();
            
           
            ColfusionRelationships result = _dao.findByID(ColfusionRelationships.class, id);
            
            if (result != null) {
            	Hibernate.initialize(result.getColfusionRelationshipsColumnses());
            	Hibernate.initialize(result.getColfusionSourceinfoBySid1());
            	Hibernate.initialize(result.getColfusionSourceinfoBySid2());
            	
            	Hibernate.initialize(result.getColfusionSourceinfoBySid1().getColfusionSourceinfoDb());
            	Hibernate.initialize(result.getColfusionSourceinfoBySid2().getColfusionSourceinfoDb());
            }
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed HibernateException", ex);
        	throw ex;
        } catch (Exception e) {
        	//HibernateUtil.rollbackTransaction();
        	
        	logger.error("findByID failed HibernateException", e);
        	throw e;
		}
	}

	@Override
	public List<RelationshipLinkViewModel> getRelationshipLinks(final int relId, final BigDecimal similarityThreshold) throws Exception {
		ColfusionRelationships relationship = findByID(relId);
		
		if (relationship == null) {
			return null;
		}
		
		List<RelationshipLinkViewModel> result = new ArrayList<>();
		
		RelationshipsColumnsDataMathingRatiosManager relColDataMatchMng = new RelationshipsColumnsDataMathingRatiosManagerImpl();
		
		for (Object linkObj : relationship.getColfusionRelationshipsColumnses()) {
			ColfusionRelationshipsColumns link = (ColfusionRelationshipsColumns) linkObj;
						
			ColfusionRelationshipsColumnsDataMathingRatios colDataMatch = 
					relColDataMatchMng.findByID(new ColfusionRelationshipsColumnsDataMathingRatiosId(link.getId().getClFrom(), link.getId().getClTo(), similarityThreshold));
			
			result.add(new RelationshipLinkViewModel(colDataMatch.getId().getClFrom(), colDataMatch.getId().getClTo(), 
					colDataMatch.getDataMatchingFromRatio().doubleValue(), colDataMatch.getDataMatchingToRatio().doubleValue()));
			
		}
		
		return result;
	}
}
