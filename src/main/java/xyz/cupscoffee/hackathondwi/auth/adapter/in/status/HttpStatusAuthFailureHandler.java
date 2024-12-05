package xyz.cupscoffee.hackathondwi.auth.adapter.in.status;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.auth.domain.query.failure.AuthFailure;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureHandler;

@Component
public final class HttpStatusAuthFailureHandler implements HttpStatusCodeFailureHandler<AuthFailure> {
    @Override
    public int getHttpStatusCode(AuthFailure failure) {
        return switch (failure) {
            case INVALID_CREDENTIALS -> 401;
            case CODE_ALREADY_EXISTS -> 409;
            case UNEXPECTED_ERROR -> 500;
        };
    }
}
