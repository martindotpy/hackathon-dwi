package xyz.cupscoffee.hackathondwi.student.core.application.port.in;

import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;
import xyz.cupscoffee.hackathondwi.student.core.domain.query.failure.StudentFailure;

/**
 * Delete student port.
 */
public interface DeleteStudentPort {
    /**
     * Delete a student by its ID.
     *
     * @param id The ID of the student to delete.
     * @return the result of the operation
     */
    Result<Void, StudentFailure> deleteById(Long id);
}
