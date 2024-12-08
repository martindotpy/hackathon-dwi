package xyz.cupscoffee.hackathondwi.exam.answer.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload.UpdateAnswerPayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update answer port.
 */
public interface UpdateAnswerPort {
    /**
     * Update a answer.
     *
     * @param payload the payload to update a answer.
     * @return the result of the operation
     */
    Result<AnswerDto, AnswerFailure> update(UpdateAnswerPayload payload);
}
