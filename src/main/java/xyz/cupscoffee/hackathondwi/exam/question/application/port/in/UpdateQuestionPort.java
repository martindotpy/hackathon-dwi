package xyz.cupscoffee.hackathondwi.exam.question.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.payload.UpdateQuestionPayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update question port.
 */
public interface UpdateQuestionPort {
    /**
     * Update a question.
     *
     * @param payload the payload to update a question.
     * @return the result of the operation
     */
    Result<QuestionDto, QuestionFailure> update(UpdateQuestionPayload payload);
}
