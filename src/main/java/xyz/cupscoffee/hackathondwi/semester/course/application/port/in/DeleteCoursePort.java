package xyz.cupscoffee.hackathondwi.semester.course.application.port.in;

import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete course port.
 */
public interface DeleteCoursePort {
    /**
     * Delete a course by its ID.
     *
     * @param id The ID of the course to delete.
     * @return the result of the operation
     */
    Result<Void, CourseFailure> deleteById(Long id);
}
