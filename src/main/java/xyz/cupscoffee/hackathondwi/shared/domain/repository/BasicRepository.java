package xyz.cupscoffee.hackathondwi.shared.domain.repository;

import java.util.List;
import java.util.Optional;

import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Basic repository interface.
 *
 * @param <T>  The entity type.
 * @param <ID> The ID type.
 * @param <F>  The failure type.
 */
public interface BasicRepository<T, ID, F extends Failure> {
    /**
     * Save an entity.
     *
     * @param entity The entity to save.
     * @return The saved entity or a failure.
     */
    Result<T, F> save(T entity);

    /**
     * Find an entity by ID.
     *
     * @param id The ID of the entity.
     * @return The entity, if found
     */
    Optional<T> findById(ID id);

    /**
     * Find all entities.
     *
     * @return All entities
     */
    List<T> findAll();

    /**
     * Delete an entity by ID.
     *
     * @param id The ID of the entity.
     * @return A success or a failure
     */
    Result<Void, F> deleteById(ID id);
}
