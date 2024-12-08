package xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence.repository;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.exam.core.adapter.out.persistence.entity.ExamEntity;
import xyz.cupscoffee.hackathondwi.shared.adapter.out.persistence.repository.CriteriaHibernateRepository;

/**
 * Criteria Hibernate repository for the exam entity.
 */
@Component
public final class CriteriaHibernateExamRepository extends CriteriaHibernateRepository<ExamEntity> {
}
