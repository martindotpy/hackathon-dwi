package xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence.repository.CriteriaHibernateExamRepository;
import xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence.repository.SpringExamRepository;
import xyz.cupscoffee.hackathondwi.exam.core.application.mapper.ExamMapper;
import xyz.cupscoffee.hackathondwi.exam.core.domain.model.Exam;
import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.exam.core.domain.repository.ExamRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.PersistenceAdapter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Persistence adapter for the exam entity.
 *
 * <p>
 * Implements the {@link ExamRepository} interface, to be used by the domain
 * layer.
 * </p>
 */
@Slf4j
@PersistenceAdapter
@AllArgsConstructor
public final class ExamPersistenceAdapter implements ExamRepository {
    private final SpringExamRepository springExamRepository;
    private final CriteriaHibernateExamRepository criteriaHibernateExamRepository;
    private final ExamMapper examMapper;

    @Override
    public Result<Exam, ExamFailure> save(Exam entity) {
        try {
            return Result.ok(examMapper.toDomain(springExamRepository.save(examMapper.toEntity(entity))));
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving exam", e);

            return Result.failure(ExamFailure.UNEXPECTED_ERROR);
        } catch (Exception e) {
            log.error("Error saving exam", e);

            return Result.failure(ExamFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Optional<Exam> findById(Long id) {
        return springExamRepository.findById(id)
                .map(examMapper::toDomain);
    }

    @Override
    public List<Exam> findAll() {
        return springExamRepository.findAll()
                .stream()
                .map(examMapper::toDomain)
                .toList();
    }

    @Override
    public Result<Void, ExamFailure> deleteById(Long id) {
        try {
            springExamRepository.deleteById(id);

            return Result.ok();
        } catch (Exception e) {
            log.error("Error deleting exam with id {}", id, e);

            return Result.failure(ExamFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Paginated<Exam> match(Criteria criteria) {
        return criteriaHibernateExamRepository.match(criteria).map(examMapper::toDomain);
    }

    @Override
    public Result<Exam, ExamFailure> matchOne(Criteria criteria) {
        Optional<Exam> exam;
        try {
            exam = criteriaHibernateExamRepository.matchOne(criteria)
                    .map(examMapper::toDomain);
        } catch (Exception e) {
            log.error("Error matching exam", e);

            return Result.failure(ExamFailure.UNEXPECTED_ERROR);
        }

        if (exam.isEmpty()) {
            return Result.failure(ExamFailure.NOT_FOUND);
        }

        return Result.ok(exam.get());
    }
}
