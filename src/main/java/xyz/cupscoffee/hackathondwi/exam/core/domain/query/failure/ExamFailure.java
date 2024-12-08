package xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

/**
 * Exam failures.
 */
@Getter
@RequiredArgsConstructor
public enum ExamFailure implements Failure {
    NOT_FOUND("Exam not found"),
    COURSE_NOT_FOUND("Course not found"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private final String message;
}
