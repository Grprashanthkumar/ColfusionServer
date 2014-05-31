/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.ColfusionRelationshipsColumnsDataMathingRatiosDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.ColfusionRelationshipsColumnsDataMathingRatiosDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.dao.RelationshipsDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationships;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;

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
	public ColfusionRelationshipsColumnsDataMathingRatios findColfusionRelationshipsColumnsDataMathingRatios(
			final ColfusionRelationshipsColumnsDataMathingRatiosId colfusionRelationshipsColumnsDataMathingRatiosId) {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionRelationshipsColumnsDataMathingRatiosDAO dao = new ColfusionRelationshipsColumnsDataMathingRatiosDAOImpl();
            
            ColfusionRelationshipsColumnsDataMathingRatios dataMatchingRatios = 
            		dao.findByID(ColfusionRelationshipsColumnsDataMathingRatios.class, colfusionRelationshipsColumnsDataMathingRatiosId);
                        
            HibernateUtil.commitTransaction();
            
            return dataMatchingRatios;
		
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findDatasetInfoBySid failed HibernateException", ex);
        	throw ex;
        }	
	}
}
