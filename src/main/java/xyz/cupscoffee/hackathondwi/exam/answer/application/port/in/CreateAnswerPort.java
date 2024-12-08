package xyz.cupscoffee.hackathondwi.exam.answer.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.answer.application.dto.AnswerDto;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.payload.CreateAnswerPayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Create answer port.
 */
public interface CreateAnswerPort {
    /**
     * Create a answer.
     *
     * @param payload the payload to create a answer.
     * @return the result of the operation
     */
    Result<AnswerDto, AnswerFailure> create(CreateAnswerPayload payload);
}
