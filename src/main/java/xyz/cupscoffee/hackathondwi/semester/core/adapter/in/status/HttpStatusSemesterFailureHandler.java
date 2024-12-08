package xyz.cupscoffee.hackathondwi.semester.core.adapter.in.status;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureHandler;

/**
 * Handler for HTTP status codes for semester failures.
 */
@Component
public final class HttpStatusSemesterFailureHandler implements HttpStatusCodeFailureHandler<SemesterFailure> {
    @Override
    public int getHttpStatusCode(SemesterFailure failure) {
        return switch (failure) {
            case NOT_FOUND -> 404;
            case UNEXPECTED_ERROR -> 500;
        };
    }
}