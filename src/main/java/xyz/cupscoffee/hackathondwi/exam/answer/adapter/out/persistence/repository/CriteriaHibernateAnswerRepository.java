package xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.repository;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.exam.answer.adapter.out.persistence.entity.AnswerEntity;
import xyz.cupscoffee.hackathondwi.shared.adapter.out.persistence.repository.CriteriaHibernateRepository;

/**
 * Criteria Hibernate repository for the answer entity.
 */
@Component
public final class CriteriaHibernateAnswerRepository extends CriteriaHibernateRepository<AnswerEntity> {
}
