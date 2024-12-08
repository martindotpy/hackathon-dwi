package xyz.cupscoffee.hackathondwi.exam.answer.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.answer.domain.query.failure.AnswerFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete answer port.
 */
public interface DeleteAnswerPort {
    /**
     * Delete a answer by its ID.
     *
     * @param id The ID of the answer to delete.
     * @return the result of the operation
     */
    Result<Void, AnswerFailure> deleteById(Long id);
}
