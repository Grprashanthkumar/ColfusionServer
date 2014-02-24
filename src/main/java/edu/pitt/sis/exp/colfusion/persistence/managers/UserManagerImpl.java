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
import edu.pitt.sis.exp.colfusion.persistence.dao.UsersDAO;
import edu.pitt.sis.exp.colfusion.persistence.dao.UsersDAOImpl;
import edu.pitt.sis.exp.colfusion.persistence.orm.ColfusionUsers;

/**
 * @author Evgeny
 *
 */
public class UserManagerImpl implements UserManager {

	Logger logger = LogManager.getLogger(UserManagerImpl.class.getName());
	
	private UsersDAO userDAO = new UsersDAOImpl();
	
	//***************************************
	// General manager interface
	//***************************************
	
	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#save(java.lang.Object)
	 */
	@Override
	public Integer save(ColfusionUsers entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(ColfusionUsers entity) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#merge(java.lang.Object)
	 */
	@Override
	public ColfusionUsers merge(ColfusionUsers entity) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#delete(java.lang.Object)
	 */
	@Override
	public void delete(ColfusionUsers entity) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#findAll()
	 */
	@Override
	public List<ColfusionUsers> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.pitt.sis.exp.colfusion.persistence.managers.GeneralManager#findByID(java.lang.Object)
	 */
	@Override
	public ColfusionUsers findByID(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	//***************************************
	// From UserManager interface
	//***************************************
	
	@Override
	public List<ColfusionUsers> lookUpUser(String searchTerm, int limit) {
		try {
            HibernateUtil.beginTransaction();
            
            List<ColfusionUsers> result = userDAO.lookUpUser(searchTerm, limit);
            
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

}
