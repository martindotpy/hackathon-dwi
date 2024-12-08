package xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence.repository.CriteriaHibernateSemesterRepository;
import xyz.cupscoffee.hackathondwi.semester.core.adapter.out.persistence.repository.SpringSemesterRepository;
import xyz.cupscoffee.hackathondwi.semester.core.application.mapper.SemesterMapper;
import xyz.cupscoffee.hackathondwi.semester.core.domain.model.Semester;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.semester.core.domain.repository.SemesterRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.PersistenceAdapter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Persistence adapter for the semester entity.
 *
 * <p>
 * Implements the {@link SemesterRepository} interface, to be used by the domain
 * layer.
 * </p>
 */
@Slf4j
@PersistenceAdapter
@AllArgsConstructor
public final class SemesterPersistenceAdapter implements SemesterRepository {
    private final SpringSemesterRepository springSemesterRepository;
    private final CriteriaHibernateSemesterRepository criteriaHibernateSemesterRepository;
    private final SemesterMapper semesterMapper;

    @Override
    public Result<Semester, SemesterFailure> save(Semester entity) {
        try {
            return Result.ok(semesterMapper.toDomain(springSemesterRepository.save(semesterMapper.toEntity(entity))));
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving semester", e);

            return Result.failure(SemesterFailure.UNEXPECTED_ERROR);
        } catch (Exception e) {
            log.error("Error saving semester", e);

            return Result.failure(SemesterFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Optional<Semester> findById(Long id) {
        return springSemesterRepository.findById(id)
                .map(semesterMapper::toDomain);
    }

    @Override
    public List<Semester> findAll() {
        return springSemesterRepository.findAll()
                .stream()
                .map(semesterMapper::toDomain)
                .toList();
    }

    @Override
    public Result<Void, SemesterFailure> deleteById(Long id) {
        try {
            springSemesterRepository.deleteById(id);

            return Result.ok();
        } catch (Exception e) {
            log.error("Error deleting semester with id {}", id, e);

            return Result.failure(SemesterFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Paginated<Semester> match(Criteria criteria) {
        return criteriaHibernateSemesterRepository.match(criteria).map(semesterMapper::toDomain);
    }

    @Override
    public Result<Semester, SemesterFailure> matchOne(Criteria criteria) {
        Optional<Semester> semester;
        try {
            semester = criteriaHibernateSemesterRepository.matchOne(criteria)
                    .map(semesterMapper::toDomain);
        } catch (Exception e) {
            log.error("Error matching semester", e);

            return Result.failure(SemesterFailure.UNEXPECTED_ERROR);
        }

        if (semester.isEmpty()) {
            return Result.failure(SemesterFailure.NOT_FOUND);
        }

        return Result.ok(semester.get());
    }
}
