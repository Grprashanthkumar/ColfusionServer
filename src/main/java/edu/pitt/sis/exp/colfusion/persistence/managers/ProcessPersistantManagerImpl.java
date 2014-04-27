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
public class ProcessPersistantManagerImpl implements ProcessPersistantManager {

	Logger logger = LogManager.getLogger(ProcessPersistantManagerImpl.class.getName());
	
	private ProcessesDAO processesDAO = new ProcessesDAOImpl();
	
	@Override
	public Integer save(ColfusionProcesses entity) {
		try {
            HibernateUtil.beginTransaction();
            
            Integer result = processesDAO.save(entity);
            
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
        }
	}

	@Override
	public void saveOrUpdate(ColfusionProcesses entity) {
		try {
            HibernateUtil.beginTransaction();
            
            processesDAO.saveOrUpdate(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            
        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("save failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public ColfusionProcesses merge(ColfusionProcesses entity) {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionProcesses result = processesDAO.merge(entity);
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("merge failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("merge failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public void delete(ColfusionProcesses entity) {
		try {
            HibernateUtil.beginTransaction();
            
            processesDAO.delete(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("delete failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("delete failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public List<ColfusionProcesses> findAll() {
		List<ColfusionProcesses> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = processesDAO.findAll(ColfusionProcesses.class);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findAll failed NonUniqueResultException", ex);
        	throw ex;
        } catch (HibernateException ex) {

        	HibernateUtil.rollbackTransaction();
        	
        	logger.error("findAll failed HibernateException", ex);
        	throw ex;
        }
		return result;
	}

	@Override
	public ColfusionProcesses findByID(Integer id) {
		ColfusionProcesses result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = processesDAO.findByID(ColfusionProcesses.class, id);
            
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
	public ColfusionProcesses getNewPendingProcessAndSetAsRunningFromDB(String status, String reason, int limit) {
		ColfusionProcesses result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = processesDAO.findPendingProcess(limit);
            
            if (result != null) {
            	result.setStatus(status);
            	result.setReasonForStatus(reason);
            	processesDAO.saveOrUpdate(result);
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
