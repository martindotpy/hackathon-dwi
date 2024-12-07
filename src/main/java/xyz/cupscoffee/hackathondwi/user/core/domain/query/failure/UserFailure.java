package xyz.cupscoffee.hackathondwi.user.core.domain.query.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

@Getter
@RequiredArgsConstructor
public enum UserFailure implements Failure {
    UNEXPECTED_ERROR("An unexpected error occurred"),
    CODE_ALREADY_EXISTS("The code already exists"),;

    private final String message;
}
