package edu.pitt.sis.exp.colfusion.persistence.managers;

import java.util.List;


public interface GeneralManager<T, ID> {
	/**
	 * Saves provided entity in the database.
	 * 
	 * @param entity to be saved in the database.
	 */
	public ID save(T entity);

	
	/**
	 * Saves or updates provided entity in the database.
	 * 
	 * @param entity to be saved in the database.
	 */
	public void saveOrUpdate(T entity);

	/**
	 * NOT SUREWHAT IT DOES :-)
	 * @param entity
	 */
    public T merge(T entity);

    /**
     * Deletes provided entity.
     * 
     * @param entity to be deleted from database.
     */
    public void delete(T entity);
    
    /**
     * Get all entities.
     * 
     * @param clazz of entities to be returned.
     * @return all found entities.
     */
	public List<T> findAll();

    /**
     * Find only one entity by id.
     * 
     * @param clazz of entity to be found.
     * @param id to search for.
     * @return found entity.
     */
     public T findByID(ID id);
}
