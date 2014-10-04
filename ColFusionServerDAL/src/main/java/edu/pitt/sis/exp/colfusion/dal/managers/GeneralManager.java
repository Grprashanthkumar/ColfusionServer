package edu.pitt.sis.exp.colfusion.dal.managers;

import java.util.List;


public interface GeneralManager<T, ID> {
	
	/**
	 * Saves provided entity in the database.
	 * 
	 * @param entity to be saved in the database.
	 * @throws Exception 
	 */
	public ID save(T entity) throws Exception;

	
	/**
	 * Saves or updates provided entity in the database.
	 * 
	 * @param entity to be saved in the database.
	 * @throws Exception 
	 */
	public void saveOrUpdate(T entity) throws Exception;

	/**
	 * NOT SUREWHAT IT DOES :-)
	 * @param entity
	 * @throws Exception 
	 */
    public T merge(T entity) throws Exception;

    /**
     * Deletes provided entity.
     * 
     * @param entity to be deleted from database.
     * @throws Exception 
     */
    public void delete(T entity) throws Exception;
    
    /**
     * Get all entities.
     * 
     * @param clazz of entities to be returned.
     * @return all found entities.
     * @throws Exception 
     */
	public List<T> findAll() throws Exception;

    /**
     * Find only one entity by id.
     * 
     * @param clazz of entity to be found.
     * @param id to search for.
     * @return found entity.
     * @throws Exception 
     */
     public T findByID(ID id) throws Exception;
}
