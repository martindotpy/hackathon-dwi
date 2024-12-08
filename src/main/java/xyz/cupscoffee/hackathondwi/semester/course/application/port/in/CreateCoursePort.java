package xyz.cupscoffee.hackathondwi.semester.course.application.port.in;

import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload.CreateCoursePayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Create course port.
 */
public interface CreateCoursePort {
    /**
     * Create a course.
     *
     * @param payload the payload to create a course.
     * @return the result of the operation
     */
    Result<CourseDto, CourseFailure> create(CreateCoursePayload payload);
}
