package xyz.cupscoffee.hackathondwi.semester.course.domain.repository;

import xyz.cupscoffee.hackathondwi.semester.course.domain.model.Course;
import xyz.cupscoffee.hackathondwi.semester.course.domain.query.failure.CourseFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.BasicRepository;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.CriteriaRepository;

/**
 * Course repository.
 */
public interface CourseRepository
        extends BasicRepository<Course, Long, CourseFailure>, CriteriaRepository<Course, CourseFailure> {
}
