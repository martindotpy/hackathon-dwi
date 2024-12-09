package xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.entity.StudentEntity;

/**
 * Spring JPA repository for student entity.
 */
public interface SpringStudentRepository extends JpaRepository<StudentEntity, Long> {
    List<StudentEntity> findAllByCoursesId(Long courseId);
}
