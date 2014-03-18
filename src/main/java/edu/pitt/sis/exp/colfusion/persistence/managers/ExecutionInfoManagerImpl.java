/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
import edu.pitt.sis.exp.colfusion.persistence.dao.ExecutionInfoDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.ExecutionInfoDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionExecuteinfo;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionSourceinfo;

/**
 * @author Evgeny
 *
 */
public class ExecutionInfoManagerImpl implements ExecutionInfoManager {

	Logger logger = LogManager.getLogger(ExecutionInfoManagerImpl.class.getName());
	
	private ExecutionInfoDAO executionInfoDAO = new ExecutionInfoDAOImpl();
	
	//***************************************
	// General manager interface
	//***************************************
	
	@Override
	public Integer save(ColfusionExecuteinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            Integer result = executionInfoDAO.save(entity);
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public void saveOrUpdate(ColfusionExecuteinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            executionInfoDAO.saveOrUpdate(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("save failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("save failed HibernateException", ex);
        }
	}

	@Override
	public ColfusionExecuteinfo merge(ColfusionExecuteinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            ColfusionExecuteinfo result = executionInfoDAO.merge(entity);
            
            HibernateUtil.commitTransaction();
            
            return result;
        } catch (NonUniqueResultException ex) {
            logger.error("merge failed NonUniqueResultException", ex);
            throw ex;
        } catch (HibernateException ex) {
        	logger.error("merge failed HibernateException", ex);
        	throw ex;
        }
	}

	@Override
	public void delete(ColfusionExecuteinfo entity) {
		try {
            HibernateUtil.beginTransaction();
            
            executionInfoDAO.delete(entity);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("delete failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("delete failed HibernateException", ex);
        }
	}

	@Override
	public List<ColfusionExecuteinfo> findAll() {
		List<ColfusionExecuteinfo> result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = executionInfoDAO.findAll(ColfusionExecuteinfo.class);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findAll failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findAll failed HibernateException", ex);
        }
		return result;
	}

	@Override
	public ColfusionExecuteinfo findByID(Integer id) {
		ColfusionExecuteinfo result = null;
		try {
            HibernateUtil.beginTransaction();
            
            result = executionInfoDAO.findByID(ColfusionExecuteinfo.class, id);
            
            HibernateUtil.commitTransaction();
        } catch (NonUniqueResultException ex) {
            logger.error("findByID failed NonUniqueResultException", ex);
        } catch (HibernateException ex) {
        	logger.error("findByID failed HibernateException", ex);
        }
		return result;		
	}

	//***************************************
	// Custom methods
	//***************************************
	
	@Override
	public int getExecutionLogId(int sid, String tableName) throws Exception {
	
		logger.info(String.format("Started getExecutionLogId for %d sid and %s table. Checking if executioninfo record already exists", sid, tableName));
		
		List<ColfusionExecuteinfo> executeInfoRecords = getExistingExecutionLogId(sid, tableName);
		
		if (executeInfoRecords == null) {
			
			logger.error(String.format("getExecutionLogId failed: the resul of the query is null sid %d sid and %s table name", sid, tableName));
			
			//TODO:handle better.
			throw new Exception(String.format("getExecutionLogId failed: the resul of the query is null sid %d sid and %s table name", sid, tableName));
		}
		
		if (executeInfoRecords.size() == 0) {
			
			logger.info(String.format("getExecutionLogId for %d sid and %s table: Not Found any execution info records - so creating one", sid, tableName));
			
			return getNewExecutionLogId(sid, tableName);
		}
		else {
			//TODO: the table should not have more than one record for a given pair of sid and table name, however it is not restricted on the db level
			// so and maybe our code is bad, so we need to check if there is more than one record and do something with it.
			
			logger.info(String.format("getExecutionLogId for %d sid and %s table: Found %d execution info records - use the first one", sid, tableName, executeInfoRecords.size()));
			
			return executeInfoRecords.get(0).getEid();
		}
	}

	/**
	 * Finds list of existing execution info records for given sid and table name. 
	 * @param sid is the id of the story.
	 * @param tableName is the table name.
	 * @return list of executioninfo records {@link ColfusionExecuteinfo}.
	 */
	private List<ColfusionExecuteinfo> getExistingExecutionLogId(int sid, String tableName) {
		try {
            HibernateUtil.beginTransaction();
            
            String sql = "SELECT ex FROM ColfusionExecuteinfo as ex WHERE ex.colfusionSourceinfo.sid = :sid AND ex.tableName = :tableName";

    		Query query = HibernateUtil.getSession().createQuery(sql).setParameter("sid", sid).setParameter("tableName", String.format("'%s'", tableName));
    		
    		//TODO: the findOne should be good here because there should be only one record for given pair of sid and table name, however it is not 
    		//restricted on the db level, so at least for now I used findMany.
    		List<ColfusionExecuteinfo> executeInfoRecords = executionInfoDAO.findMany(query);
            
            HibernateUtil.commitTransaction();
            
            return executeInfoRecords;
        } catch (NonUniqueResultException ex) {
            logger.error("getExistingExecutionLogId failed NonUniqueResultException", ex);
            
            throw ex;
        } catch (HibernateException ex) {
        	logger.error("getExistingExecutionLogId failed HibernateException", ex);
        	
        	throw ex;
        }
	}

	/**
	 * Inserts a new records into executioninfo table.
	 * @param sid the id of the story associated with the log record.
	 * @param tableName the table name associated with the log record.
	 * @return the id of the executioninfo record (logid).
	 * @throws Exception 
	 */
	private int getNewExecutionLogId(int sid, String tableName) throws Exception {
		
		//TODO: this will run separate transaction to find sid, maybe we need to run it together within one tracsaction, need to test and see.
		SourceInfoManager storyMgr = new SourceInfoManagerImpl();
		ColfusionSourceinfo story = storyMgr.findByID(sid);
		
		if (story == null) {
			logger.error(String.format("getNewExecutionLogId failedL: the story is null for %d", sid));
			
			//TODO:handle better.
			throw new Exception(String.format("getNewExecutionLogId failedL: the story is null for %d", sid));
		}
		
		ColfusionExecuteinfo newExecutionInfoRecord = new ColfusionExecuteinfo(story, tableName);
		
		return save(newExecutionInfoRecord);
	}

}
