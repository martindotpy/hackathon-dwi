package xyz.cupscoffee.hackathondwi.exam.answer.application.port.in;

import java.util.List;

import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.criteria.Criteria;
import xyz.cupscoffee.hackathondwi.shared.domain.query.paginated.Paginated;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Find answer port.
 */
public interface FindAnswerPort {
    /**
     * Find all answers that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Paginated<AnswerDto> match(Criteria criteria);

    /**
     * Find one answer that match the given criteria.
     *
     * @param criteria The criteria to match.
     * @return the result of the operation
     */
    Result<AnswerDto, AnswerFailure> matchOne(Criteria criteria);

    /**
     * Find all answers by exam id.
     *
     * @param examId    The exam id.
     * @param studentId The student id.
     * @return the list of answers
     */
    List<AnswerDto> findAllByExamIdAndStudentId(Long examId, Long studentId);
}
