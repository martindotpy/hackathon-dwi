package xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence.repository.CriteriaHibernateCourseRepository;
import xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence.repository.SpringCourseRepository;
import xyz.cupscoffee.hackathondwi.semester.course.application.mapper.CourseMapper;
import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.semester.course.domain.repository.CourseRepository;
import xyz.cupscoffee.hackathondwi.shared.adapter.annotations.PersistenceAdapter;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Persistence adapter for the course entity.
 *
 * <p>
 * Implements the {@link CourseRepository} interface, to be used by the domain
 * layer.
 * </p>
 */
@Slf4j
@PersistenceAdapter
@AllArgsConstructor
public final class CoursePersistenceAdapter implements CourseRepository {
    private final SpringCourseRepository springCourseRepository;
    private final CriteriaHibernateCourseRepository criteriaHibernateCourseRepository;
    private final CourseMapper courseMapper;

    @Override
    public Result<Course, CourseFailure> save(Course entity) {
        try {
            return Result.ok(courseMapper.toDomain(springCourseRepository.save(courseMapper.toEntity(entity))));
        } catch (DataIntegrityViolationException e) {
            log.error("Error saving course", e);

            return Result.failure(CourseFailure.UNEXPECTED_ERROR);
        } catch (Exception e) {
            log.error("Error saving course", e);

            return Result.failure(CourseFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Optional<Course> findById(Long id) {
        return springCourseRepository.findById(id)
                .map(courseMapper::toDomain);
    }

    @Override
    public List<Course> findAll() {
        return springCourseRepository.findAll()
                .stream()
                .map(courseMapper::toDomain)
                .toList();
    }

    @Override
    public Result<Void, CourseFailure> deleteById(Long id) {
        try {
            springCourseRepository.deleteById(id);

            return Result.ok();
        } catch (Exception e) {
            log.error("Error deleting course with id {}", id, e);

            return Result.failure(CourseFailure.UNEXPECTED_ERROR);
        }
    }

    @Override
    public Paginated<Course> match(Criteria criteria) {
        return criteriaHibernateCourseRepository.match(criteria).map(courseMapper::toDomain);
    }

    @Override
    public Result<Course, CourseFailure> matchOne(Criteria criteria) {
        Optional<Course> course;
        try {
            course = criteriaHibernateCourseRepository.matchOne(criteria)
                    .map(courseMapper::toDomain);
        } catch (Exception e) {
            log.error("Error matching course", e);

            return Result.failure(CourseFailure.UNEXPECTED_ERROR);
        }

        if (course.isEmpty()) {
            return Result.failure(CourseFailure.NOT_FOUND);
        }

        return Result.ok(course.get());
    }
}
