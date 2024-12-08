package xyz.cupscoffee.hackathondwi.student.core.adapter.in.status;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureHandler;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;

/**
 * Handler for HTTP status codes for student failures.
 */
@Component
public final class HttpStatusStudentFailureHandler implements HttpStatusCodeFailureHandler<StudentFailure> {
    @Override
    public int getHttpStatusCode(StudentFailure failure) {
        return switch (failure) {
            case NOT_FOUND -> 404;
            case COURSE_NOT_FOUND -> 404;
            case UNEXPECTED_ERROR -> 500;
        };
    }
}