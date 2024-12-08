package xyz.cupscoffee.hackathondwi.exam.question.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.question.application.dto.QuestionDto;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.exam.question.domain.query.payload.CreateQuestionPayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Create question port.
 */
public interface CreateQuestionPort {
    /**
     * Create a question.
     *
     * @param payload the payload to create a question.
     * @return the result of the operation
     */
    Result<QuestionDto, QuestionFailure> create(CreateQuestionPayload payload);
}
