package xyz.cupscoffee.hackathondwi.student.core.domain.repository;

import java.util.List;

import xyz.cupscoffee.hackathondwi.shared.domain.repository.BasicRepository;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.CriteriaRepository;
import xyz.cupscoffee.hackathondwi.student.core.domain.model.Student;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;

/**
 * Student repository.
 */
public interface StudentRepository
        extends BasicRepository<Student, Long, StudentFailure>, CriteriaRepository<Student, StudentFailure> {
    List<Student> findAllByCoursesId(Long courseId);
}
