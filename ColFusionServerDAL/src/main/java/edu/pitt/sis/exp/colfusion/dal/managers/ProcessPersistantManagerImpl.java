/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.dal.dao.ProcessesDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionProcesses;
//import edu.pitt.sis.exp.colfusion.process.ProcessStatusEnum;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author Evgeny
 *
 */
public class ProcessPersistantManagerImpl extends GeneralManagerImpl<ColfusionProcesses, Integer> implements ProcessPersistantManager {

	Logger logger = LogManager.getLogger(ProcessPersistantManagerImpl.class.getName());
	
	public ProcessPersistantManagerImpl() {
		super(new ProcessesDAOImpl(), ColfusionProcesses.class);
	}

	@Override
	public ColfusionProcesses getNewPendingProcessAndSetAsRunningFromDB(final String status, final String reason, final int limit) {
		ColfusionProcesses result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = ((ProcessesDAOImpl)_dao).findPendingProcess(limit);
            
            if (result != null) {
            	result.setStatus(status);
            	result.setReasonForStatus(reason);
            	_dao.saveOrUpdate(result);
            }
                        
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findPendingProcess failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findPendingProcess failed HibernateException", ex);
        	throw ex;
        }
		return result;	
	}

}
