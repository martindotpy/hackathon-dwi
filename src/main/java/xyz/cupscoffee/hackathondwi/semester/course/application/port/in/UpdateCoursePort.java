package xyz.cupscoffee.hackathondwi.semester.course.application.port.in;

import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.payload.UpdateCoursePayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update course port.
 */
public interface UpdateCoursePort {
    /**
     * Update a course.
     *
     * @param payload the payload to update a course.
     * @return the result of the operation
     */
    Result<CourseDto, CourseFailure> update(UpdateCoursePayload payload);
}
