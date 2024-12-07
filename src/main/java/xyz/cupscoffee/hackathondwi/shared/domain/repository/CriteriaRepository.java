package xyz.cupscoffee.hackathondwi.shared.domain.repository;

import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Criteria repository.
 *
 * @param <T> the type of the entity.
 * @param <F> the type of the failure.
 */
public interface CriteriaRepository<T, F extends Failure> {
    /**
     * Match the criteria.
     *
     * @param criteria the criteria.
     * @return the paginated result
     */
    Paginated<T> match(Criteria criteria);

    /**
     * Match one.
     *
     * @param criteria the criteria.
     * @return the optional result
     */
    Result<T, F> matchOne(Criteria criteria);
}
