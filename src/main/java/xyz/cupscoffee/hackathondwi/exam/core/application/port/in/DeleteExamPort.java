package xyz.cupscoffee.hackathondwi.exam.core.application.port.in;

import xyz.cupscoffee.hackathondwi.exam.core.domain.query.failure.ExamFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete exam port.
 */
public interface DeleteExamPort {
    /**
     * Delete a exam by its ID.
     *
     * @param id The ID of the exam to delete.
     * @return the result of the operation
     */
    Result<Void, ExamFailure> deleteById(Long id);
}
