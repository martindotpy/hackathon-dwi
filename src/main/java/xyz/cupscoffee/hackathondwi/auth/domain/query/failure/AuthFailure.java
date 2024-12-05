package xyz.cupscoffee.hackathondwi.auth.domain.query.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

@Getter
@RequiredArgsConstructor
public enum AuthFailure implements Failure {
    INVALID_CREDENTIALS("Invalid credentials"),
    CODE_ALREADY_EXISTS("Code already exists"),
    UNEXPECTED_ERROR("Unexpected error");

    private final String message;
}
