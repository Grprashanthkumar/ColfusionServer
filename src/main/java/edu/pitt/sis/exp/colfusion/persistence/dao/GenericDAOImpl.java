/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.pitt.sis.exp.colfusion.persistence.HibernateUtil;
 
/**
 *
 * @author leonidas
 */
public abstract class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {
 
    protected Session getSession() {
        return HibernateUtil.getSession();
    }
 
    /**
	 * Saves provided entity in the database.
	 * 
	 * @param entity to be saved in the database.
	 */
    @SuppressWarnings("unchecked")
	public ID save(T entity) {
        Session hibernateSession = this.getSession();
        return (ID) hibernateSession.save(entity);
    }
    
    /**
	 * Saves provided entity in the database.
	 * 
	 * @param entity to be saved in the database.
	 */
    public void saveOrUpdate(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.saveOrUpdate(entity);
    }
 
    /**
	 * NOT SUREWHAT IT DOES :-)
	 * @param entity
	 */
    @SuppressWarnings("unchecked")
	public T merge(T entity) {
        Session hibernateSession = this.getSession();
        
		return (T) hibernateSession.merge(entity);
    }
 
    /**
     * Deletes provided entity.
     * 
     * @param entity to be deleted from database.
     */
    public void delete(T entity) {
        Session hibernateSession = this.getSession();
        hibernateSession.delete(entity);
    }
 
    /**
     * Find all entities which satisfy provided query.
     * 
     * @param query to run to find the entities.
     * @return list of found entities.
     */
    @SuppressWarnings("unchecked")
	public List<T> findMany(Query query) {
        List<T> t;
        t = (List<T>) query.list();
        return t;
    }
 
    /**
     * Find only one entity.
     * 
     * @param query to run to find the entity.
     * @return the found entity.
     */
    @SuppressWarnings("unchecked")
	public T findOne(Query query) {
        T t;
        t = (T) query.uniqueResult();
        return t;
    }
 
    /**
     * Find only one entity by id.
     * 
     * @param clazz of entity to be found.
     * @param id to search for.
     * @return found entity.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public T findByID(Class clazz, ID id) {
        Session hibernateSession = this.getSession();
        T t = null;
        t = (T) hibernateSession.get(clazz, id);
        return t;
    }
 
    /**
     * Get all entities.
     * 
     * @param clazz of entities to be returned.
     * @return all found entities.
     */
    @SuppressWarnings("unchecked")
	public List<T> findAll(@SuppressWarnings("rawtypes") Class clazz) {
        Session hibernateSession = this.getSession();
        List<T> result = null;
        Query query = hibernateSession.createQuery("from " + clazz.getName());
        result = query.list();
        return result;
    }
}