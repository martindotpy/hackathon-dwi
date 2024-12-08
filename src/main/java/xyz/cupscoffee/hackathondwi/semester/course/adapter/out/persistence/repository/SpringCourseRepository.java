package xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence.entity.CourseEntity;

/**
 * Spring JPA repository for course entity.
 */
public interface SpringCourseRepository extends JpaRepository<CourseEntity, Long> {
}
