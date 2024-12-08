package xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence.repository;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.semester.course.adapter.out.persistence.entity.CourseEntity;
import xyz.cupscoffee.hackathondwi.shared.adapter.out.persistence.repository.CriteriaHibernateRepository;

/**
 * Criteria Hibernate repository for the course entity.
 */
@Component
public final class CriteriaHibernateCourseRepository extends CriteriaHibernateRepository<CourseEntity> {
}
