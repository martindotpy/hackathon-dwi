package xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

/**
 * Course failures.
 */
@Getter
@RequiredArgsConstructor
public enum CourseFailure implements Failure {
    NOT_FOUND("Course not found"),
    SEMESTER_NOT_FOUND("Semester not found"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private final String message;
}
