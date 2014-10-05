/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.dal.dao.RelationshipsColumnsDataMathingRatiosDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.RelationshipsColumnsDataMathingRatiosDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumnsDataMathingRatios;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionRelationshipsColumnsDataMathingRatiosId;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author Evgeny
 *
 */
public class RelationshipsColumnsDataMathingRatiosManagerImpl extends
		GeneralManagerImpl<ColfusionRelationshipsColumnsDataMathingRatios, ColfusionRelationshipsColumnsDataMathingRatiosId> implements
		RelationshipsColumnsDataMathingRatiosManager {

	static Logger logger = LogManager.getLogger(RelationshipsColumnsDataMathingRatiosManagerImpl.class.getName());
	
	public RelationshipsColumnsDataMathingRatiosManagerImpl() {
		super(new RelationshipsColumnsDataMathingRatiosDAOImpl(), ColfusionRelationshipsColumnsDataMathingRatios.class);
	}
	
	@Override
	public ColfusionRelationshipsColumnsDataMathingRatios findColfusionRelationshipsColumnsDataMathingRatios(
			final ColfusionRelationshipsColumnsDataMathingRatiosId colfusionRelationshipsColumnsDataMathingRatiosId) {
		try {
            HibernateUtil.beginTransaction();
            
            RelationshipsColumnsDataMathingRatiosDAO dao = new RelationshipsColumnsDataMathingRatiosDAOImpl();
            
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
