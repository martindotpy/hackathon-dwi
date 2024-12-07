package xyz.cupscoffee.hackathondwi.semester.core.application.port.in;

import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload.UpdateSemesterPayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Update semester port.
 */
public interface UpdateSemesterPort {
    /**
     * Update a semester.
     *
     * @param payload the payload to update a semester.
     * @return the result of the operation
     */
    Result<SemesterDto, SemesterFailure> update(UpdateSemesterPayload payload);
}
