/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.ProcessesDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.ProcessesDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionProcesses;
import edu.pitt.sis.exp.colfusion.process.ProcessStatusEnum;

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
	public ColfusionProcesses getNewPendingProcessAndSetAsRunningFromDB(String status, String reason, int limit) {
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
