package xyz.cupscoffee.hackathondwi.exam.question.domain.repository;

import java.util.List;

import xyz.cupscoffee.hackathondwi.exam.question.domain.model.Question;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.BasicRepository;
import xyz.cupscoffee.hackathondwi.shared.domain.repository.CriteriaRepository;

/**
 * Question repository.
 */
public interface QuestionRepository
        extends BasicRepository<Question, Long, QuestionFailure>, CriteriaRepository<Question, QuestionFailure> {
    List<Question> findAllByExamId(Long examId);
}
