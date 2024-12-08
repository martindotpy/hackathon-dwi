package xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

/**
 * Question failures.
 */
@Getter
@RequiredArgsConstructor
public enum QuestionFailure implements Failure {
    NOT_FOUND("Question not found"),
    EXAM_NOT_FOUND("Exam not found"),
    CANNOT_OVERFLOW_MAX_VALUE("Cannot overflow max value"),
    CANNOT_OVERLAP_ORDER("Cannot overlap order"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private final String message;
}
