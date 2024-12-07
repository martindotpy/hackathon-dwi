package xyz.cupscoffee.hackathondwi.user.core.domain.repository;

import java.util.Optional;

import xyz.cupscoffee.hackathondwi.shared.domain.repository.BasicRepository;
import xyz.cupscoffee.hackathondwi.user.core.domain.model.User;
import xyz.cupscoffee.hackathondwi.user.core.domain.query.failure.UserFailure;

/**
 * User repository.
 */
public interface UserRepository extends BasicRepository<User, Long, UserFailure> {
    /**
     * Find a user by code.
     *
     * @param code the code.
     * @return the user.
     */
    Optional<User> findByCode(String code);
}
