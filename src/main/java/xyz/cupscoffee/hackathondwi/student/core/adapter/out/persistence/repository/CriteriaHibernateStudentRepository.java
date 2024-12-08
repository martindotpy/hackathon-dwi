package xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.repository;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.shared.adapter.out.persistence.repository.CriteriaHibernateRepository;
import xyz.cupscoffee.hackathondwi.student.core.adapter.out.persistence.entity.StudentEntity;

/**
 * Criteria Hibernate repository for the student entity.
 */
@Component
public final class CriteriaHibernateStudentRepository extends CriteriaHibernateRepository<StudentEntity> {
}
