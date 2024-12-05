package xyz.cupscoffee.hackathondwi.auth.application.port.out;

import java.util.Optional;

import xyz.cupscoffee.hackathondwi.user.domain.model.User;

/**
 * Interface for getting the authenticated user.
 */
public interface GetAuthenticatedUserPort {
    /**
     * Get the authenticated user.
     *
     * @return The authenticated user, if any.
     */
    Optional<User> get();
}
