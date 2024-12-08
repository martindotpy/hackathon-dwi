package xyz.cupscoffee.hackathondwi.exam.answer.domain.repository;

import xyz.cupscoffee.hackathondwi.exam.answer.domain.model.Answer;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.BasicRepository;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.CriteriaRepository;

/**
 * Answer repository.
 */
public interface AnswerRepository
        extends BasicRepository<Answer, Long, AnswerFailure>, CriteriaRepository<Answer, AnswerFailure> {
}
