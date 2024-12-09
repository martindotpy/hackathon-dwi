package xyz.cupscoffee.hackathondwi.student.core.application.port.in;

import java.util.List;

import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.application.dto.StudentDto;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;

/**
 * Find student port.
 */
public interface FindStudentPort {
    /**
     * Find all students that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Paginated<StudentDto> match(Criteria criteria);

    /**
     * Find one student that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Result<StudentDto, StudentFailure> matchOne(Criteria criteria);

    /**
     * Find all students by course id.
     *
     * @param courseId The course id.
     * @return the result of the operation
     */
    List<StudentDto> findAllByCourseId(Long courseId);
}
