package xyz.cupscoffee.hackathondwi.auth.application.port.in;

import xyz.cupscoffee.hackathondwi.auth.application.dto.JwtDto;
import xyz.cupscoffee.hackathondwi.auth.domain.query.failure.AuthFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.user.core.domain.payload.RegisterUserPayload;

/**
 * Port for registering a user.
 */
public interface RegisterUserPort {
    Result<JwtDto, AuthFailure> register(RegisterUserPayload payload);
}
