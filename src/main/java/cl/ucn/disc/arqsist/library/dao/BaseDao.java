/*
 * Copyright (c) 2026. Arquitectura de Sistemas, DISC, UCN, Antofagasta.
 */
package cl.ucn.disc.arqsist.library.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/** Provides shared ORMLite CRUD and transaction operations. */
public abstract class BaseDao<T> {

    /** ORMLite data access object. */
    protected final Dao<T, Integer> dao;

    /** Creates a DAO for the specified entity type.
     * @param connectionSource database connection source
     * @param entityClass entity class
     */
    protected BaseDao(ConnectionSource connectionSource, Class<T> entityClass) {
        try {
            this.dao = DaoManager.createDao(connectionSource, entityClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Finds all entities.
     * @return all entities
     */
    public List<T> findAll() {
        try {
            return dao.queryForAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Finds an entity by id.
     * @param id entity id
     * @return entity or null
     */
    public T findById(int id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Creates an entity.
     * @param entity entity to create
     */
    public void create(T entity) {
        try {
            dao.create(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Updates an entity.
     * @param entity entity to update
     */
    public void update(T entity) {
        try {
            dao.update(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Deletes an entity.
     * @param entity entity to delete
     */
    public void delete(T entity) {
        try {
            dao.delete(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Runs work in a database transaction and preserves domain runtime errors.
     * @param callable transactional work
     * @param <R> result type
     * @return callable result
     * @throws SQLException if the transaction cannot complete
     */
    public <R> R transaction(Callable<R> callable) throws SQLException {
        try {
            return TransactionManager.callInTransaction(dao.getConnectionSource(), callable);
        } catch (SQLException e) {
            if (e.getCause() instanceof RuntimeException cause) {
                throw cause;
            }
            throw e;
        }
    }
}