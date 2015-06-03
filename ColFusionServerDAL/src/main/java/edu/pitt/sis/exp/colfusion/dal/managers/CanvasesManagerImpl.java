/**
 * 
 */
package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;

import edu.pitt.sis.exp.colfusion.dal.dao.CanvasesDAO;
import edu.pitt.sis.exp.colfusion.dal.dao.CanvasesDAOImpl;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionCanvases;
import edu.pitt.sis.exp.colfusion.dal.orm.ColfusionUsers;
import edu.pitt.sis.exp.colfusion.dal.utils.HibernateUtil;

/**
 * @author xiaotingli
 *
 */
public class CanvasesManagerImpl extends GeneralManagerImpl<CanvasesDAO, ColfusionCanvases, Integer>
		implements CanvasesManager {

	Logger logger = LogManager.getLogger(CanvasesManagerImpl.class.getName());
	
	public CanvasesManagerImpl() {
		super(new CanvasesDAOImpl(), ColfusionCanvases.class);
	}
	
	public ColfusionCanvases createNewCanvas(ColfusionUsers canvasOwner, String name) {
		ColfusionCanvases newCanvas = new ColfusionCanvases(new Date(), new Date());
		newCanvas.setUsers(canvasOwner);
		newCanvas.setName(name);
		
		try {
			HibernateUtil.beginTransaction();
			
			_dao.save(newCanvas);
			
			// Hibernate.initialize(newCanvas.getUsers());
			
			HibernateUtil.commitTransaction();
			
		}
		catch (Exception ex) {
			HibernateUtil.rollbackTransaction();
			
			this.logger.error("getTableInfo failed HibernateException", ex);
			throw ex;
		}
		
		return newCanvas;
	}
	

	public List<ColfusionCanvases> findCanvasByUser(ColfusionUsers users){
		try{
			HibernateUtil.beginTransaction();
			
			//int userId = users.getUserId();
			String hql = "SELECT C FROM Canvases C WHERE C.users = :users";

			Query query = HibernateUtil.getSession().createQuery(hql);
			query.setParameter("users", users);
			
			List<ColfusionCanvases> result = _dao.findMany(query);
			
			for (ColfusionCanvases canvas : result) { // lazy
				Hibernate.initialize(canvas.getUsers());
			}
			
			HibernateUtil.commitTransaction();
			
			return result;
			
		}catch(Exception ex){
            HibernateUtil.rollbackTransaction();
			
			this.logger.error("Cannot find the records", ex);
			throw ex;
		}
	}
	}

