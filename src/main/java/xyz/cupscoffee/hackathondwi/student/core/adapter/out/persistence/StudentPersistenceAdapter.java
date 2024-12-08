package xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.PersistenceAdapter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.repository.CriteriaHibernateStudentRepository;
import xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.repository.SpringStudentRepository;
import xyz.cupscoffee.hackathondwi.student.core.application.mapper.StudentMapper;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;
import xyz.cupscoffee.hackathondwi.student.core.domain.repository.StudentRepository;

/**
 * Persistence adapter for the student entity.
 *
 * <p>
 * Implements the {@link StudentRepository} interface, to be used by the domain
 * layer.
 * </p>
 */
@Slf4j
@PersistenceAdapter
@AllArgsConstructor
public final class StudentPersistenceAdapter implements StudentRepository {
    private final SpringStudentRepository springStudentRepository;
    private final CriteriaHibernateStudentRepository criteriaHibernateStudentRepository;
    private final StudentMapper studentMapper;

    @Override
    public Result<Student, StudentFailure> save(Student entity) {
        try {
            return Result.ok(studentMapper.toDomain(springStudentRepository.save(studentMapper.toEntity(entity))));
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving student", e);

            return Result.failure(StudentFailure.UNEXPECTED_ERROR);
        } catch (Exception e) {
            log.error("Error saving student", e);

            return Result.failure(StudentFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        return springStudentRepository.findById(id)
                .map(studentMapper::toDomain);
    }

    @Override
    public List<Student> findAll() {
        return springStudentRepository.findAll()
                .stream()
                .map(studentMapper::toDomain)
                .toList();
    }

    @Override
    public Result<Void, StudentFailure> deleteById(Long id) {
        try {
            springStudentRepository.deleteById(id);

            return Result.ok();
        } catch (Exception e) {
            log.error("Error deleting student with id {}", id, e);

            return Result.failure(StudentFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Paginated<Student> match(Criteria criteria) {
        return criteriaHibernateStudentRepository.match(criteria).map(studentMapper::toDomain);
    }

    @Override
    public Result<Student, StudentFailure> matchOne(Criteria criteria) {
        Optional<Student> student;
        try {
            student = criteriaHibernateStudentRepository.matchOne(criteria)
                    .map(studentMapper::toDomain);
        } catch (Exception e) {
            log.error("Error matching student", e);

            return Result.failure(StudentFailure.UNEXPECTED_ERROR);
        }

        if (student.isEmpty()) {
            return Result.failure(StudentFailure.NOT_FOUND);
        }

        return Result.ok(student.get());
    }
}
