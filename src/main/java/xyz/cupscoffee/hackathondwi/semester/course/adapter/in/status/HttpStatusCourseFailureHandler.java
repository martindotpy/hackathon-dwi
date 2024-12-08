package xyz.cupscoffee.hackathondwi.semester.course.adapter.in.status;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.shared.adapter.in.status.HttpStatusCodeFailureHandler;

/**
 * Handler for HTTP status codes for course failures.
 */
@Component
public final class HttpStatusCourseFailureHandler implements HttpStatusCodeFailureHandler<CourseFailure> {
    @Override
    public int getHttpStatusCode(CourseFailure failure) {
        return switch (failure) {
            case NOT_FOUND -> 404;
            case SEMESTER_NOT_FOUND -> 404;
            case UNEXPECTED_ERROR -> 500;
        };
    }
}