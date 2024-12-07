package xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

@Getter
@RequiredArgsConstructor
public enum SemesterFailure implements Failure {
    NOT_FOUND("Semester not found"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private final String message;
}
