package xyz.cupscoffee.hackathondwi.exam.question.application.port.in;

import java.util.List;

import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find question port.
 */
public interface FindQuestionPort {
    /**
     * Find all questions that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Paginated<QuestionDto> match(Criteria criteria);

    /**
     * Find one question that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Result<QuestionDto, QuestionFailure> matchOne(Criteria criteria);

    /**
     * Find all questions by exam id.
     *
     * @param examId The exam id.
     * @return the questions
     */
    List<QuestionDto> findAllByExamId(Long examId);
}
