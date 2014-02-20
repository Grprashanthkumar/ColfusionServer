/**
 * 
 */
package edu.pitt.sis.exp.colfusion.persistence.dao;

/**
*
* @author leonidas
*/
import java.io.*;
import java.util.*;

import org.hibernate.Query;

public interface GenericDAO<T, ID extends Serializable> {

	/**
	 * Saves provided entity in the database.
	 * 
	 * @param entity to be saved in the database.
	 */
	public void save(T entity);

	/**
	 * NOT SUREWHAT IT DOES :-)
	 * @param entity
	 */
    public void merge(T entity);

    /**
     * Deletes provided entity.
     * 
     * @param entity to be deleted from database.
     */
    public void delete(T entity);

    /**
     * Find all entities which satisfy provided query.
     * 
     * @param query to run to find the entities.
     * @return list of found entities.
     */
    public List<T> findMany(Query query);

    /**
     * Find only one entity.
     * 
     * @param query to run to find the entity.
     * @return the found entity.
     */
    public T findOne(Query query);

    /**
     * Get all entities.
     * 
     * @param clazz of entities to be returned.
     * @return all found entities.
     */
    @SuppressWarnings("rawtypes")
	public List<T> findAll(Class clazz);

    /**
     * Find only one entity by id.
     * 
     * @param clazz of entity to be found.
     * @param id to search for.
     * @return found entity.
     */
    @SuppressWarnings("rawtypes")
    public T findByID(Class clazz, ID id);
}
