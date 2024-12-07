package xyz.cupscoffee.hackathondwi.semester.core.application.port.in;

import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Delete semester port.
 */
public interface DeleteSemesterPort {
    /**
     * Delete a semester by its ID.
     *
     * @param id The ID of the semester to delete.
     * @return the result of the operation
     */
    Result<Void, SemesterFailure> deleteById(Long id);
}
