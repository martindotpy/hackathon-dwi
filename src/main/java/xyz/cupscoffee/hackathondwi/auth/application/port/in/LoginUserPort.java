package xyz.cupscoffee.hackathondwi.auth.application.port.in;

import xyz.cupscoffee.hackathondwi.auth.application.dto.JwtDto;
import xyz.cupscoffee.hackathondwi.auth.domain.query.failure.AuthFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.user.core.domain.payload.LoginUserPayload;

/**
 * Port for logging in a user.
 */
public interface LoginUserPort {
    /**
     * Log in a user.
     *
     * @param payload The login payload.
     * @return The JWT, if the login is successful
     */
    Result<JwtDto, AuthFailure> login(LoginUserPayload payload);
}
