package xyz.cupscoffee.hackathondwi.semester.core.domain.repository;

import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.BasicRepository;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.CriteriaRepository;

/**
 * Semester repository.
 */
public interface SemesterRepository
        extends BasicRepository<Semester, Long, SemesterFailure>, CriteriaRepository<Semester, SemesterFailure> {
}
