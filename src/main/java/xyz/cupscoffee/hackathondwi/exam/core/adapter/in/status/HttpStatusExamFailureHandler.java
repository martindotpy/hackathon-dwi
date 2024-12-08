package xyz.cupscoffee.hackathondwi.exam.core.adapter.in.status;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureHandler;

/**
 * Handler for HTTP status codes for exam failures.
 */
@Component
public final class HttpStatusExamFailureHandler implements HttpStatusCodeFailureHandler<ExamFailure> {
    @Override
    public int getHttpStatusCode(ExamFailure failure) {
        return switch (failure) {
            case NOT_FOUND -> 404;
            case COURSE_NOT_FOUND -> 404;
            case UNEXPECTED_ERROR -> 500;
        };
    }
}