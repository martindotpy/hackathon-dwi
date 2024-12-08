package xyz.cupscoffee.hackathondwi.semester.course.application.port.in;

import xyz.cupscoffee.hackathondwi.semester.course.application.dto.CourseDto;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find course port.
 */
public interface FindCoursePort {
    /**
     * Find all courses that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Paginated<CourseDto> match(Criteria criteria);

    /**
     * Find one course that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Result<CourseDto, CourseFailure> matchOne(Criteria criteria);
}
