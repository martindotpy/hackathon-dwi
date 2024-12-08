package xyz.cupscoffee.hackathondwi.student.core.domain.query.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

/**
 * Student failures.
 */
@Getter
@RequiredArgsConstructor
public enum StudentFailure implements Failure {
    NOT_FOUND("Student not found"),
    COURSE_NOT_FOUND("Course not found"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private final String message;
}
