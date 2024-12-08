package xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import xyz.cupscoffee.hackathondwi.shared.domain.query.failure.Failure;

/**
 * Answer failures.
 */
@Getter
@RequiredArgsConstructor
public enum AnswerFailure implements Failure {
    NOT_FOUND("Answer not found"),
    EXAM_NOT_FOUND("Exam not found"),
    QUESTION_NOT_FOUND("Question not found"),
    STUDENT_NOT_FOUND("Student not found"),
    CANNOT_OVERFLOW_MAX_VALUE("Cannot overflow max value"),
    UNEXPECTED_ERROR("An unexpected error occurred");

    private final String message;
}
