package xyz.cupscoffee.hackathondwi.semester.core.application.port.in;

import xyz.cupscoffee.hackathondwi.semester.core.application.dto.SemesterDto;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.failure.SemesterFailure;
import xyz.cupscoffee.hackathondwi.semester.core.domain.query.payload.CreateSemesterPayload;
import xyz.cupscoffee.hackathondwi.shared.domain.query.result.Result;

/**
 * Create semester port.
 */
public interface CreateSemesterPort {
    /**
     * Create a semester.
     *
     * @param payload the payload to create a semester.
     * @param userId  the user id.
     * @return the result of the operation
     */
    Result<SemesterDto, SemesterFailure> create(CreateSemesterPayload payload, Long userId);
}
