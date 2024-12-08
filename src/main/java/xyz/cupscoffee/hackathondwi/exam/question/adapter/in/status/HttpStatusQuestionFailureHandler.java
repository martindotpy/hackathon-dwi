package xyz.cupscoffee.hackathondwi.exam.question.adapter.in.status;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureHandler;

/**
 * Handler for HTTP status codes for question failures.
 */
@Component
public final class HttpStatusQuestionFailureHandler implements HttpStatusCodeFailureHandler<QuestionFailure> {
    @Override
    public int getHttpStatusCode(QuestionFailure failure) {
        return switch (failure) {
            case NOT_FOUND -> 404;
            case EXAM_NOT_FOUND -> 404;
            case CANNOT_OVERFLOW_MAX_VALUE -> 409;
            case CANNOT_OVERLAP_ORDER -> 409;
            case UNEXPECTED_ERROR -> 500;
        };
    }
}