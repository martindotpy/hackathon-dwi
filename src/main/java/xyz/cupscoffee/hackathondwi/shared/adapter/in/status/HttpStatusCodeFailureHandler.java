package xyz.cupscoffee.hackathondwi.shared.adapter.in.status;

import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

/**
 * Handler for HTTP status codes for failures.
 *
 * @param <T> the type of the failure.
 */
public interface HttpStatusCodeFailureHandler<T extends Failure> {
    /**
     * Get the HTTP status code for a failure.
     *
     * @param failure the failure.
     * @return the HTTP status code.
     */
    int getHttpStatusCode(T failure);
}
