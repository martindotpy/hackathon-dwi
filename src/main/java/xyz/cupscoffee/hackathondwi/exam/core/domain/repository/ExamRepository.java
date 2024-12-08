package xyz.cupscoffee.hackathondwi.exam.core.domain.repository;

import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.BasicRepository;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.CriteriaRepository;

/**
 * Exam repository.
 */
public interface ExamRepository
        extends BasicRepository<Exam, Long, ExamFailure>, CriteriaRepository<Exam, ExamFailure> {
}
