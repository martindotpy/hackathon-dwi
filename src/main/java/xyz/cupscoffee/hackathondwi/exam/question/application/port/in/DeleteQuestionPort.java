package xyz.cupscoffee.hackathondwi.exam.question.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.question.domain.query.failure.QuestionFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete question port.
 */
public interface DeleteQuestionPort {
    /**
     * Delete a question by its ID.
     *
     * @param id The ID of the question to delete.
     * @return the result of the operation
     */
    Result<Void, QuestionFailure> deleteById(Long id);
}
