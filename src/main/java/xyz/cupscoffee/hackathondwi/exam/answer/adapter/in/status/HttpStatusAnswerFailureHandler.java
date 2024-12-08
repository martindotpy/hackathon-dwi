package xyz.cupscoffee.hackathondwi.exam.answer.adapter.in.status;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureHandler;

/**
 * Handler for HTTP status codes for answer failures.
 */
@Component
public final class HttpStatusAnswerFailureHandler implements HttpStatusCodeFailureHandler<AnswerFailure> {
    @Override
    public int getHttpStatusCode(AnswerFailure failure) {
        return switch (failure) {
            case NOT_FOUND -> 404;
            case EXAM_NOT_FOUND -> 404;
            case CANNOT_OVERFLOW_MAX_VALUE -> 409;
            case QUESTION_NOT_FOUND -> 404;
            case STUDENT_NOT_FOUND -> 404;
            case UNEXPECTED_ERROR -> 500;
        };
    }
}