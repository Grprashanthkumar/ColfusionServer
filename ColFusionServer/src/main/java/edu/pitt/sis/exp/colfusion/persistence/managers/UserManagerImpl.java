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
public class UserManagerImpl extends GeneralManagerImpl<ColfusionUsers, Integer> implements UserManager {

	Logger logger = LogManager.getLogger(UserManagerImpl.class.getName());
	
	public UserManagerImpl() {
		super(new UsersDAOImpl(), ColfusionUsers.class);
	}

	//***************************************
	// From UserManager interface
	//***************************************
	
	@Override
	public List<ColfusionUsers> lookUpUser(String searchTerm, int limit) {
		try {
            HibernateUtil.beginTransaction();
            
            List<ColfusionUsers> result = ((UsersDAO) _dao).lookUpUser(searchTerm, limit);
            
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
}