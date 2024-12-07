package xyz.cupscoffee.hackathondwi.semester.core.application.port.in;

import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find semester port.
 */
public interface FindSemesterPort {
    /**
     * Find all semesters that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Paginated<SemesterDto> match(Criteria criteria);

    /**
     * Find one semester that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Result<SemesterDto, SemesterFailure> matchOne(Criteria criteria);
}
