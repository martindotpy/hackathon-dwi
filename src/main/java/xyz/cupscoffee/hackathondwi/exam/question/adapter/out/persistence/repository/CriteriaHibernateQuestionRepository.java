package xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.repository;

import org.springframework.stereotype.Component;

import xyz.cupscoffee.hackathondwi.exam.question.adapter.out.persistence.entity.QuestionEntity;
import xyz.cupscoffee.hackathondwi.shared.adapter.out.persistence.repository.CriteriaHibernateRepository;

/**
 * Criteria Hibernate repository for the question entity.
 */
@Component
public final class CriteriaHibernateQuestionRepository extends CriteriaHibernateRepository<QuestionEntity> {
}
